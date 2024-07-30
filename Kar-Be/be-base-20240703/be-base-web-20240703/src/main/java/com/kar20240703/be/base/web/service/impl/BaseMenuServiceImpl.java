package com.kar20240703.be.base.web.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kar20240703.be.base.web.mapper.BaseMenuMapper;
import com.kar20240703.be.base.web.model.domain.BaseMenuDO;
import com.kar20240703.be.base.web.service.BaseMenuService;
import org.springframework.stereotype.Service;

@Service
public class BaseMenuServiceImpl extends ServiceImpl<BaseMenuMapper, BaseMenuDO> implements BaseMenuService {

}
