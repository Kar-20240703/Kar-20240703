package com.kar20240703.be.base.web.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kar20240703.be.base.web.mapper.BasePostRefUserMapper;
import com.kar20240703.be.base.web.model.domain.BasePostRefUser;
import com.kar20240703.be.base.web.service.BasePostRefUserService;
import org.springframework.stereotype.Service;

@Service
public class BasePostRefUserServiceImpl extends ServiceImpl<BasePostRefUserMapper, BasePostRefUser>
    implements BasePostRefUserService {

}
