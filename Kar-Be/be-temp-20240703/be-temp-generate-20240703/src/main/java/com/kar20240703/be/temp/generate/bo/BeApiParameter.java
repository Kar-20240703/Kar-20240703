package com.kar20240703.be.temp.generate.bo;

import com.kar20240703.be.temp.generate.interfaces.BeApiField;
import lombok.Data;

/**
 * 请求的是一般类型时
 */
@Data
public class BeApiParameter implements BeApiField {

    /**
     * 字段名称
     */
    private String name;

    /**
     * 对象的类型
     */
    private String type;

    /**
     * 是否必须传递
     */
    private Boolean required;

    /**
     * 字段描述
     */
    private String description;

    /**
     * 字段格式化
     */
    private String format;

    /**
     * 正则表达式
     */
    private String pattern;

    /**
     * 最大长度
     */
    private Integer maxLength;

    /**
     * 最小长度
     */
    private Integer minLength;

    /**
     * 是否是集合
     */
    private Boolean arrFlag;

}
