package com.kar20240703.be.base.web.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kar20240703.be.base.web.model.domain.BaseRoleDO;
import com.kar20240703.be.base.web.model.dto.BaseRoleInsertOrUpdateDTO;
import com.kar20240703.be.base.web.model.dto.BaseRolePageDTO;
import com.kar20240703.be.base.web.model.vo.BaseRoleInfoByIdVO;
import com.kar20240703.be.temp.web.model.dto.NotEmptyIdSet;
import com.kar20240703.be.temp.web.model.dto.NotNullId;
import com.kar20240703.be.temp.web.model.vo.DictVO;

public interface BaseRoleService extends IService<BaseRoleDO> {

    String insertOrUpdate(BaseRoleInsertOrUpdateDTO dto);

    Page<BaseRoleDO> myPage(BaseRolePageDTO dto);

    Page<DictVO> dictList();

    BaseRoleInfoByIdVO infoById(NotNullId notNullId);

    String deleteByIdSet(NotEmptyIdSet notEmptyIdSet);

}
