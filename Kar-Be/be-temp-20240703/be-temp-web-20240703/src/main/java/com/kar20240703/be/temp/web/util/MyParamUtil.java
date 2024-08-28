package com.kar20240703.be.temp.web.util;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.extension.toolkit.ChainWrappers;
import com.kar20240703.be.temp.web.mapper.TempParamMapper;
import com.kar20240703.be.temp.web.model.constant.ParamConstant;
import com.kar20240703.be.temp.web.model.domain.TempEntityNoId;
import com.kar20240703.be.temp.web.model.domain.TempParamDO;
import java.util.HashSet;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Component;

/**
 * 系统参数 工具类
 */
@Component
@Slf4j
public class MyParamUtil {

    // 系统内置参数 uuidSet，备注：不允许删除
    // 备注：系统内置参数的 uuid等于 id
    public static final Set<String> SYSTEM_PARAM_UUID_SET =
        CollUtil.newHashSet(ParamConstant.RSA_PRIVATE_KEY_UUID, ParamConstant.IP_REQUESTS_PER_SECOND_UUID);

    // 不允许删除的：参数主键 id
    public static final Set<String> SYSTEM_PARAM_NOT_DELETE_ID_SET =
        (Set<String>)CollUtil.addAll(new HashSet<>(SYSTEM_PARAM_UUID_SET),
            CollUtil.newHashSet(ParamConstant.DEFAULT_MANAGE_SIGN_IN_FLAG));

    private static TempParamMapper tempParamMapper;

    public MyParamUtil(TempParamMapper tempParamMapper) {
        MyParamUtil.tempParamMapper = tempParamMapper;
    }

    /**
     * 通过：参数的 uuid，获取 value，没有 value则返回 null 备注：请不要直接传字符串，请在：ParamConstant 类里面加一个常量
     */
    @Nullable
    public static String getValueByUuid(String paramUuid) {

        TempParamDO tempParamDO = ChainWrappers.lambdaQueryChain(tempParamMapper).select(TempParamDO::getValue)
            .eq(TempParamDO::getUuid, paramUuid).eq(TempEntityNoId::getEnableFlag, true).one();

        if (tempParamDO == null) {
            return null;
        }

        return tempParamDO.getValue();

    }

}
