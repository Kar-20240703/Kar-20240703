package com.kar20240703.be.temp.generate.bo;

import com.kar20240703.be.temp.generate.interfaces.BeApiField;
import java.util.Map;
import lombok.Data;

/**
 * 一个接口的信息描述
 */
@Data
public class BeApi {

    /**
     * 请求路径
     */
    private String path;

    /**
     * 请求的方法
     */
    private String method;

    /**
     * 请求的所属的标签
     */
    private String tag;

    /**
     * 请求的所属的分组
     */
    private String group;

    /**
     * 请求的描述
     */
    private String summary;

    /**
     * contentType
     */
    private String contentType;

    /**
     * post，json请求时，需要传递的对象
     */
    private BeApiSchema requestBody;

    /**
     * 一般的 get或者 post 请求时，传递的参数，key是参数名
     */
    private Map<String, BeApiField> parameter;

    /**
     * 响应的对象
     */
    private BeApiField response;

    /**
     * 生成前端 api时用，api接口需要返回的类型
     */
    private String returnTypeStr;

    /**
     * 生成前端 api时用，api接口需要返回的类型，是否是数组类型
     */
    private Boolean returnTypeArrFlag;

}
