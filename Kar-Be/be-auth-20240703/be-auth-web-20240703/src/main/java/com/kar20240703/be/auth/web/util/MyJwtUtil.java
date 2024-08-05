package com.kar20240703.be.auth.web.util;

import cn.hutool.core.convert.NumberWithFormat;
import cn.hutool.core.text.StrBuilder;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import com.kar20240703.be.auth.web.exception.AuthBizCodeEnum;
import com.kar20240703.be.auth.web.model.AuthConstant;
import com.kar20240703.be.auth.web.model.constant.SecurityConstant;
import com.kar20240703.be.auth.web.model.enums.AuthRedisKeyEnum;
import com.kar20240703.be.auth.web.model.enums.AuthRequestCategoryEnum;
import com.kar20240703.be.auth.web.model.vo.R;
import com.kar20240703.be.auth.web.properties.SecurityProperties;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MyJwtUtil {

    // 系统里的 jwt密钥
    private static final String JWT_SECRET_SYS =
        "4382dde8cb54c0c68082ada1b1d1ce048195cd309jqk0e07d2ed3e1231b462a8b75fee46467b96f33dea66a11863f1ea4867aed76243dfe7e1efb89638d3da6570d1";

    // 系统里的 jwt刷新token密钥
    private static final String JWT_REFRESH_TOKEN_SECRET_SYS =
        "4382dde8cb54c0casd82ada1b1d1ce048195cd309jqk0e07d2ed3e1871b462a8b75ghj46467b96f33dea66a11863f1ea4867aed76243dfe7e1efb89638d3da6570d1";

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
     * 生成 redis中，jwt存储使用的 key
     */
    @NotNull
    public static String generateRedisJwt(String jwtStr, Long userId, AuthRequestCategoryEnum authRequestCategoryEnum) {

        StrBuilder strBuilder = StrBuilder.create();

        strBuilder.append(AuthRedisKeyEnum.PRE_JWT.name()).append(":").append(userId).append(":")
            .append(authRequestCategoryEnum.getCode()).append(":").append(jwtStr);

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

        if (authorization == null || authorization.startsWith(SecurityConstant.JWT_PREFIX) == false) {
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
     * 通过 userId获取到权限的集合
     */
    @Nullable
    public static List<String> getAuthListByUserId(Long userId) {

        if (userId == null) {
            R.error(AuthBizCodeEnum.ILLEGAL_REQUEST); // 直接抛出异常
        }

        // admin账号，自带所有权限
        if (AuthConstant.ADMIN_ID.equals(userId)) {
            return null;
        }

        return redissonClient.<String>getList(AuthRedisKeyEnum.PRE_USER_AUTH.name() + ":" + userId).readAll();

    }

}
