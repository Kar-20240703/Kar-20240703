package com.kar20240703.be.base.web.util;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.hutool.jwt.JWT;
import com.kar20240703.be.base.web.model.enums.BaseRedisKeyEnum;
import com.kar20240703.be.temp.web.model.constant.SecurityConstant;
import com.kar20240703.be.temp.web.model.constant.TempConstant;
import com.kar20240703.be.temp.web.model.enums.TempRequestCategoryEnum;
import com.kar20240703.be.temp.web.model.vo.SignInVO;
import com.kar20240703.be.temp.web.util.MyJwtUtil;
import com.kar20240703.be.temp.web.util.RedissonUtil;
import java.time.Duration;
import java.util.Date;
import java.util.function.Consumer;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class BaseJwtUtil {

    /**
     * 统一生成 jwt
     */
    @Nullable
    public static SignInVO generateJwt(Long userId, @Nullable Consumer<JSONObject> consumer,
        boolean generateRefreshTokenFlag, TempRequestCategoryEnum tempRequestCategoryEnum,
        @Nullable String jwtRefreshToken) {

        if (userId == null) {
            return null;
        }

        RedissonUtil.batch(batch -> {

            // 移除密码错误次数相关
            batch.getBucket(BaseRedisKeyEnum.PRE_PASSWORD_ERROR_COUNT.name() + ":" + userId).deleteAsync();
            batch.getBucket(BaseRedisKeyEnum.PRE_TOO_MANY_PASSWORD_ERROR.name() + ":" + userId).deleteAsync();

        });

        // 生成 jwt
        return sign(userId, consumer, generateRefreshTokenFlag, tempRequestCategoryEnum, jwtRefreshToken);

    }

    /**
     * 生成 jwt
     */
    @NotNull
    private static SignInVO sign(Long userId, @Nullable Consumer<JSONObject> consumer, boolean generateRefreshTokenFlag,
        TempRequestCategoryEnum tempRequestCategoryEnum, @Nullable String jwtRefreshTokenTemp) {

        // 备注：jwtRefreshToken请求，需要同步进行修改
        JSONObject payloadMap = JSONUtil.createObj();

        payloadMap.set(MyJwtUtil.PAYLOAD_MAP_USER_ID_KEY, userId);

        if (consumer != null) {
            consumer.accept(payloadMap);
        }

        long jwtExpireTime = TempConstant.JWT_EXPIRE_TIME;

        // 过期时间
        Date expireTs = new Date(System.currentTimeMillis() + jwtExpireTime);

        String jwt = JWT.create() //
            .setExpiresAt(expireTs) // 设置过期时间
            .addPayloads(payloadMap) // 增加JWT载荷信息
            .setKey(MyJwtUtil.getJwtSecret().getBytes()) // 设置密钥
            .sign();

        String jwtRefreshToken;

        if (generateRefreshTokenFlag) {

            jwtRefreshToken = JWT.create() //
                .addPayloads(payloadMap) // 增加JWT载荷信息
                .setKey(MyJwtUtil.getJwtRefreshTokenSecret().getBytes()) // 设置密钥
                .sign();

        } else {

            jwtRefreshToken = jwtRefreshTokenTemp;

        }

        RedissonUtil.batch(batch -> {

            if (generateRefreshTokenFlag) {

                batch.getBucket(MyJwtUtil.generateRedisJwtRefreshToken(jwtRefreshToken, userId))
                    .setAsync(jwtRefreshToken);

            }

            batch.getBucket(MyJwtUtil.generateRedisJwt(jwt, userId, tempRequestCategoryEnum))
                .setAsync(jwt, Duration.ofMillis(jwtExpireTime));

        });

        return new SignInVO(SecurityConstant.JWT_PREFIX + jwt, expireTs.getTime() - (10 * 60 * 1000), jwtRefreshToken);

    }

}
