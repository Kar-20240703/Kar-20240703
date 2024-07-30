package com.kar20240703.be.temp.web.model.enums;

/**
 * redis中 key的枚举类 备注：如果是 redisson的锁 key，一定要备注：锁什么，例如：锁【用户主键 id】 备注：【PRE_】开头，表示 key后面还要跟字符串 备注：【_CACHE】结尾，表示 key后面不用跟字符串
 */
public enum TempRedisKeyEnum {

    // 【PRE_】开头 ↓
    PRE_JWT_HASH, // jwtHash 前缀，后面跟：userId:requestCategoryEnum，目的：可以退出该用户的所有登录

    // 【_CACHE】结尾 ↓

    // 其他 ↓
    ATOMIC_LONG_ID_GENERATOR, // 获取主键 id，自增值

    ;

}
