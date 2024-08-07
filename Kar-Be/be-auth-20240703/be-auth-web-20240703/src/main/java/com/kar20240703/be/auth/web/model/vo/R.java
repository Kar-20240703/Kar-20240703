package com.kar20240703.be.auth.web.model.vo;

import cn.hutool.core.util.StrUtil;
import com.kar20240703.be.auth.web.configuration.base.AuthConfiguration;
import com.kar20240703.be.auth.web.exception.AuthBizCodeEnum;
import com.kar20240703.be.auth.web.exception.AuthException;
import com.kar20240703.be.auth.web.model.interfaces.IBizCode;
import lombok.Data;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

/**
 * 统一响应实体类
 */
@Data
public class R<T> {

    /**
     * 响应代码，成功返回：200
     */
    private Integer code;

    /**
     * 响应描述
     */
    private String msg;

    /**
     * 服务器是否收到请求，只会返回 true
     */
    private Boolean receive;

    /**
     * 数据
     */
    private T data;

    /**
     * 服务名
     */
    private String service = AuthConfiguration.applicationName;

    private R(Integer code, String msg, @Nullable T data) {

        this.msg = msg;
        this.code = code;
        this.data = data;
        this.receive = true;

    }

    private void setReceive(boolean receive) {
        // 不允许修改 success的值
    }

    private void setService(String service) {
        // 不允许修改 service的值
    }

    /**
     * Contract注解，目的：让 IDEA知道这里会抛出异常
     */
    @Contract(" -> fail")
    public R<T> error() {
        throw new AuthException(this);
    }

    /**
     * 系统异常
     */
    public static void sysError() {
        error(AuthBizCodeEnum.RESULT_SYS_ERROR);
    }

    /**
     * 操作失败
     */
    @Contract("_ -> fail")
    public static <T> R<T> error(IBizCode iBizCode) {
        return new R<T>(iBizCode.getCode(), iBizCode.getMsg(), null).error();
    }

    public static <T> R<T> errorOrigin(IBizCode iBizCode) {
        return new R<>(iBizCode.getCode(), iBizCode.getMsg(), null);
    }

    @Contract("_,_ -> fail")
    public static <T> R<T> error(IBizCode iBizCode, @Nullable T data) {
        return errorOrigin(iBizCode, data).error();
    }

    public static <T> R<T> errorOrigin(IBizCode iBizCode, @Nullable T data) {
        return new R<>(iBizCode.getCode(), iBizCode.getMsg(), data);
    }

    @Contract("_,_ -> fail")
    public static <T> R<T> error(String msg, @Nullable T data) {
        return new R<>(AuthBizCodeEnum.RESULT_SYS_ERROR.getCode(), msg, data).error();
    }

    @Contract("_ -> fail")
    public static <T> R<T> errorData(@Nullable T data) {
        return new R<>(AuthBizCodeEnum.RESULT_SYS_ERROR.getCode(), AuthBizCodeEnum.RESULT_SYS_ERROR.getMsg(),
            data).error();
    }

    @Contract("_,_ -> fail")
    public static <T> R<T> errorMsg(String msgTemp, Object... paramArr) {
        return (R<T>)errorMsgOrigin(msgTemp, paramArr).error();
    }

    public static <T> R<T> errorMsgOrigin(String msgTemp, Object... paramArr) {
        return new R<>(AuthBizCodeEnum.RESULT_SYS_ERROR.getCode(), StrUtil.format(msgTemp, paramArr), null);
    }

    /**
     * 操作成功
     */
    public static <T> R<T> ok(String msg, @Nullable T data) {
        return new R<>(AuthBizCodeEnum.RESULT_OK.getCode(), msg, data);
    }

    public static <T> R<T> okData(@Nullable T data) {
        return new R<>(AuthBizCodeEnum.RESULT_OK.getCode(), AuthBizCodeEnum.RESULT_OK.getMsg(), data);
    }

    public static <T> R<T> okMsg(String msg) {
        return new R<>(AuthBizCodeEnum.RESULT_OK.getCode(), msg, null);
    }

}
