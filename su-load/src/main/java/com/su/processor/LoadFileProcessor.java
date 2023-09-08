package com.su.processor;

import com.su.annotation.LoadFile;
import com.su.entity.LoadFileEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.env.PropertySourceLoader;
import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.SpringFactoriesLoader;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 自定义资源加载器
 *
 * @author suweitao
 */
@Slf4j
public class LoadFileProcessor implements BeanFactoryPostProcessor {

    /**
     * 资源文件加载器
     */
    private final ResourceLoader resourceLoader;

    /**
     * 资源加载器map
     */
    private final ConcurrentHashMap<String, PropertySourceLoader> loaderMap = new ConcurrentHashMap<>();

    public LoadFileProcessor() {
        this.resourceLoader = new DefaultResourceLoader();
        //获取spring.factories中的yml和properties加载器
        List<PropertySourceLoader> propertySourceLoaderList = SpringFactoriesLoader.loadFactories(PropertySourceLoader.class, getClass().getClassLoader());
        //资源加载器缓存到map中
        for (PropertySourceLoader propertySourceLoader : propertySourceLoaderList) {
            //获取后缀扩展名
            String[] fileExtensions = propertySourceLoader.getFileExtensions();
            for (String fileExtension : fileExtensions) {
                //保存后缀扩展名：资源加载器对应映射关系
                loaderMap.put(fileExtension, propertySourceLoader);
            }
        }
    }

    /**
     * 读取自定义配置文件
     *
     * @param beanFactory bean工厂
     * @throws BeansException
     */
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        //获取标注了自定义注解LoadFile的类
        Map<String, Object> loadFileBeans = beanFactory.getBeansWithAnnotation(LoadFile.class);
        //将存储容器转换为list集合
        List<LoadFileEntity> loadFileEntityList = loadFileBeans.values().stream().map(bean -> {
            Class<?> clazz = bean.getClass();
            //获取对应的注解
            LoadFile annotation = clazz.getAnnotation(LoadFile.class);
            if (ObjectUtils.isEmpty(annotation)) {
                return null;
            }
            return new LoadFileEntity(annotation.filePath(), annotation.order());
        }).filter(Objects::nonNull)
                .sorted(Comparator.comparing(LoadFileEntity::getOrder))
                .collect(Collectors.toList());

        //根据对应的配置文件类型，读取对应的配置文件
        List<PropertySource<?>> propertySourceList = new ArrayList<>();
        for (LoadFileEntity loadFile : loadFileEntityList) {
            //根据后缀扩展名获取对应资源加载器
            String extension = loadFile.getExtension();
            PropertySourceLoader propertySourceLoader = loaderMap.get(extension);
            //获取不到对应扩展名资源加载器
            if(ObjectUtils.isEmpty(propertySourceLoader)){
                throw new IllegalStateException("加载资源【"+loadFile.getFilePath()+"】错误,请检查对应扩展名是否正确");
            }
            //加载资源
            Resource resource = resourceLoader.getResource(loadFile.getFilePath());
            //读取资源配置到全局配置中
            try {
                List<PropertySource<?>> load = propertySourceLoader.load(loadFile.getFilePath(), resource);
                log.info("资源加载：【"+loadFile.getFilePath()+"]");
                propertySourceList.addAll(load);
            } catch (IOException ioException) {
                throw new RuntimeException(ioException);
            }
        }

        //获取系统资源配置
        ConfigurableEnvironment environment = beanFactory.getBean(ConfigurableEnvironment.class);
        MutablePropertySources propertySources = environment.getPropertySources();
        //添加对应的资源配置到系统全局配置中
        for (PropertySource<?> propertySource : propertySourceList) {
            propertySources.addLast(propertySource);
        }
    }

}
