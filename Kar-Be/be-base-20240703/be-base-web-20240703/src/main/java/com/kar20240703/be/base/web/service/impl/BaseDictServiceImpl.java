package com.kar20240703.be.base.web.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kar20240703.be.base.web.exception.BaseBizCodeEnum;
import com.kar20240703.be.base.web.model.dto.BaseDictInsertOrUpdateDTO;
import com.kar20240703.be.base.web.model.dto.BaseDictListByDictKeyDTO;
import com.kar20240703.be.base.web.model.dto.BaseDictPageDTO;
import com.kar20240703.be.base.web.service.BaseDictService;
import com.kar20240703.be.temp.web.exception.TempBizCodeEnum;
import com.kar20240703.be.temp.web.mapper.TempDictMapper;
import com.kar20240703.be.temp.web.model.annotation.MyTransactional;
import com.kar20240703.be.temp.web.model.domain.TempDictDO;
import com.kar20240703.be.temp.web.model.domain.TempEntity;
import com.kar20240703.be.temp.web.model.domain.TempEntityNoId;
import com.kar20240703.be.temp.web.model.dto.ChangeNumberDTO;
import com.kar20240703.be.temp.web.model.dto.NotEmptyIdSet;
import com.kar20240703.be.temp.web.model.dto.NotNullId;
import com.kar20240703.be.temp.web.model.enums.TempDictTypeEnum;
import com.kar20240703.be.temp.web.model.vo.DictIntegerVO;
import com.kar20240703.be.temp.web.model.vo.R;
import com.kar20240703.be.temp.web.util.MyDictUtil;
import com.kar20240703.be.temp.web.util.MyEntityUtil;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class BaseDictServiceImpl extends ServiceImpl<TempDictMapper, TempDictDO> implements BaseDictService {

    /**
     * 新增/修改 备注：这里修改了，租户管理那边也要一起修改
     */
    @Override
    @MyTransactional
    public String insertOrUpdate(BaseDictInsertOrUpdateDTO dto) {

        if (TempDictTypeEnum.DICT.equals(dto.getType())) {

            // 字典 key和 name不能重复
            boolean exists = lambdaQuery().eq(TempDictDO::getType, TempDictTypeEnum.DICT)
                .and(i -> i.eq(TempDictDO::getDictKey, dto.getDictKey()).or().eq(TempDictDO::getName, dto.getName()))
                .eq(TempEntityNoId::getEnableFlag, true).ne(dto.getId() != null, TempEntity::getId, dto.getId())
                .exists();

            if (exists) {
                R.error(BaseBizCodeEnum.SAME_KEY_OR_NAME_EXIST);
            }

            dto.setValue(-1); // 字典的value为 -1

        } else {

            if (dto.getValue() == null) {
                R.error(BaseBizCodeEnum.VALUE_CANNOT_BE_EMPTY);
            }

            // 字典项 value和 name不能重复
            boolean exists = lambdaQuery().eq(TempDictDO::getType, TempDictTypeEnum.DICT_ITEM)
                .eq(TempEntityNoId::getEnableFlag, true).eq(TempDictDO::getDictKey, dto.getDictKey())
                .and(i -> i.eq(TempDictDO::getValue, dto.getValue()).or().eq(TempDictDO::getName, dto.getName()))
                .ne(dto.getId() != null, TempEntity::getId, dto.getId()).exists();

            if (exists) {
                R.error(BaseBizCodeEnum.SAME_VALUE_OR_NAME_EXIST);
            }

        }

        if (dto.getId() != null && TempDictTypeEnum.DICT.equals(dto.getType())) {

            // 如果是修改，并且是字典，那么也需要修改 该字典的字典项的 dictKey
            TempDictDO tempDictDO =
                lambdaQuery().eq(TempEntity::getId, dto.getId()).select(TempDictDO::getDictKey).one();

            if (tempDictDO == null) {
                R.errorMsg("操作失败：字典不存在，请刷新重试");
            }

            if (!tempDictDO.getDictKey().equals(dto.getDictKey())) {

                lambdaUpdate().eq(TempDictDO::getDictKey, tempDictDO.getDictKey())
                    .eq(TempDictDO::getType, TempDictTypeEnum.DICT_ITEM).set(TempDictDO::getDictKey, dto.getDictKey())
                    .update();

            }

        }

        TempDictDO tempDictDO = new TempDictDO();

        tempDictDO.setDictKey(dto.getDictKey());
        tempDictDO.setName(dto.getName());
        tempDictDO.setType(dto.getType());
        tempDictDO.setValue(dto.getValue());
        tempDictDO.setOrderNo(MyEntityUtil.getNotNullOrderNo(dto.getOrderNo()));
        tempDictDO.setEnableFlag(BooleanUtil.isTrue(dto.getEnableFlag()));
        tempDictDO.setRemark(MyEntityUtil.getNotNullStr(dto.getRemark()));
        tempDictDO.setId(dto.getId());
        tempDictDO.setUuid(MyEntityUtil.getNotNullStr(dto.getUuid(), IdUtil.simpleUUID()));

        saveOrUpdate(tempDictDO);

        return TempBizCodeEnum.OK;

    }

    /**
     * 分页排序查询
     */
    @Override
    public Page<TempDictDO> myPage(BaseDictPageDTO dto) {

        return lambdaQuery().like(StrUtil.isNotBlank(dto.getName()), TempDictDO::getName, dto.getName())
            .like(StrUtil.isNotBlank(dto.getRemark()), TempEntityNoId::getRemark, dto.getRemark())
            .like(StrUtil.isNotBlank(dto.getDictKey()), TempDictDO::getDictKey, dto.getDictKey())
            .eq(dto.getType() != null, TempDictDO::getType, dto.getType())
            .eq(dto.getValue() != null, TempDictDO::getValue, dto.getValue())
            .eq(dto.getEnableFlag() != null, TempEntityNoId::getEnableFlag, dto.getEnableFlag())
            .orderByDesc(TempDictDO::getOrderNo).page(dto.pageOrder());

    }

    /**
     * 通过：dictKey获取字典项集合，备注：会进行缓存
     */
    @Override
    public List<DictIntegerVO> listByDictKey(BaseDictListByDictKeyDTO dto) {

        return MyDictUtil.listByDictKey(dto.getDictKey());

    }

    /**
     * 查询：树结构
     */
    @Override
    public List<TempDictDO> tree(BaseDictPageDTO dto) {

        dto.setPageSize(-1); // 不分页
        List<TempDictDO> records = myPage(dto).getRecords();

        if (records.size() == 0) {
            return new ArrayList<>();
        }

        // 过滤出：为字典项的数据，目的：查询其所属字典，封装成树结构
        List<TempDictDO> dictItemList =
            records.stream().filter(it -> TempDictTypeEnum.DICT_ITEM.equals(it.getType())).collect(Collectors.toList());

        if (dictItemList.size() == 0) {

            // 如果没有字典项类型数据，则直接返回
            return records;

        }

        // 查询出：字典项所属，字典的信息
        List<TempDictDO> allDictList =
            records.stream().filter(item -> TempDictTypeEnum.DICT.equals(item.getType())).collect(Collectors.toList());

        Set<Long> dictIdSet = allDictList.stream().map(TempEntity::getId).collect(Collectors.toSet());

        Set<String> dictKeySet = dictItemList.stream().map(TempDictDO::getDictKey).collect(Collectors.toSet());

        // 查询数据库：字典信息
        List<TempDictDO> tempDictDoList = lambdaQuery().notIn(dictIdSet.size() != 0, TempEntity::getId, dictIdSet)
            .in(dictKeySet.size() != 0, TempDictDO::getDictKey, dictKeySet)
            .eq(TempDictDO::getType, TempDictTypeEnum.DICT).list();

        // 拼接本次返回值所需的，所有字典
        allDictList.addAll(tempDictDoList);

        // 排序
        allDictList =
            allDictList.stream().sorted(Comparator.comparing(TempDictDO::getOrderNo, Comparator.reverseOrder()))
                .collect(Collectors.toList());

        Map<String, TempDictDO> dictMap =
            allDictList.stream().collect(Collectors.toMap(TempDictDO::getDictKey, it -> it));

        // 封装 children
        for (TempDictDO item : dictItemList) {

            TempDictDO tempDictDO = dictMap.get(item.getDictKey());

            List<TempDictDO> children = tempDictDO.getChildren();

            if (children == null) {

                children = new ArrayList<>();
                tempDictDO.setChildren(children);

            }

            children.add(item); // 添加：字典项

        }

        return allDictList;

    }

    /**
     * 通过主键id，查看详情
     */
    @Override
    public TempDictDO infoById(NotNullId notNullId) {

        return lambdaQuery().eq(TempEntity::getId, notNullId.getId()).one();

    }

    /**
     * 批量删除
     */
    @Override
    @MyTransactional
    public String deleteByIdSet(NotEmptyIdSet notEmptyIdSet, boolean checkDeleteFlag) {

        Set<Long> idSet = notEmptyIdSet.getIdSet();

        if (CollUtil.isEmpty(idSet)) {
            return TempBizCodeEnum.OK;
        }

        List<TempDictDO> tempDictDoList =
            lambdaQuery().in(TempEntity::getId, idSet).eq(TempDictDO::getType, TempDictTypeEnum.DICT)
                .select(TempDictDO::getDictKey).list();

        removeByIds(idSet); // 根据 idSet删除

        if (CollUtil.isEmpty(tempDictDoList)) {
            return TempBizCodeEnum.OK;
        }

        // 如果删除是字典项的父级，则把其下的字典项也跟着删除了
        Set<String> dictKeySet = tempDictDoList.stream().map(TempDictDO::getDictKey).collect(Collectors.toSet());

        lambdaUpdate().in(TempDictDO::getDictKey, dictKeySet).remove();

        return TempBizCodeEnum.OK;

    }

    /**
     * 通过主键 idSet，加减排序号
     */
    @Override
    @MyTransactional
    public String addOrderNo(ChangeNumberDTO dto) {

        if (dto.getNumber() == 0) {
            return TempBizCodeEnum.OK;
        }

        List<TempDictDO> tempDictDoList =
            lambdaQuery().in(TempEntity::getId, dto.getIdSet()).select(TempEntity::getId, TempDictDO::getOrderNo)
                .list();

        for (TempDictDO item : tempDictDoList) {
            item.setOrderNo((int)(item.getOrderNo() + dto.getNumber()));
        }

        updateBatchById(tempDictDoList);

        return TempBizCodeEnum.OK;

    }

    /**
     * 通过主键 idSet，修改排序号
     */
    @Override
    public String updateOrderNo(ChangeNumberDTO dto) {

        if (dto.getNumber() == 0) {
            return TempBizCodeEnum.OK;
        }

        lambdaUpdate().in(TempEntity::getId, dto.getIdSet()).set(TempDictDO::getOrderNo, dto.getNumber()).update();

        return TempBizCodeEnum.OK;

    }

}
