package com.kar20240703.be.base.web.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kar20240703.be.base.web.mapper.BaseUserMapper;
import com.kar20240703.be.base.web.service.BaseUserService;
import com.kar20240703.be.temp.web.model.domain.TempUserDO;
import org.springframework.stereotype.Service;

@Service
public class BaseUserServiceImpl extends ServiceImpl<BaseUserMapper, TempUserDO> implements BaseUserService {

}
