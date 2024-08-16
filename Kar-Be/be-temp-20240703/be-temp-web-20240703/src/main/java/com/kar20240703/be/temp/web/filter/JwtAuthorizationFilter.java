package com.kar20240703.be.temp.web.filter;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONUtil;
import cn.hutool.jwt.JWT;
import com.kar20240703.be.temp.web.configuration.base.TempConfiguration;
import com.kar20240703.be.temp.web.configuration.security.SecurityConfiguration;
import com.kar20240703.be.temp.web.exception.TempBizCodeEnum;
import com.kar20240703.be.temp.web.model.configuration.IJwtGenerateConfiguration;
import com.kar20240703.be.temp.web.model.configuration.IJwtGetAuthListConfiguration;
import com.kar20240703.be.temp.web.model.constant.SecurityConstant;
import com.kar20240703.be.temp.web.model.vo.R;
import com.kar20240703.be.temp.web.model.vo.SignInVO;
import com.kar20240703.be.temp.web.properties.MySecurityProperties;
import com.kar20240703.be.temp.web.util.MyExceptionUtil;
import com.kar20240703.be.temp.web.util.MyJwtUtil;
import com.kar20240703.be.temp.web.util.RequestUtil;
import com.kar20240703.be.temp.web.util.ResponseUtil;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * 自定义 jwt过滤器，备注：后续接口方法，无需判断账号是否封禁或者不存在
 */
@Slf4j
@Component
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    @Nullable
    IJwtGenerateConfiguration iJwtGenerateConfiguration;

    MySecurityProperties mySecurityProperties;

    @Nullable
    IJwtGetAuthListConfiguration iJwtGetAuthListConfiguration;

    public JwtAuthorizationFilter(
        @Autowired(required = false) @Nullable IJwtGenerateConfiguration iJwtGenerateConfiguration,
        MySecurityProperties mySecurityProperties,
        @Autowired(required = false) @Nullable IJwtGetAuthListConfiguration iJwtGetAuthListConfiguration) {

        this.iJwtGenerateConfiguration = iJwtGenerateConfiguration;
        this.mySecurityProperties = mySecurityProperties;
        this.iJwtGetAuthListConfiguration = iJwtGetAuthListConfiguration;

    }

    @SneakyThrows
    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response,
        @NotNull FilterChain filterChain) {

        if (SecurityConfiguration.permitAllCheck(request)) {

            filterChain.doFilter(request, response);
            return;

        }

        // 从请求头里，获取：jwt字符串，备注：就算加了不需要登录就可以访问，但是也会走该方法
        String jwtStr = MyJwtUtil.getJwtStrByRequest(request);

        if (jwtStr == null) {

            filterChain.doFilter(request, response);
            return;

        }

        jwtStr = handleJwtStr(jwtStr, request); // 处理：jwtStr

        JWT jwt;

        try {

            jwt = JWT.of(jwtStr);

        } catch (Exception e) {

            MyExceptionUtil.printError(e);
            ResponseUtil.out(response, TempBizCodeEnum.LOGIN_EXPIRED);
            return;

        }

        // 获取：userId的值
        Long userId = MyJwtUtil.getPayloadMapUserIdValue(jwt.getPayload().getClaimsJson());

        if (userId == null) {

            ResponseUtil.out(response, TempBizCodeEnum.LOGIN_EXPIRED);
            return;

        }

        String jwtGetAuthListUrl = mySecurityProperties.getJwtGetAuthListUrl();

        if (StrUtil.isBlank(jwtGetAuthListUrl)) {

            filterChain.doFilter(request, response);
            return;

        }

        List<String> authList;

        if (iJwtGetAuthListConfiguration == null) {

            try {

                String body = HttpRequest.post(jwtGetAuthListUrl)
                    .header(SecurityConstant.AUTHORIZATION, SecurityConstant.JWT_PREFIX + jwtStr).execute().body();

                R<List<String>> r = JSONUtil.toBean(body, R.class);

                if (TempBizCodeEnum.RESULT_OK.getCode() != r.getCode()) {

                    ResponseUtil.out(response, body, false);
                    return;

                }

                authList = r.getData();

            } catch (Exception e) {

                MyExceptionUtil.printError(e);
                ResponseUtil.out(response, TempBizCodeEnum.LOGIN_EXPIRED);
                return;

            }

        } else {

            authList = iJwtGetAuthListConfiguration.getAuthList(jwtStr, jwt, response);

        }

        List<GrantedAuthority> authoritieList =
            authList.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());

        SecurityContextHolder.getContext().setAuthentication(
            new UsernamePasswordAuthenticationToken(jwt.getPayload().getClaimsJson(), null, authoritieList));

        filterChain.doFilter(request, response);

    }

    /**
     * 处理：jwtStr
     */
    @Nullable
    private String handleJwtStr(String jwtStr, HttpServletRequest request) {

        // 如果不是正式环境：Authorization Bearer 0
        if (TempConfiguration.prodFlag() == false) {

            if (iJwtGenerateConfiguration != null) {

                if (NumberUtil.isNumber(jwtStr)) {

                    SignInVO signInVO = iJwtGenerateConfiguration.generateJwt(Convert.toLong(jwtStr), null, false,
                        RequestUtil.getRequestCategoryEnum(request));

                    if (signInVO != null) {

                        String jwtStrTmp = signInVO.getJwt();

                        log.info("jwtStrTmp：{}", jwtStrTmp);

                        jwtStr = MyJwtUtil.getJwtStrByHeadAuthorization(jwtStrTmp);

                    }

                }

            }

        }

        return jwtStr;

    }

}
