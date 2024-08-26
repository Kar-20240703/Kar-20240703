package com.kar20240703.be.base.web.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kar20240703.be.base.web.model.domain.BaseAuthDO;
import com.kar20240703.be.base.web.model.dto.BaseAuthInsertOrUpdateDTO;
import com.kar20240703.be.base.web.model.dto.BaseAuthPageDTO;
import com.kar20240703.be.base.web.model.vo.BaseAuthInfoByIdVO;
import com.kar20240703.be.temp.web.model.dto.NotEmptyIdSet;
import com.kar20240703.be.temp.web.model.dto.NotNullId;

public interface BaseAuthService extends IService<BaseAuthDO> {

    String insertOrUpdate(BaseAuthInsertOrUpdateDTO dto);

    Page<BaseAuthDO> myPage(BaseAuthPageDTO dto);

    BaseAuthInfoByIdVO infoById(NotNullId notNullId);

    String deleteByIdSet(NotEmptyIdSet notEmptyIdSet);

}
