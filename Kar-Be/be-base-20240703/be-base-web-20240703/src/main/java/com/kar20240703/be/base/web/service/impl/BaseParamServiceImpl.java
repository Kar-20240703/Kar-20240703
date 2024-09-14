package com.kar20240703.be.base.web.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kar20240703.be.base.web.exception.BaseBizCodeEnum;
import com.kar20240703.be.base.web.model.dto.BaseParamInsertOrUpdateDTO;
import com.kar20240703.be.base.web.model.dto.BaseParamPageDTO;
import com.kar20240703.be.base.web.service.BaseParamService;
import com.kar20240703.be.temp.web.exception.TempBizCodeEnum;
import com.kar20240703.be.temp.web.mapper.TempParamMapper;
import com.kar20240703.be.temp.web.model.annotation.MyTransactional;
import com.kar20240703.be.temp.web.model.domain.TempEntity;
import com.kar20240703.be.temp.web.model.domain.TempEntityNoId;
import com.kar20240703.be.temp.web.model.domain.TempEntityNoIdSuper;
import com.kar20240703.be.temp.web.model.domain.TempParamDO;
import com.kar20240703.be.temp.web.model.dto.NotEmptyIdSet;
import com.kar20240703.be.temp.web.model.dto.NotNullId;
import com.kar20240703.be.temp.web.model.vo.R;
import com.kar20240703.be.temp.web.util.MyEntityUtil;
import com.kar20240703.be.temp.web.util.MyParamUtil;
import java.util.Set;
import org.springframework.stereotype.Service;

@Service
public class BaseParamServiceImpl extends ServiceImpl<TempParamMapper, TempParamDO> implements BaseParamService {

    /**
     * 新增/修改
     */
    @Override
    @MyTransactional
    public String insertOrUpdate(BaseParamInsertOrUpdateDTO dto) {

        // uuid不能重复
        if (StrUtil.isNotBlank(dto.getUuid())) {

            boolean exists = lambdaQuery().eq(TempParamDO::getUuid, dto.getUuid())
                .ne(dto.getId() != null, TempEntity::getId, dto.getId()).exists();

            if (exists) {
                R.error(BaseBizCodeEnum.UUID_IS_EXIST);
            }

        }

        TempParamDO tempParamDO = new TempParamDO();

        tempParamDO.setName(dto.getName());
        tempParamDO.setValue(dto.getValue());
        tempParamDO.setEnableFlag(BooleanUtil.isTrue(dto.getEnableFlag()));
        tempParamDO.setId(dto.getId());
        tempParamDO.setUuid(MyEntityUtil.getNotNullStr(dto.getUuid(), IdUtil.simpleUUID()));
        tempParamDO.setRemark(MyEntityUtil.getNotNullStr(dto.getRemark()));

        saveOrUpdate(tempParamDO);

        return TempBizCodeEnum.OK;

    }

    /**
     * 分页排序查询
     */
    @Override
    public Page<TempParamDO> myPage(BaseParamPageDTO dto) {

        return lambdaQuery().like(StrUtil.isNotBlank(dto.getName()), TempParamDO::getName, dto.getName())
            .like(StrUtil.isNotBlank(dto.getRemark()), TempEntityNoId::getRemark, dto.getRemark())
            .eq(dto.getEnableFlag() != null, TempEntityNoId::getEnableFlag, dto.getEnableFlag())
            .orderByDesc(TempEntityNoIdSuper::getUpdateTime).page(dto.pageOrder());

    }

    /**
     * 通过主键id，查看详情
     */
    @Override
    public TempParamDO infoById(NotNullId notNullId) {

        return lambdaQuery().eq(TempEntity::getId, notNullId.getId()).one();

    }

    /**
     * 批量删除
     */
    @Override
    @MyTransactional
    public String deleteByIdSet(NotEmptyIdSet notEmptyIdSet) {

        Set<Long> idSet = notEmptyIdSet.getIdSet();

        if (CollUtil.isEmpty(idSet)) {
            return TempBizCodeEnum.OK;
        }

        for (String item : MyParamUtil.SYSTEM_PARAM_NOT_DELETE_ID_SET) {

            if (idSet.contains(Convert.toLong(item))) {

                R.errorMsg("操作失败：id【{}】不允许删除", item);

            }

        }

        removeByIds(idSet); // 根据 idSet删除

        return TempBizCodeEnum.OK;

    }

}
