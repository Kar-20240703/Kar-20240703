package com.kar20240703.be.temp.web.util;

import cn.hutool.core.convert.Convert;
import cn.hutool.extra.servlet.ServletUtil;
import com.kar20240703.be.temp.web.model.constant.SecurityConstant;
import com.kar20240703.be.temp.web.model.enums.TempRequestCategoryEnum;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
@Slf4j
public class RequestUtil {

    public static final String[] IP_HEADER_ARR =
        {"X-Forwarded-For", "X-Real-IP", "Proxy-Client-IP", "WL-Proxy-Client-IP", "HTTP_CLIENT_IP",
            "HTTP_X_FORWARDED_FOR"};

    /**
     * 获取当前上下文的 request对象
     */
    @Nullable
    public static HttpServletRequest getRequest() {

        ServletRequestAttributes requestAttributes =
            (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();

        if (requestAttributes == null) {
            return null;
        }

        return requestAttributes.getRequest();

    }

    /**
     * 获取请求类别
     */
    @NotNull
    public static TempRequestCategoryEnum getRequestCategoryEnum() {

        return getRequestCategoryEnum(getRequest());

    }

    /**
     * 获取请求类别
     */
    @NotNull
    public static TempRequestCategoryEnum getRequestCategoryEnum(@Nullable HttpServletRequest httpServletRequest) {

        if (httpServletRequest == null) {
            return TempRequestCategoryEnum.PC_BROWSER_WINDOWS;
        }

        return TempRequestCategoryEnum.getByCode(
            Convert.toInt(httpServletRequest.getHeader(SecurityConstant.REQUEST_HEADER_CATEGORY)));

    }

    /**
     * 获取：ip
     */
    @NotNull
    public static String getIp() {

        HttpServletRequest httpServletRequest = getRequest();

        if (httpServletRequest == null) {
            return "";
        }

        return ServletUtil.getClientIP(httpServletRequest);

    }

}
