package com.kar20240703.be.temp.web.filter;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.exceptions.ValidateException;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTValidator;
import com.kar20240703.be.temp.web.configuration.base.TempConfiguration;
import com.kar20240703.be.temp.web.configuration.security.SecurityConfiguration;
import com.kar20240703.be.temp.web.exception.TempBizCodeEnum;
import com.kar20240703.be.temp.web.model.configuration.IJwtConfiguration;
import com.kar20240703.be.temp.web.model.configuration.IJwtValidatorConfiguration;
import com.kar20240703.be.temp.web.model.vo.R;
import com.kar20240703.be.temp.web.model.vo.SignInVO;
import com.kar20240703.be.temp.web.properties.SecurityProperties;
import com.kar20240703.be.temp.web.util.MyExceptionUtil;
import com.kar20240703.be.temp.web.util.MyJwtUtil;
import com.kar20240703.be.temp.web.util.RequestUtil;
import com.kar20240703.be.temp.web.util.ResponseUtil;
import com.kar20240703.be.temp.web.util.UserUtil;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * 自定义 jwt过滤器，备注：后续接口方法，无需判断账号是否封禁或者不存在
 */
@Slf4j
@Component
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    @Resource
    SecurityProperties securityProperties;

    @Resource
    List<IJwtValidatorConfiguration> iJwtValidatorConfigurationList;

    @Nullable
    IJwtConfiguration iJwtConfiguration;

    public JwtAuthorizationFilter(@Autowired(required = false) @Nullable IJwtConfiguration iJwtConfiguration) {

        this.iJwtConfiguration = iJwtConfiguration;

    }

    @SneakyThrows
    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response,
        @NotNull FilterChain filterChain) {

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = getAuthentication(request, response);

        if (usernamePasswordAuthenticationToken != null) {
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        }

        filterChain.doFilter(request, response);

    }

    @SneakyThrows
    @Nullable
    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request,
        HttpServletResponse response) {

        // 从请求头里，获取：jwt字符串，备注：就算加了不需要登录就可以访问，但是也会走该方法
        String jwtStr = MyJwtUtil.getJwtStrByRequest(request);

        if (jwtStr == null) {
            return null;
        }

        if (SecurityConfiguration.permitAllCheck(request)) {
            return null;
        }

        jwtStr = handleJwtStr(jwtStr, request); // 处理：jwtStr

        JWT jwt;

        try {

            jwt = JWT.of(jwtStr);

        } catch (Exception e) {

            MyExceptionUtil.printError(e);

            return null;

        }

        // 获取：userId的值
        Long userId = MyJwtUtil.getPayloadMapUserIdValue(jwt.getPayload().getClaimsJson());

        if (userId == null) {
            return null;
        }

        String redisJwt = MyJwtUtil.generateRedisJwt(jwtStr, userId, RequestUtil.getRequestCategoryEnum(request));

        // 判断 jwtHash是否存在于 redis中，如果存在，则表示能使用
        if (StrUtil.isBlank(redisJwt)) {
            return loginExpired(response, userId, request); // 提示登录过期，请重新登录
        }

        // 设置：jwt的密钥
        if (setJwtKey(jwt, userId)) {
            return null;
        }

        // 验证算法
        if (jwt.verify() == false) {
            return loginExpired(response, userId, request); // 提示登录过期，请重新登录，目的：为了可以随时修改配置的 jwt前缀
        }

        try {

            // 校验时间字段：如果过期了，这里会抛出 ValidateException异常
            JWTValidator.of(jwt).validateDate(new Date());

        } catch (ValidateException e) {

            return loginExpired(response, userId, request); // 提示登录过期，请重新登录

        }

        // 执行：额外的，检查 jwt的方法
        if (CollUtil.isNotEmpty(iJwtValidatorConfigurationList)) {

            for (IJwtValidatorConfiguration item : iJwtValidatorConfigurationList) {

                boolean validFlag = item.validator(jwt, request.getRequestURI(), response);

                if (BooleanUtil.isFalse(validFlag)) {

                    R.error(TempBizCodeEnum.LOGIN_EXPIRED, userId);

                }

            }

        }

        if (iJwtConfiguration == null) {

            // 返回：无权限
            return new UsernamePasswordAuthenticationToken(jwt.getPayload().getClaimsJson(), null);

        } else {

            // 返回：有权限
            return new UsernamePasswordAuthenticationToken(jwt.getPayload().getClaimsJson(), null,
                iJwtConfiguration.getSimpleGrantedAuthorityListByUserId(userId));

        }

    }

    /**
     * 处理：jwtStr
     */
    @Nullable
    private String handleJwtStr(String jwtStr, HttpServletRequest request) {

        // 如果不是正式环境：Authorization Bearer 0
        if (BooleanUtil.isFalse(TempConfiguration.prodFlag())) {

            if (iJwtConfiguration != null) {

                if (NumberUtil.isNumber(jwtStr)) {

                    SignInVO signInVO = iJwtConfiguration.generateJwt(Convert.toLong(jwtStr), null, false,
                        RequestUtil.getRequestCategoryEnum(request));

                    String jwtStrTmp = signInVO.getJwt();

                    log.info("jwtStrTmp：{}", jwtStrTmp);

                    jwtStr = MyJwtUtil.getJwtStrByHeadAuthorization(jwtStrTmp);

                }

            }

        }

        return jwtStr;

    }

    /**
     * 设置：jwt的密钥
     */
    private boolean setJwtKey(JWT jwt, Long userId) {

        if (UserUtil.getCurrentUserAdminFlag(userId)) {

            if (BooleanUtil.isFalse(securityProperties.getAdminEnable())) {
                return true;
            }

        }

        jwt.setKey(MyJwtUtil.getJwtSecret().getBytes());

        return false;

    }

    /**
     * 提示登录过期，请重新登录 备注：这里抛出异常不会进入：ExceptionAdvice
     */
    public static UsernamePasswordAuthenticationToken loginExpired(HttpServletResponse response, Long userId,
        HttpServletRequest request) {

        ResponseUtil.out(response, TempBizCodeEnum.LOGIN_EXPIRED);

        R.error(TempBizCodeEnum.LOGIN_EXPIRED, userId);

        return null;

    }

}
