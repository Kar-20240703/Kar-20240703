package com.kar20240703.be.base.web.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kar20240703.be.base.web.mapper.BaseRoleUserMapper;
import com.kar20240703.be.base.web.model.domain.BaseRoleUserDO;
import com.kar20240703.be.base.web.service.BaseRoleUserService;
import org.springframework.stereotype.Service;

@Service
public class BaseRoleUserServiceImpl extends ServiceImpl<BaseRoleUserMapper, BaseRoleUserDO>
    implements BaseRoleUserService {

}
