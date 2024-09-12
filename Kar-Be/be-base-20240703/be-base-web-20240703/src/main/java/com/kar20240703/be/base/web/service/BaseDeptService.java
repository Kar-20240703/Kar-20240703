package com.kar20240703.be.base.web.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kar20240703.be.base.web.model.domain.BaseDeptDO;
import com.kar20240703.be.base.web.model.dto.BaseDeptInsertOrUpdateDTO;
import com.kar20240703.be.base.web.model.dto.BaseDeptPageDTO;
import com.kar20240703.be.base.web.model.vo.BaseDeptInfoByIdVO;
import com.kar20240703.be.temp.web.model.dto.ChangeNumberDTO;
import com.kar20240703.be.temp.web.model.dto.NotEmptyIdSet;
import com.kar20240703.be.temp.web.model.dto.NotNullId;
import java.util.List;

public interface BaseDeptService extends IService<BaseDeptDO> {

    String insertOrUpdate(BaseDeptInsertOrUpdateDTO dto);

    Page<BaseDeptDO> myPage(BaseDeptPageDTO dto);

    List<BaseDeptDO> tree(BaseDeptPageDTO dto);

    List<BaseDeptDO> dictTreeList();

    BaseDeptInfoByIdVO infoById(NotNullId notNullId);

    String deleteByIdSet(NotEmptyIdSet notEmptyIdSet);

    String addOrderNo(ChangeNumberDTO dto);

    String updateOrderNo(ChangeNumberDTO dto);

}
