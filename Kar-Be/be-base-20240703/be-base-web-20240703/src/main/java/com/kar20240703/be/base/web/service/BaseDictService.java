package com.kar20240703.be.base.web.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kar20240703.be.base.web.model.dto.BaseDictInsertOrUpdateDTO;
import com.kar20240703.be.base.web.model.dto.BaseDictListByDictKeyDTO;
import com.kar20240703.be.base.web.model.dto.BaseDictPageDTO;
import com.kar20240703.be.temp.web.model.domain.TempDictDO;
import com.kar20240703.be.temp.web.model.dto.ChangeNumberDTO;
import com.kar20240703.be.temp.web.model.dto.NotEmptyIdSet;
import com.kar20240703.be.temp.web.model.dto.NotNullId;
import com.kar20240703.be.temp.web.model.vo.DictIntegerVO;
import java.util.List;

public interface BaseDictService extends IService<TempDictDO> {

    String insertOrUpdate(BaseDictInsertOrUpdateDTO dto);

    Page<TempDictDO> myPage(BaseDictPageDTO dto);

    List<DictIntegerVO> listByDictKey(BaseDictListByDictKeyDTO dto);

    List<TempDictDO> tree(BaseDictPageDTO dto);

    TempDictDO infoById(NotNullId notNullId);

    String deleteByIdSet(NotEmptyIdSet notEmptyIdSet, boolean checkDeleteFlag);

    String addOrderNo(ChangeNumberDTO dto);

    String updateOrderNo(ChangeNumberDTO dto);

}
