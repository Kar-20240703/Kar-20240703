package com.kar20240703.be.base.web.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kar20240703.be.base.web.mapper.BaseMenuRefUserMapper;
import com.kar20240703.be.base.web.model.domain.BaseMenuRefUserDO;
import com.kar20240703.be.base.web.service.BaseMenuRefUserService;
import org.springframework.stereotype.Service;

@Service
public class BaseMenuRefUserServiceImpl extends ServiceImpl<BaseMenuRefUserMapper, BaseMenuRefUserDO>
    implements BaseMenuRefUserService {

}
