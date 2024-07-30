package com.kar20240703.be.base.web.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kar20240703.be.base.web.mapper.BaseRoleAuthMapper;
import com.kar20240703.be.base.web.model.domain.BaseRoleAuthDO;
import com.kar20240703.be.base.web.service.BaseRoleAuthService;
import org.springframework.stereotype.Service;

@Service
public class BaseRoleAuthServiceImpl extends ServiceImpl<BaseRoleAuthMapper, BaseRoleAuthDO>
    implements BaseRoleAuthService {

}
