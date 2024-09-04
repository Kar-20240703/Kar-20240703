package com.kar20240703.be.base.web.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kar20240703.be.base.web.model.domain.BaseAreaDO;
import com.kar20240703.be.base.web.model.dto.BaseAreaInsertOrUpdateDTO;
import com.kar20240703.be.base.web.model.dto.BaseAreaPageDTO;
import com.kar20240703.be.base.web.model.vo.BaseAreaInfoByIdVO;
import com.kar20240703.be.temp.web.model.dto.NotEmptyIdSet;
import com.kar20240703.be.temp.web.model.dto.NotNullId;
import java.util.List;

public interface BaseAreaService extends IService<BaseAreaDO> {

    String insertOrUpdate(BaseAreaInsertOrUpdateDTO dto);

    Page<BaseAreaDO> myPage(BaseAreaPageDTO dto);

    List<BaseAreaDO> tree(BaseAreaPageDTO dto);

    List<BaseAreaDO> dictTreeList();

    BaseAreaInfoByIdVO infoById(NotNullId notNullId);

    String deleteByIdSet(NotEmptyIdSet notEmptyIdSet);

}
