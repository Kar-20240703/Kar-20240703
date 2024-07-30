package com.kar20240703.be.temp.web.model.vo;

import cn.hutool.core.util.StrUtil;
import com.kar20240703.be.temp.web.configuration.base.TempConfiguration;
import com.kar20240703.be.temp.web.exception.TempBizCodeEnum;
import com.kar20240703.be.temp.web.exception.TempException;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.jetbrains.annotations.Contract;

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
    private String service = TempConfiguration.applicationName;

    private R(Integer code, String msg, T data) {

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
        throw new TempException(this);
    }

    /**
     * 系统异常
     */
    public static void sysError() {
        error(TempBizCodeEnum.RESULT_SYS_ERROR);
    }

    /**
     * 操作失败
     */
    @Contract("_ -> fail")
    public static <T> R<T> error(TempBizCodeEnum tempBizCodeEnum) {
        return new R<T>(tempBizCodeEnum.getCode(), tempBizCodeEnum.getMsg(), null).error();
    }

    @Contract("_,_ -> fail")
    public static <T> R<T> error(TempBizCodeEnum tempBizCodeEnum, T data) {
        return new R<>(tempBizCodeEnum.getCode(), tempBizCodeEnum.getMsg(), data).error();
    }

    @Contract("_,_ -> fail")
    public static <T> R<T> error(String msg, T data) {
        return new R<>(TempBizCodeEnum.RESULT_SYS_ERROR.getCode(), msg, data).error();
    }

    @Contract("_ -> fail")
    public static <T> R<T> errorData(T data) {
        return new R<>(TempBizCodeEnum.RESULT_SYS_ERROR.getCode(), TempBizCodeEnum.RESULT_SYS_ERROR.getMsg(),
            data).error();
    }

    @Contract("_,_ -> fail")
    public static <T> R<T> errorMsg(String msgTemp, Object... paramArr) {
        return new R<T>(TempBizCodeEnum.RESULT_SYS_ERROR.getCode(), StrUtil.format(msgTemp, paramArr), null).error();
    }

    /**
     * 操作成功
     */
    public static <T> R<T> ok(String msg, T data) {
        return new R<>(TempBizCodeEnum.RESULT_OK.getCode(), msg, data);
    }

    public static <T> R<T> okData(T data) {
        return new R<>(TempBizCodeEnum.RESULT_OK.getCode(), TempBizCodeEnum.RESULT_OK.getMsg(), data);
    }

    public static <T> R<T> okMsg(String msg) {
        return new R<>(TempBizCodeEnum.RESULT_OK.getCode(), msg, null);
    }

}
