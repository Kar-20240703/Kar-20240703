package com.kar20240703.be.base.web.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kar20240703.be.temp.web.model.domain.TempUserInfoDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BaseUserInfoMapper extends BaseMapper<TempUserInfoDO> {

}
