package com.kar20240703.be.base.web.model.enums;

import com.kar20240703.be.temp.web.model.interfaces.IRedisKey;

/**
 * redis中 key的枚举类 备注：如果是 redisson的锁 key，一定要备注：锁什么，例如：锁【用户主键 id】 备注：【PRE_】开头，表示 key后面还要跟字符串 备注：【_CACHE】结尾，表示 key后面不用跟字符串
 */
public enum BaseRedisKeyEnum implements IRedisKey {

    // 【PRE_】开头 ↓
    PRE_WX_APP_ID, // 微信 appId（应用）：锁：【微信 appId】
    PRE_WX_OPEN_ID, // 微信 openId（用户）：锁：【微信 openId】，备注：一般锁：微信 openId，不锁：微信 appId
    PRE_WX_UNION_ID, // 微信 unionId（平台）：锁：【微信 unionId】

    PRE_PHONE, // 手机号码：锁：【手机号码】
    PRE_EMAIL, // 邮箱：锁：【邮箱】
    PRE_USER_NAME, // 用户名：锁：【用户名】

    PRE_TOO_MANY_PASSWORD_ERROR, // 密码错误次数太多：锁【用户主键 id】
    PRE_PASSWORD_ERROR_COUNT, // 密码错误总数：锁【用户主键 id】

    // 【_CACHE】结尾 ↓

    // 其他 ↓

    ;

}
