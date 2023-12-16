package com.su.base;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author suweitao
 * 公共字段
 */
@Data
public class BaseEntity implements Serializable {

    /**
     * 用户id
     */
    private Integer id;

    /**
     * 逻辑删除字段，0未删除，1已删除
     */
    private Boolean isDelete;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    private LocalDateTime updateTime;

    /**
     * 版本号
     */
    private String version;

}
