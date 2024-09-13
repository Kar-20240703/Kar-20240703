package com.kar20240703.be.base.web.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kar20240703.be.base.web.service.BaseDictService;
import com.kar20240703.be.temp.web.mapper.TempDictMapper;
import com.kar20240703.be.temp.web.model.domain.TempDictDO;
import org.springframework.stereotype.Service;

@Service
public class BaseDictServiceImpl extends ServiceImpl<TempDictMapper, TempDictDO> implements BaseDictService {
}
