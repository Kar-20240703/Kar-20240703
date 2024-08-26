package com.kar20240703.be.base.web.exception;

import com.kar20240703.be.temp.web.model.interfaces.IBizCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 规则： 错误代码格式：300010 300021 解释：前面5位：错误代码，最后一位：0，系统异常，1，业务异常 注意：10001开头的留给 BaseBizCodeEnum类进行配置，建议用 20001开头配置一些公用异常，实际业务从
 * 30001开头，开始使用 备注：可以每个业务的错误代码配置类，使用相同的 错误代码，比如每个业务的错误代码配置类都从 30001开始，原因：因为 R 任何的
 * error方法都会打印服务名，所有就不用关心是哪个服务报出异常，直接根据打印的服务名，找到对应的错误信息即可。
 */
@AllArgsConstructor
@Getter
public enum BaseBizCodeEnum implements IBizCode {

    EMAIL_HAS_BEEN_REGISTERED(300011, "操作失败：该邮箱已被占用，请重新输入"), //
    PHONE_HAS_BEEN_REGISTERED(300021, "操作失败：该手机号码已被占用，请重新输入"), //
    TOO_MANY_PASSWORD_ERROR(300031, "密码错误次数过多，已被冻结，请点击【忘记密码了】，进行密码修改"), //
    NO_PASSWORD_SET(300041, "未设置密码，请点击【忘记密码了】，进行密码设置"), //
    ACCOUNT_OR_PASSWORD_NOT_VALID(300051, "账号或密码错误"), //
    USER_DOES_NOT_EXIST(300071, "操作失败：用户不存在"), //
    PASSWORD_NOT_VALID(300081, "操作失败：当前密码错误"), //
    THE_ACCOUNT_HAS_ALREADY_BEEN_REGISTERED(300091, "操作失败：该账号已被注册，请重试"), //
    PASSWORD_RESTRICTIONS(300101, "密码限制：必须包含大小写字母和数字，可以使用特殊字符，长度8-20"), //
    USER_NAME_EXIST_PLEASE_RE_ENTER(300111, "操作失败：该用户名已被占用，请重新输入"), //

    THIS_OPERATION_CANNOT_BE_PERFORMED_WITHOUT_BINDING_AN_EMAIL_ADDRESS(300121,
        "操作失败：您还没有绑定邮箱，无法进行该操作"), //

    THERE_IS_NO_BOUND_MOBILE_PHONE_NUMBER_SO_THIS_OPERATION_CANNOT_BE_PERFORMED(300131,
        "操作失败：您还没有绑定手机号码，无法进行该操作"), //

    MENU_PATH_IS_EXIST(300141, "操作失败：路径重复"), //

    MENU_UUID_IS_EXIST(300151, "操作失败：唯一标识重复"), //

    THE_SAME_ROLE_NAME_EXIST(300161, "操作失败：存在相同的角色名"), //

    THE_SAME_AUTH_NAME_EXIST(300171, "操作失败：存在相同的权限名"), //

    ;

    private final int code;
    private final String msg;

}
