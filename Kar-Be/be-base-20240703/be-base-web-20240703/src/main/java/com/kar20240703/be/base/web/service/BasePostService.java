package com.kar20240703.be.base.web.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kar20240703.be.base.web.model.domain.BasePostDO;
import com.kar20240703.be.base.web.model.dto.BasePostInsertOrUpdateDTO;
import com.kar20240703.be.base.web.model.dto.BasePostPageDTO;
import com.kar20240703.be.base.web.model.vo.BasePostInfoByIdVO;
import com.kar20240703.be.temp.web.model.dto.ChangeNumberDTO;
import com.kar20240703.be.temp.web.model.dto.NotEmptyIdSet;
import com.kar20240703.be.temp.web.model.dto.NotNullId;
import java.util.List;

public interface BasePostService extends IService<BasePostDO> {

    String insertOrUpdate(BasePostInsertOrUpdateDTO dto);

    Page<BasePostDO> myPage(BasePostPageDTO dto);

    List<BasePostDO> tree(BasePostPageDTO dto);

    List<BasePostDO> dictTreeList();

    BasePostInfoByIdVO infoById(NotNullId notNullId);

    String deleteByIdSet(NotEmptyIdSet notEmptyIdSet);

    String addOrderNo(ChangeNumberDTO dto);

    String updateOrderNo(ChangeNumberDTO dto);

}
