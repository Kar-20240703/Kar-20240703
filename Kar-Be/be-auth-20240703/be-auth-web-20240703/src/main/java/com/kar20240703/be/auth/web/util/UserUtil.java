package com.kar20240703.be.auth.web.util;

import cn.hutool.json.JSONObject;
import com.kar20240703.be.auth.web.exception.AuthBizCodeEnum;
import com.kar20240703.be.auth.web.model.constant.AuthConstant;
import com.kar20240703.be.auth.web.model.vo.R;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class UserUtil {

    /**
     * 获取当前 userId 这里只会返回实际的 userId，如果为 null，则会抛出异常
     */
    @NotNull
    public static Long getCurrentUserId() {

        Long userId = getCurrentUserIdWillNull();

        if (userId == null) {
            R.error(AuthBizCodeEnum.NOT_LOGGED_IN_YET);
        }

        return userId;

    }

    /**
     * 这里只会返回实际的 userId 或者 -1，备注：-1表示没有 用户id，在大多数情况下，表示的是 系统 备注：尽量采用 {@link #getCurrentUserId} 方法
     */
    @NotNull
    public static Long getCurrentUserIdDefault() {

        Long userId = getCurrentUserIdWillNull();

        if (userId == null) {
            userId = AuthConstant.SYS_ID;
        }

        return userId;

    }

    /**
     * 获取当前 userId，注意：这里获取 userId之后需要做 非空判断 这里只会返回实际的 userId或者 null
     */
    @Nullable
    private static Long getCurrentUserIdWillNull() {

        return MyJwtUtil.getPayloadMapUserIdValue(getSecurityContextHolderContextAuthenticationPrincipalJsonObject());

    }

    /**
     * 获取：当前 security上下文里面存储的用户信息
     */
    @Nullable
    public static JSONObject getSecurityContextHolderContextAuthenticationPrincipalJsonObject() {

        JSONObject result = null;

        if (SecurityContextHolder.getContext().getAuthentication() != null) {

            Object principalObject = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            if (principalObject instanceof JSONObject) {
                result = (JSONObject)principalObject;
            }

        }

        return result;

    }

    /**
     * 用户是否是系统管理员
     */
    public static boolean getCurrentUserAdminFlag() {

        return AuthConstant.ADMIN_ID.equals(getCurrentUserIdDefault());

    }

    /**
     * 用户是否是系统管理员
     */
    public static boolean getCurrentUserAdminFlag(Long userId) {

        return AuthConstant.ADMIN_ID.equals(userId);

    }

}
