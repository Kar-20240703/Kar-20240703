package com.kar20240703.be.temp.web.util;

import com.baomidou.mybatisplus.extension.toolkit.ChainWrappers;
import com.kar20240703.be.temp.web.mapper.TempDictMapper;
import com.kar20240703.be.temp.web.model.domain.TempDictDO;
import com.kar20240703.be.temp.web.model.domain.TempEntityNoId;
import com.kar20240703.be.temp.web.model.enums.TempDictTypeEnum;
import com.kar20240703.be.temp.web.model.vo.DictIntegerVO;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 系统字典 工具类
 */
@Component
@Slf4j
public class MyDictUtil {

    private static TempDictMapper tempDictMapper;

    @Resource
    public void setSysDictMapper(TempDictMapper tempDictMapper) {
        MyDictUtil.tempDictMapper = tempDictMapper;
    }

    /**
     * 通过：dictKey获取字典项集合
     */
    public static List<DictIntegerVO> listByDictKey(String dictKey) {

        return ChainWrappers.lambdaQueryChain(tempDictMapper).eq(TempDictDO::getType, TempDictTypeEnum.DICT_ITEM)
            .eq(TempEntityNoId::getEnableFlag, true) //
            .eq(TempDictDO::getDictKey, dictKey).select(TempDictDO::getValue, TempDictDO::getName) //
            .orderByDesc(TempDictDO::getOrderNo).list() //
            .stream().map(it -> new DictIntegerVO(it.getValue(), it.getName())).collect(Collectors.toList());

    }

}
