package com.kar20240703.be.base.web.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kar20240703.be.base.web.model.domain.BaseMenuDO;
import com.kar20240703.be.base.web.model.dto.BaseMenuInsertOrUpdateDTO;
import com.kar20240703.be.base.web.model.dto.BaseMenuPageDTO;
import com.kar20240703.be.temp.web.model.dto.ChangeNumberDTO;
import com.kar20240703.be.temp.web.model.dto.NotEmptyIdSet;
import com.kar20240703.be.temp.web.model.dto.NotNullId;
import java.util.List;

public interface BaseMenuService extends IService<BaseMenuDO> {

    String insertOrUpdate(BaseMenuInsertOrUpdateDTO dto);

    Page<BaseMenuDO> myPage(BaseMenuPageDTO dto);

    List<BaseMenuDO> tree(BaseMenuPageDTO dto);

    BaseMenuDO infoById(NotNullId notNullId);

    String deleteByIdSet(NotEmptyIdSet notEmptyIdSet, boolean checkChildrenFlag, boolean checkDeleteFlag);

    List<BaseMenuDO> userSelfMenuList();

    String addOrderNo(ChangeNumberDTO dto);

}
