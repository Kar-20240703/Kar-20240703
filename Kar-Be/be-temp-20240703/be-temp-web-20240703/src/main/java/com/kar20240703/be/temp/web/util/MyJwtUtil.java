package com.kar20240703.be.temp.web.util;

import cn.hutool.core.convert.NumberWithFormat;
import cn.hutool.core.text.StrBuilder;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.RegisteredPayload;
import com.kar20240703.be.temp.web.exception.TempBizCodeEnum;
import com.kar20240703.be.temp.web.model.constant.SecurityConstant;
import com.kar20240703.be.temp.web.model.enums.TempRedisKeyEnum;
import com.kar20240703.be.temp.web.model.enums.TempRequestCategoryEnum;
import com.kar20240703.be.temp.web.model.vo.R;
import com.kar20240703.be.temp.web.properties.SecurityProperties;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.redisson.api.RedissonClient;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MyJwtUtil {

    // 系统里的 jwt密钥
    private static final String JWT_SECRET_SYS =
        "4382dde8cb54c0c68082ada1b1d1ce048195cd309jqk0e07d2ed3e1871b462a8b75fee46467b96f33dea66a11863f1ea4867aed76243dfe7e1efb89638d3da6570d1";

    public static final String PAYLOAD_MAP_USER_ID_KEY = "userId";

    public static final String PAYLOAD_MAP_WX_APP_ID_KEY = "wxAppId";

    public static final String PAYLOAD_MAP_WX_OPEN_ID_KEY = "wxOpenId";

    private static SecurityProperties securityProperties;

    private static RedissonClient redissonClient;

    public MyJwtUtil(SecurityProperties securityProperties, RedissonClient redissonClient) {

        MyJwtUtil.securityProperties = securityProperties;
        MyJwtUtil.redissonClient = redissonClient;

    }

    /**
     * 获取：jwt中的 wxAppId值
     */
    @Nullable
    public static String getPayloadMapWxAppIdValue(@Nullable JSONObject claimsJson) {

        if (claimsJson == null) {
            return null;
        }

        return claimsJson.getStr(MyJwtUtil.PAYLOAD_MAP_WX_APP_ID_KEY);

    }

    /**
     * 获取：jwt中的 wxOpenId值
     */
    @Nullable
    public static String getPayloadMapWxOpenIdValue(@Nullable JSONObject claimsJson) {

        if (claimsJson == null) {
            return null;
        }

        return claimsJson.getStr(MyJwtUtil.PAYLOAD_MAP_WX_OPEN_ID_KEY);

    }

    /**
     * 获取：jwt中的 userId值
     */
    @Nullable
    public static Long getPayloadMapUserIdValue(@Nullable JSONObject claimsJson) {

        if (claimsJson == null) {
            return null;
        }

        NumberWithFormat numberWithFormat = (NumberWithFormat)claimsJson.get(MyJwtUtil.PAYLOAD_MAP_USER_ID_KEY);

        if (numberWithFormat == null) {
            return null;
        }

        return numberWithFormat.longValue();

    }

    /**
     * 生成 redis中，jwt存储使用的 key（jwtHash），目的：不直接暴露明文的 jwt
     */
    @NotNull
    public static String generateRedisJwtHash(String jwtStr, Long userId,
        TempRequestCategoryEnum tempRequestCategoryEnum) {

        StrBuilder strBuilder = StrBuilder.create();

        strBuilder.append(TempRedisKeyEnum.PRE_JWT_HASH.name()).append(":").append(userId).append(":")
            .append(tempRequestCategoryEnum.getCode()).append(":").append(DigestUtil.sha512Hex(jwtStr));

        return strBuilder.toString();

    }

    /**
     * 获取 jwt密钥：配置的私钥前缀 + JWT_SECRET_SYS
     */
    @NotNull
    public static String getJwtSecret() {

        return MyJwtUtil.securityProperties.getJwtSecretPre() + MyJwtUtil.JWT_SECRET_SYS;

    }

    /**
     * 从请求头里，获取：jwt字符串
     */
    @Nullable
    public static String getJwtStrByRequest(HttpServletRequest request) {

        String authorization = request.getHeader(SecurityConstant.AUTHORIZATION);

        if (authorization == null || BooleanUtil.isFalse(authorization.startsWith(SecurityConstant.JWT_PREFIX))) {
            return null;
        }

        String jwtStr = getJwtStrByHeadAuthorization(authorization);

        if (StrUtil.isBlank(jwtStr)) {
            return null;
        }

        return jwtStr;

    }

    /**
     * 获取：jwtStr
     */
    public static String getJwtStrByHeadAuthorization(String authorization) {

        return authorization.replace(SecurityConstant.JWT_PREFIX, "");

    }

    /**
     * 通过 userId获取到权限的 set
     */
    @Nullable
    public static Set<SimpleGrantedAuthority> getSimpleGrantedAuthorityListByUserId(Long userId) {

        if (userId == null) {
            R.error(TempBizCodeEnum.ILLEGAL_REQUEST); // 直接抛出异常
        }

        // admin账号，自带所有权限
        if (UserUtil.getCurrentUserAdminFlag(userId)) {
            return null;
        }

        // 组装权限，并去重
        Set<String> authSet = new HashSet<>();

        return authSet.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toSet());

    }

    /**
     * 获取：请求里面的 jwtHash值
     */
    public static String getJwtHashByRequest(HttpServletRequest httpServletRequest,
        @Nullable CallBack<Long> jwtHashRemainMsCallBack, @Nullable CallBack<Long> expireTsCallBack) {

        // 从请求头里，获取：jwt字符串
        String jwtStr = MyJwtUtil.getJwtStrByRequest(httpServletRequest);

        if (jwtStr == null) {
            return null;
        }

        JWT jwt = JWT.of(jwtStr); // 备注：这里不会报错

        JSONObject claimsJson = jwt.getPayload().getClaimsJson();

        Date expiresDate = claimsJson.getDate(RegisteredPayload.EXPIRES_AT);

        if (expiresDate == null) { // 备注：这里不会为 null
            return null;
        }

        Long currentUserId = UserUtil.getCurrentUserId();

        String jwtHash = MyJwtUtil.generateRedisJwtHash(jwtStr, currentUserId,
            RequestUtil.getRequestCategoryEnum(httpServletRequest));

        // jwt剩余时间
        long remainMs = expiresDate.getTime() - System.currentTimeMillis();

        if (jwtHashRemainMsCallBack != null) {

            jwtHashRemainMsCallBack.setValue(remainMs);

        }

        if (expireTsCallBack != null) {

            expireTsCallBack.setValue(expiresDate.getTime());

        }

        return jwtHash;

    }

}
