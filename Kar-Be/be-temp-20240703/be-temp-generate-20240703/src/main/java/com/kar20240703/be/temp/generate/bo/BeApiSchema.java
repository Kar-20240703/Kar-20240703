package com.kar20240703.be.temp.generate.bo;

import com.kar20240703.be.temp.generate.interfaces.BeApiField;
import java.util.List;
import java.util.Map;
import lombok.Data;

/**
 * 请求的是对象时
 */
@Data
public class BeApiSchema implements BeApiField {

    /**
     * 对象的 class名
     */
    private String className;

    /**
     * 对象传递时的名称，备注：只有此对象是一个对象的字段时，才有值
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
     * 对象的字段情况 map，key是参数名
     */
    private Map<String, BeApiField> fieldMap;

    /**
     * 必传的字段名集合
     */
    private List<String> requiredFieldName;

    /**
     * 是否是集合
     */
    private Boolean arrFlag;

}
