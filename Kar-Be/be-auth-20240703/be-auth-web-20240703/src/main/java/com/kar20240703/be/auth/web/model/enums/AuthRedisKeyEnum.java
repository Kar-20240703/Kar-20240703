package com.kar20240703.be.auth.web.model.enums;

/**
 * redis中 key的枚举类 备注：如果是 redisson的锁 key，一定要备注：锁什么，例如：锁【用户主键 id】 备注：【PRE_】开头，表示 key后面还要跟字符串 备注：【_CACHE】结尾，表示 key后面不用跟字符串
 */
public enum AuthRedisKeyEnum {

    // 【PRE_】开头 ↓
    PRE_USER_DISABLE, // 用户是否被冻结前缀，后面跟：userId，如果存在，则表示，用户被冻结了

    PRE_USER_AUTH, // 用户权限前缀，后面跟：userId

    PRE_JWT, // jwt 前缀，后面跟：userId:requestCategoryEnum:jwt

    // 【_CACHE】结尾 ↓
    DEFAULT_USER_AUTH_CACHE, // 默认用户权限

    // 其他 ↓

    ;

}
