package com.kar20240703.be.base.web.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 规则： 错误代码格式：300010 300021 解释：前面5位：错误代码，最后一位：0，系统异常，1，业务异常 注意：10001开头的留给 BaseBizCodeEnum类进行配置，建议用 20001开头配置一些公用异常，实际业务从
 * 30001开头，开始使用 备注：可以每个业务的错误代码配置类，使用相同的 错误代码，比如每个业务的错误代码配置类都从 30001开始，原因：因为 R 任何的
 * error方法都会打印服务名，所有就不用关心是哪个服务报出异常，直接根据打印的服务名，找到对应的错误信息即可。
 */
@AllArgsConstructor
@Getter
public enum BaseBizCodeEnum {

    THIS_OPERATION_CANNOT_BE_PERFORMED_WITHOUT_BINDING_AN_EMAIL_ADDRESS(300011,
        "操作失败：您还没有绑定邮箱，无法进行该操作"), //

    THERE_IS_NO_BOUND_MOBILE_PHONE_NUMBER_SO_THIS_OPERATION_CANNOT_BE_PERFORMED(300021,
        "操作失败：您还没有绑定手机号码，无法进行该操作"), //

    ;

    private final int code;
    private final String msg;

}
