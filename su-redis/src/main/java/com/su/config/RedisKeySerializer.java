package com.su.config;

import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.util.StringUtils;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * 将redis key序列化为字符串
 *
 * @author Ludeching
 */
public class RedisKeySerializer implements RedisSerializer<Object> {
    private final Charset charset;
    private final ConversionService converter;

    public RedisKeySerializer() {
        this(StandardCharsets.UTF_8);
    }

    public RedisKeySerializer(Charset charset) {
        this.charset = charset;
        this.converter = DefaultConversionService.getSharedInstance();
    }

    @Override
    public Object deserialize(byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        return new String(bytes, charset);
    }

    @Override
    public byte[] serialize(Object object) {
        Objects.requireNonNull(object, "Redis Key 不能为空");
        String key;
        if (object instanceof String) {
            key = String.valueOf(object);
        } else {
            key = converter.convert(object, String.class);
            if (!StringUtils.hasText(key)) {
                throw new RuntimeException("Redis Key 序列化失败");
            }
        }
        return key.getBytes(this.charset);
    }
}