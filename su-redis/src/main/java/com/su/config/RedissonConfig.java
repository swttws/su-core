package com.su.config;

import com.su.properties.RedisProperties;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

/**
 * @author suweitao
 * redisson配置文件
 */
@Configuration
@EnableConfigurationProperties(RedisProperties.class)
public class RedissonConfig {

    @Bean("redissonClient")
    public RedissonClient redissonClient(RedisProperties redisProperties){
        Config config=new Config();
        //获取连接地址
        String address = redisProperties.getAddress();
        if (!StringUtils.hasText(address)){
            throw new RuntimeException("redis地址不能为空，请配置");
        }
        //格式不合法
        if (!address.contains(":")){
            throw new RuntimeException("redis配置地址不正确，redis的地址应该配置为host:port格式");
        }
        //数据库为空
        Integer database = redisProperties.getDatabase();
        if(ObjectUtils.isEmpty(database)){
            throw new RuntimeException("请指定redis数据库");
        }
        //设置配置文件
        SingleServerConfig singleServerConfig = config.useSingleServer();
        singleServerConfig.setAddress("redis://" + address);
        singleServerConfig.setPassword(redisProperties.getPassword());
        singleServerConfig.setDatabase(database);
        singleServerConfig.setConnectionPoolSize(redisProperties.getPool().getPoolSize());
        singleServerConfig.setConnectionMinimumIdleSize(redisProperties.getPool().getIdleSize());
        return Redisson.create(config);
    }

}
