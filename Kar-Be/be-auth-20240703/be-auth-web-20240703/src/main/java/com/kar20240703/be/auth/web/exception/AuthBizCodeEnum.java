package com.kar20240703.be.auth.web.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 规则： 错误代码格式：300010 300021 解释：前面5位：错误代码，最后一位：0，系统异常，1，业务异常 注意：10001开头的留给 BaseBizCodeEnum类进行配置，建议用 20001开头配置一些公用异常，实际业务从
 * 30001开头，开始使用 备注：可以每个业务的错误代码配置类，使用相同的 错误代码，比如每个业务的错误代码配置类都从 30001开始，原因：因为 R 任何的
 * error方法都会打印服务名，所有就不用关心是哪个服务报出异常，直接根据打印的服务名，找到对应的错误信息即可。
 */
@AllArgsConstructor
@Getter
public enum AuthBizCodeEnum {

    RESULT_OK(200, AuthBizCodeEnum.OK), //
    RESULT_SEND_OK(200, AuthBizCodeEnum.SEND_OK), //

    RESULT_SYS_ERROR(100010, "系统异常，请联系管理员"), //
    PARAMETER_CHECK_ERROR(100011, "参数校验出现问题"), //
    ILLEGAL_REQUEST(100021, "非法请求"), //
    INSUFFICIENT_PERMISSIONS(100041, "权限不足"), //

    NOT_LOGGED_IN_YET(100111, "尚未登录，请先登录"), // 返回这个 code（100111），会触发前端，登出功能
    LOGIN_EXPIRED(100111, "登录过期，请重新登录"), //
    ACCOUNT_IS_DISABLED(100111, "账户被冻结，请联系管理员"), //

    ;

    private final int code;
    private final String msg;

    public static final String OK = "操作成功";
    public static final String SEND_OK = "发送成功";
    public static final String OK_ENGLISH = "ok";

}
