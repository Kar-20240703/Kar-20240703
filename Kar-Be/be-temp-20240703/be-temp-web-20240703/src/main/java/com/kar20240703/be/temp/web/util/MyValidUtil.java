package com.kar20240703.be.temp.web.util;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.kar20240703.be.temp.web.exception.TempBizCodeEnum;
import com.kar20240703.be.temp.web.model.vo.R;
import java.util.Set;
import java.util.stream.Collectors;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

/**
 * 我的验证工具类
 */
public class MyValidUtil {

    /**
     * 执行：验证
     */
    public static <T> Set<ConstraintViolation<T>> valid(T t) {

        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

        return validator.validate(t);

    }

    /**
     * 执行：验证
     *
     * @return null 则表示验证成功
     */
    public static <T> String validReturnStr(T t) {

        Set<ConstraintViolation<T>> errSet = valid(t);

        if (CollUtil.isEmpty(errSet)) {
            return null;
        }

        return errSet.stream().map(ConstraintViolation::getMessage).collect(Collectors.toList()).toString();

    }

    /**
     * 执行：验证
     */
    public static <T> void validWillError(T t) {

        String errMsg = validReturnStr(t);

        if (StrUtil.isBlank(errMsg)) {
            return;
        }

        R.error(TempBizCodeEnum.PARAMETER_CHECK_ERROR, errMsg);

    }

}
