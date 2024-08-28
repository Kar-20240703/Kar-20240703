package com.kar20240703.be.base.web.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kar20240703.be.base.web.model.domain.BaseMenuDO;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.jetbrains.annotations.Nullable;

@Mapper
public interface BaseMenuMapper extends BaseMapper<BaseMenuDO> {

    List<BaseMenuDO> getMenuListByUserId(@Nullable @Param("userId") Long userId);

}
