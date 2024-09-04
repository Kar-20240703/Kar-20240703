package com.kar20240703.be.base.web.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kar20240703.be.base.web.model.domain.BasePostDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BasePostMapper extends BaseMapper<BasePostDO> {

}
