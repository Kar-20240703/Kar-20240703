package com.kar20240703.be.base.web.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kar20240703.be.base.web.mapper.BaseAuthMapper;
import com.kar20240703.be.base.web.model.domain.BaseAuthDO;
import com.kar20240703.be.base.web.service.BaseAuthService;
import org.springframework.stereotype.Service;

@Service
public class BaseAuthServiceImpl extends ServiceImpl<BaseAuthMapper, BaseAuthDO> implements BaseAuthService {

}
