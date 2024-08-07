package com.kar20240703.be.auth.web.model.vo;

import cn.hutool.core.util.StrUtil;
import com.kar20240703.be.auth.web.configuration.base.AuthConfiguration;
import com.kar20240703.be.auth.web.exception.AuthBizCodeEnum;
import com.kar20240703.be.auth.web.exception.AuthException;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

@Data
@Schema(description = "统一响应实体类")
public class R<T> {

    @Schema(description = "响应代码，成功返回：200")
    private Integer code;

    @Schema(description = "响应描述")
    private String msg;

    @Schema(description = "服务器是否收到请求，只会返回 true")
    private Boolean successFlag;

    @Schema(description = "数据")
    private T data;

    @Schema(description = "服务名")
    private String service = AuthConfiguration.applicationName;

    private R(Integer code, String msg, @Nullable T data) {

        this.msg = msg;
        this.code = code;
        this.data = data;
        this.successFlag = true;

    }

    private void setSuccessFlag(boolean successFlag) {
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
    public static <T> R<T> error(AuthBizCodeEnum authBizCodeEnum) {
        return new R<T>(authBizCodeEnum.getCode(), authBizCodeEnum.getMsg(), null).error();
    }

    public static <T> R<T> errorOrigin(AuthBizCodeEnum authBizCodeEnum) {
        return new R<>(authBizCodeEnum.getCode(), authBizCodeEnum.getMsg(), null);
    }

    @Contract("_,_ -> fail")
    public static <T> R<T> error(AuthBizCodeEnum authBizCodeEnum, @Nullable T data) {
        return errorOrigin(authBizCodeEnum, data).error();
    }

    public static <T> R<T> errorOrigin(AuthBizCodeEnum authBizCodeEnum, @Nullable T data) {
        return new R<>(authBizCodeEnum.getCode(), authBizCodeEnum.getMsg(), data);
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
