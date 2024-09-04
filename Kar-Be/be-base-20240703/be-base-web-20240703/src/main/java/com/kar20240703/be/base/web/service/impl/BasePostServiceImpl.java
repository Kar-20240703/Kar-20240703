package com.kar20240703.be.base.web.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kar20240703.be.base.web.mapper.BasePostMapper;
import com.kar20240703.be.base.web.model.domain.BasePostDO;
import com.kar20240703.be.base.web.model.dto.BasePostInsertOrUpdateDTO;
import com.kar20240703.be.base.web.model.dto.BasePostPageDTO;
import com.kar20240703.be.base.web.model.vo.BasePostInfoByIdVO;
import com.kar20240703.be.base.web.service.BasePostService;
import com.kar20240703.be.temp.web.model.annotation.MyTransactional;
import com.kar20240703.be.temp.web.model.dto.NotEmptyIdSet;
import com.kar20240703.be.temp.web.model.dto.NotNullId;
import java.util.Collections;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class BasePostServiceImpl extends ServiceImpl<BasePostMapper, BasePostDO> implements BasePostService {

    /**
     * 新增/修改
     */
    @Override
    @MyTransactional
    public String insertOrUpdate(BasePostInsertOrUpdateDTO dto) {
        return "";
    }

    /**
     * 分页排序查询
     */
    @Override
    public Page<BasePostDO> myPage(BasePostPageDTO dto) {
        return null;
    }

    /**
     * 查询：树结构
     */
    @Override
    public List<BasePostDO> tree(BasePostPageDTO dto) {
        return Collections.emptyList();
    }

    /**
     * 下拉树形列表
     */
    @Override
    public List<BasePostDO> dictTreeList() {
        return Collections.emptyList();
    }

    /**
     * 通过主键id，查看详情
     */
    @Override
    public BasePostInfoByIdVO infoById(NotNullId notNullId) {
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
