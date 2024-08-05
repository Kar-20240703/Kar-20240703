package com.kar20240703.be.auth.web.filter;

import cn.hutool.core.exceptions.ValidateException;
import cn.hutool.core.util.StrUtil;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTValidator;
import com.kar20240703.be.auth.web.exception.AuthBizCodeEnum;
import com.kar20240703.be.auth.web.model.enums.AuthRedisKeyEnum;
import com.kar20240703.be.auth.web.util.MyJwtUtil;
import com.kar20240703.be.auth.web.util.RequestUtil;
import com.kar20240703.be.auth.web.util.ResponseUtil;
import java.util.Date;
import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;
import org.redisson.api.RedissonClient;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    @Resource
    RedissonClient redissonClient;

    @SneakyThrows
    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response,
        @NotNull FilterChain filterChain) {

        String jwtStr = MyJwtUtil.getJwtStrByRequest(request);

        if (StrUtil.isBlank(jwtStr)) {

            ResponseUtil.out(response, AuthBizCodeEnum.LOGIN_EXPIRED);
            return;

        }

        JWT jwt;

        try {

            jwt = JWT.of(jwtStr);

        } catch (Exception e) {

            ResponseUtil.out(response, AuthBizCodeEnum.LOGIN_EXPIRED);
            return;

        }

        jwt.setKey(MyJwtUtil.getJwtSecret().getBytes());

        // 验证算法
        if (jwt.verify() == false) {

            ResponseUtil.out(response, AuthBizCodeEnum.LOGIN_EXPIRED);
            return;

        }

        try {

            // 校验时间字段：如果过期了，这里会抛出 ValidateException异常
            JWTValidator.of(jwt).validateDate(new Date());

        } catch (ValidateException e) {

            ResponseUtil.out(response, AuthBizCodeEnum.LOGIN_EXPIRED);
            return;

        }

        // 获取：userId的值
        Long userId = MyJwtUtil.getPayloadMapUserIdValue(jwt.getPayload().getClaimsJson());

        if (userId == null) {

            ResponseUtil.out(response, AuthBizCodeEnum.LOGIN_EXPIRED);
            return;

        }

        String redisJwt = MyJwtUtil.generateRedisJwt(jwtStr, userId, RequestUtil.getRequestCategoryEnum(request));

        boolean exists = redissonClient.getBucket(redisJwt).isExists();

        if (exists == false) {

            ResponseUtil.out(response, AuthBizCodeEnum.LOGIN_EXPIRED);
            return;

        }

        boolean disableFlag =
            redissonClient.getBucket(AuthRedisKeyEnum.PRE_USER_DISABLE.name() + ":" + userId).isExists();

        if (disableFlag) {

            ResponseUtil.out(response, AuthBizCodeEnum.ACCOUNT_IS_DISABLED);
            return;

        }

        SecurityContextHolder.getContext()
            .setAuthentication(new UsernamePasswordAuthenticationToken(jwt.getPayload().getClaimsJson(), null));

        filterChain.doFilter(request, response);

    }

}
