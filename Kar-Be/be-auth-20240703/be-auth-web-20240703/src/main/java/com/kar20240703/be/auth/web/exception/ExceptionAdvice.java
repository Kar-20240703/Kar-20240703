package com.kar20240703.be.auth.web.exception;

import cn.hutool.core.map.MapUtil;
import cn.hutool.json.JSONUtil;
import com.kar20240703.be.auth.web.model.vo.R;
import com.kar20240703.be.auth.web.util.MyEntityUtil;
import com.kar20240703.be.auth.web.util.MyExceptionUtil;
import io.swagger.v3.oas.annotations.Operation;
import java.lang.reflect.Method;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Nullable;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ExceptionAdvice {

    @Resource
    HttpServletRequest httpServletRequest;

    /**
     * 参数校验异常：@Valid
     */
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public R<?> handleValidException(MethodArgumentNotValidException e) {

        MyExceptionUtil.printError(e);

        // 返回详细的参数校验错误信息
        Map<String, String> map = MapUtil.newHashMap(e.getBindingResult().getFieldErrors().size());

        BindingResult bindingResult = e.getBindingResult();

        for (FieldError item : bindingResult.getFieldErrors()) {
            map.put(item.getField(), item.getDefaultMessage());
        }

        R<Map<String, String>> r = R.errorOrigin(AuthBizCodeEnum.PARAMETER_CHECK_ERROR, map);

        Method method = e.getParameter().getMethod();

        if (method != null) {

            // 处理：请求
            handleRequest(httpServletRequest, method.getAnnotation(Operation.class),
                MyEntityUtil.getNotNullStr(JSONUtil.toJsonStr(r)), //
                MyEntityUtil.getNotNullStr(JSONUtil.toJsonStr(e.getBindingResult().getTarget())));

        }

        return r;

    }

    /**
     * 处理：请求
     */
    public static void handleRequest(HttpServletRequest httpServletRequest, @Nullable Operation operation,
        String errorMsg, String requestParam) {

    }

    /**
     * 参数校验异常：断言
     */
    @ExceptionHandler(value = IllegalArgumentException.class)
    public R<?> handleIllegalArgumentException(IllegalArgumentException e) {

        MyExceptionUtil.printError(e);

        return R.errorMsgOrigin(e.getMessage());

    }

    /**
     * 参数校验异常：springframework
     */
    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    public R<?> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {

        MyExceptionUtil.printError(e);

        R<String> r = R.errorOrigin(AuthBizCodeEnum.PARAMETER_CHECK_ERROR, e.getMessage());

        // 处理：请求
        handleRequest(httpServletRequest, null, MyEntityUtil.getNotNullStr(JSONUtil.toJsonStr(r)), "");

        return r; // 这里不会执行，只是为了通过语法检查

    }

    /**
     * 自定义异常
     */
    @ExceptionHandler(value = AuthException.class)
    public R<?> handleAuthException(AuthException e) {

        MyExceptionUtil.printError(e);

        return e.getR();

    }

    /**
     * 权限不够时的异常处理
     */
    @ExceptionHandler(value = AccessDeniedException.class)
    public R<?> handleAccessDeniedException(AccessDeniedException e) {

        R<String> r = R.errorOrigin(AuthBizCodeEnum.INSUFFICIENT_PERMISSIONS);

        // 处理：请求
        handleRequest(httpServletRequest, null, MyEntityUtil.getNotNullStr(JSONUtil.toJsonStr(r)), "");

        return r; // 这里不会执行，只是为了通过语法检查

    }

    /**
     * 缺省异常处理，直接提示系统异常
     */
    @ExceptionHandler(value = Throwable.class)
    public R<?> handleThrowable(Throwable e) {

        MyExceptionUtil.printError(e);

        R<String> r = R.errorOrigin(AuthBizCodeEnum.RESULT_SYS_ERROR);

        // 处理：请求
        handleRequest(httpServletRequest, null, MyEntityUtil.getNotNullStr(e.getMessage()), "");

        return r; // 这里不会执行，只是为了通过语法检查

    }

}
