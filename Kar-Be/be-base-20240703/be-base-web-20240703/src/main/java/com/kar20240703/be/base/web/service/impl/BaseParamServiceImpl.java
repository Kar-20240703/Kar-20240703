package com.kar20240703.be.base.web.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kar20240703.be.base.web.service.BaseParamService;
import com.kar20240703.be.temp.web.mapper.TempParamMapper;
import com.kar20240703.be.temp.web.model.domain.TempParamDO;
import org.springframework.stereotype.Service;

@Service
public class BaseParamServiceImpl extends ServiceImpl<TempParamMapper, TempParamDO> implements BaseParamService {
}
