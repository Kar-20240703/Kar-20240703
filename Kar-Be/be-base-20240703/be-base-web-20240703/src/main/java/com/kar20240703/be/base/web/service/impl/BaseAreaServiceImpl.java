package com.kar20240703.be.base.web.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kar20240703.be.base.web.mapper.BaseAreaMapper;
import com.kar20240703.be.base.web.model.domain.BaseAreaDO;
import com.kar20240703.be.base.web.model.dto.BaseAreaInsertOrUpdateDTO;
import com.kar20240703.be.base.web.model.dto.BaseAreaPageDTO;
import com.kar20240703.be.base.web.model.vo.BaseAreaInfoByIdVO;
import com.kar20240703.be.base.web.service.BaseAreaService;
import com.kar20240703.be.temp.web.model.annotation.MyTransactional;
import com.kar20240703.be.temp.web.model.dto.NotEmptyIdSet;
import com.kar20240703.be.temp.web.model.dto.NotNullId;
import java.util.Collections;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class BaseAreaServiceImpl extends ServiceImpl<BaseAreaMapper, BaseAreaDO> implements BaseAreaService {

    /**
     * 新增/修改
     */
    @Override
    @MyTransactional
    public String insertOrUpdate(BaseAreaInsertOrUpdateDTO dto) {
        return "";
    }

    /**
     * 分页排序查询
     */
    @Override
    public Page<BaseAreaDO> myPage(BaseAreaPageDTO dto) {
        return null;
    }

    /**
     * 查询：树结构
     */
    @Override
    public List<BaseAreaDO> tree(BaseAreaPageDTO dto) {
        return Collections.emptyList();
    }

    /**
     * 下拉树形列表
     */
    @Override
    public List<BaseAreaDO> dictTreeList() {
        return Collections.emptyList();
    }

    /**
     * 通过主键id，查看详情
     */
    @Override
    public BaseAreaInfoByIdVO infoById(NotNullId notNullId) {
        return null;
    }

    /**
     * 批量删除
     */
    @Override
    @MyTransactional
    public String deleteByIdSet(NotEmptyIdSet notEmptyIdSet) {
        return "";
    }

}
