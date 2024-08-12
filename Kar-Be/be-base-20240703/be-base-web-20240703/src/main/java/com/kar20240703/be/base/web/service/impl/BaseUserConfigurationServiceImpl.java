package com.kar20240703.be.base.web.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kar20240703.be.base.web.mapper.BaseSignConfigurationMapper;
import com.kar20240703.be.base.web.model.domain.BaseUserConfigurationDO;
import com.kar20240703.be.base.web.service.BaseUserConfigurationService;
import org.springframework.stereotype.Service;

@Service
public class BaseUserConfigurationServiceImpl extends ServiceImpl<BaseSignConfigurationMapper, BaseUserConfigurationDO>
    implements BaseUserConfigurationService {
}
