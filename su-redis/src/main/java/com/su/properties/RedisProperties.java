package com.su.properties;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Primary;

/**
 * @author suweitao
 */
@Data
@Primary
@ConfigurationProperties("su.redis")
public class RedisProperties {

    /**
     * 密码
     */
    private String password;

    /**
     *连接池
     */
    private Pool pool;

    /**
     * 命名空间
     */
    private String namespace;

    /**
     * 数据库,默认使用0号数据库
     */
    private Integer database=0;

    /**
     * 连接地址,地址格式为host:port
     */
    private String address;

    @Getter
    @Setter
    public static class Pool {
        /**
         * 连接池大小
         */
        private Integer poolSize;

        /**
         * 最小空闲连接数
         */
        private Integer idleSize;

        /**
         * 连接空闲超时时间，单位：毫秒
         */
        private Integer idleTimeout;

        /**
         * 连接超时时间，单位：毫秒
         */
        private Integer connectionTimeout;

        /**
         * 等待命令超时时间，单位：毫秒
         */
        private Integer timeout;
    }

}
