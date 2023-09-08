package com.su.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

/**
 * @author suweitao
 */
@Data
public class LoadFileEntity {

    /**
     * 加载路径
     */
    private String filePath;

    /**
     * 资源加载的优先级
     */
    private Integer order;

    /**
     * 资源文件后缀扩展名
     */
    private String extension;

    public LoadFileEntity(String filePath,Integer order){
        this.filePath=filePath;
        this.order=order;
        this.extension= StringUtils.getFilenameExtension(filePath);
    }

}
