package com.kar20240703.be.base.web.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kar20240703.be.base.web.mapper.BaseAuthRefUserMapper;
import com.kar20240703.be.base.web.model.domain.BaseAuthRefUserDO;
import com.kar20240703.be.base.web.service.BaseAuthRefUserService;
import org.springframework.stereotype.Service;

@Service
public class BaseAuthRefUserServiceImpl extends ServiceImpl<BaseAuthRefUserMapper, BaseAuthRefUserDO>
    implements BaseAuthRefUserService {

}
