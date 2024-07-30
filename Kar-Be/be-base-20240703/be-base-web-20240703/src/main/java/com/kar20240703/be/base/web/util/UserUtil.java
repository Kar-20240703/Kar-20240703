package com.kar20240703.be.base.web.util;

import cn.hutool.core.convert.NumberWithFormat;
import cn.hutool.core.lang.func.VoidFunc0;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.kar20240703.be.base.web.exception.BizCodeEnum;
import com.kar20240703.be.base.web.model.constant.BaseConstant;
import com.kar20240703.be.base.web.model.domain.BaseUserDO;
import com.kar20240703.be.base.web.model.vo.R;
import java.util.Set;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class UserUtil {

    //    private static SysRoleMapper sysRoleMapper;
    //    private static SysRoleRefUserMapper sysRoleRefUserMapper;
    //    public static SysUserMapper sysUserMapper;
    //    private static SysUserInfoMapper sysUserInfoMapper;
    //
    //    public UserUtil(SysRoleMapper sysRoleMapper, SysRoleRefUserMapper sysRoleRefUserMapper, SysUserMapper sysUserMapper,
    //        SysUserInfoMapper sysUserInfoMapper) {
    //
    //        UserUtil.sysRoleMapper = sysRoleMapper;
    //        UserUtil.sysRoleRefUserMapper = sysRoleRefUserMapper;
    //        UserUtil.sysUserMapper = sysUserMapper;
    //        UserUtil.sysUserInfoMapper = sysUserInfoMapper;
    //
    //    }

    /**
     * 获取当前 userId 这里只会返回实际的 userId，如果为 null，则会抛出异常
     */
    @NotNull
    public static Long getCurrentUserId() {

        Long userId = getCurrentUserIdWillNull();

        if (userId == null) {
            R.error(BizCodeEnum.NOT_LOGGED_IN_YET);
        }

        return userId;

    }

    /**
     * 获取当前 userId，如果是 admin账号，则会报错，只会返回 用户id，不会返回 null 因为 admin不支持一些操作，例如：修改密码，修改邮箱等
     */
    @NotNull
    public static Long getCurrentUserIdNotAdmin() {

        Long currentUserId = getCurrentUserId();

        if (UserUtil.getCurrentUserAdminFlag(currentUserId)) {
            R.error(BizCodeEnum.THE_ADMIN_ACCOUNT_DOES_NOT_SUPPORT_THIS_OPERATION);
        }

        return currentUserId;

    }

    /**
     * 这里只会返回实际的 userId 或者 -1，备注：-1表示没有 用户id，在大多数情况下，表示的是 系统 备注：尽量采用 {@link #getCurrentUserId} 方法
     */
    @NotNull
    public static Long getCurrentUserIdDefault() {

        Long userId = getCurrentUserIdWillNull();

        if (userId == null) {
            userId = BaseConstant.SYS_ID;
        }

        return userId;

    }

    /**
     * 这里只会返回实际的 wxOpenId 或者 空字符串
     */
    @NotNull
    public static String getCurrentUserWxOpenIdDefault() {

        String currentUserWxOpenIdWillNull = getCurrentUserWxOpenIdWillNull();

        if (currentUserWxOpenIdWillNull == null) {
            currentUserWxOpenIdWillNull = "";
        }

        return currentUserWxOpenIdWillNull;

    }

    /**
     * 用户是否是系统管理员
     */
    public static boolean getCurrentUserAdminFlag() {

        return BaseConstant.ADMIN_ID.equals(getCurrentUserIdDefault());

    }

    /**
     * 用户是否是系统管理员
     */
    public static boolean getCurrentUserAdminFlag(Long userId) {

        return BaseConstant.ADMIN_ID.equals(userId);

    }

    /**
     * 获取当前 wxAppId，注意：这里获取 wxAppId之后需要做 非空判断 这里只会返回实际的 wxAppId或者 null
     */
    @Nullable
    private static String getCurrentUserWxAppIdWillNull() {

        return MyJwtUtil.getPayloadMapWxAppIdValue(getSecurityContextHolderContextAuthenticationPrincipalJsonObject());

    }

    /**
     * 获取当前 wxOpenId，注意：这里获取 wxOpenId之后需要做 非空判断 这里只会返回实际的 wxOpenId或者 null
     */
    @Nullable
    private static String getCurrentUserWxOpenIdWillNull() {

        return MyJwtUtil.getPayloadMapWxOpenIdValue(getSecurityContextHolderContextAuthenticationPrincipalJsonObject());

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
    public static Authentication getSecurityContextHolderContextAuthentication() {

        return SecurityContextHolder.getContext().getAuthentication();

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
     * 获取：当前 security上下文里面存储的用户信息，通过：key
     */
    @Nullable
    public static <T> T getSecurityContextHolderContextAuthenticationPrincipalJsonObjectValueByKey(String key) {

        JSONObject principalJSONObject = getSecurityContextHolderContextAuthenticationPrincipalJsonObject();

        if (principalJSONObject == null) {
            return null;
        }

        return (T)principalJSONObject.get(key);

    }

    /**
     * 通过用户 id，获取 菜单 set type：1 完整的菜单信息 2 给 security获取权限时使用
     */
    @Nullable
    public static Set<Long> getMenuSetByUserId(@NotNull Long userId, int type) {

        return null;

    }

    /**
     * 给 security设置用户信息，并执行方法
     */
    public static void securityContextHolderSetAuthenticationAndExecFun(VoidFunc0 voidFunc0, BaseUserDO baseUserDO,
        boolean setAuthoritySetFlag) {

        // 执行
        securityContextHolderSetAuthenticationAndExecFun(voidFunc0, baseUserDO.getId(), baseUserDO.getWxAppId(),
            baseUserDO.getWxOpenId(), setAuthoritySetFlag);

    }

    /**
     * 给 security设置用户信息，并执行方法
     *
     * @param setAuthoritySetFlag 是否设置：权限
     */
    public static void securityContextHolderSetAuthenticationAndExecFun(VoidFunc0 voidFunc0, @Nullable Long userId,
        @Nullable String wxAppId, @Nullable String wxOpenId, boolean setAuthoritySetFlag) {

        JSONObject principalJson = JSONUtil.createObj();

        if (userId != null) {

            principalJson.set(MyJwtUtil.PAYLOAD_MAP_USER_ID_KEY, new NumberWithFormat(userId, null));

        }

        if (StrUtil.isNotBlank(wxAppId)) {

            principalJson.set(MyJwtUtil.PAYLOAD_MAP_WX_APP_ID_KEY, wxAppId);

        }

        if (StrUtil.isNotBlank(wxOpenId)) {

            principalJson.set(MyJwtUtil.PAYLOAD_MAP_WX_OPEN_ID_KEY, wxOpenId);

        }

        Set<SimpleGrantedAuthority> authoritySet = null;

        if (setAuthoritySetFlag) {

            authoritySet = MyJwtUtil.getSimpleGrantedAuthorityListByUserId(userId);

        }

        // 把 principalJson 设置到：security的上下文里面
        SecurityContextHolder.getContext()
            .setAuthentication(new UsernamePasswordAuthenticationToken(principalJson, null, authoritySet));

        MyTryUtil.tryCatchFinally(() -> {

            // 执行方法
            voidFunc0.call();

        }, () -> {

            SecurityContextHolder.clearContext(); // 清除：当前线程存储的值

        });

    }

}
