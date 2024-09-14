package com.kar20240703.be.base.web.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kar20240703.be.base.web.model.dto.BaseParamInsertOrUpdateDTO;
import com.kar20240703.be.base.web.model.dto.BaseParamPageDTO;
import com.kar20240703.be.temp.web.model.domain.TempParamDO;
import com.kar20240703.be.temp.web.model.dto.NotEmptyIdSet;
import com.kar20240703.be.temp.web.model.dto.NotNullId;

public interface BaseParamService extends IService<TempParamDO> {

    String insertOrUpdate(BaseParamInsertOrUpdateDTO dto);

    Page<TempParamDO> myPage(BaseParamPageDTO dto);

    TempParamDO infoById(NotNullId notNullId);

    String deleteByIdSet(NotEmptyIdSet notEmptyIdSet);

}
