package com.kar20240703.be.base.web.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kar20240703.be.base.web.mapper.BaseUserAuthMapper;
import com.kar20240703.be.base.web.model.domain.BaseUserAuthDO;
import com.kar20240703.be.base.web.service.BaseUserAuthService;
import org.springframework.stereotype.Service;

@Service
public class BaseUserAuthServiceImpl extends ServiceImpl<BaseUserAuthMapper, BaseUserAuthDO>
    implements BaseUserAuthService {

}
