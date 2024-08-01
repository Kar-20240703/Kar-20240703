package com.kar20240703.be.temp.web.configuration.security;

import com.kar20240703.be.temp.web.exception.ExceptionAdvice;
import com.kar20240703.be.temp.web.exception.TempBizCodeEnum;
import com.kar20240703.be.temp.web.util.ResponseUtil;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

/**
 * 未登录异常
 */
public class MyAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
        AuthenticationException authException) {

        // 尚未登录，请先登录
        ResponseUtil.out(response, TempBizCodeEnum.NOT_LOGGED_IN_YET);

        ExceptionAdvice.handleRequest(request, null, TempBizCodeEnum.NOT_LOGGED_IN_YET.getMsg(), "");

    }

}
