package com.kar20240703.be.base.web.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kar20240703.be.base.web.exception.BaseBizCodeEnum;
import com.kar20240703.be.base.web.mapper.BaseDeptMapper;
import com.kar20240703.be.base.web.model.domain.BaseDeptDO;
import com.kar20240703.be.base.web.model.domain.BaseDeptRefUserDO;
import com.kar20240703.be.base.web.model.dto.BaseDeptInsertOrUpdateDTO;
import com.kar20240703.be.base.web.model.dto.BaseDeptPageDTO;
import com.kar20240703.be.base.web.model.vo.BaseDeptInfoByIdVO;
import com.kar20240703.be.base.web.service.BaseDeptRefUserService;
import com.kar20240703.be.base.web.service.BaseDeptService;
import com.kar20240703.be.temp.web.exception.TempBizCodeEnum;
import com.kar20240703.be.temp.web.model.annotation.MyTransactional;
import com.kar20240703.be.temp.web.model.domain.TempEntity;
import com.kar20240703.be.temp.web.model.domain.TempEntityNoId;
import com.kar20240703.be.temp.web.model.domain.TempEntityTree;
import com.kar20240703.be.temp.web.model.dto.ChangeNumberDTO;
import com.kar20240703.be.temp.web.model.dto.NotEmptyIdSet;
import com.kar20240703.be.temp.web.model.dto.NotNullId;
import com.kar20240703.be.temp.web.model.vo.R;
import com.kar20240703.be.temp.web.util.CallBack;
import com.kar20240703.be.temp.web.util.MyEntityUtil;
import com.kar20240703.be.temp.web.util.MyThreadUtil;
import com.kar20240703.be.temp.web.util.MyTreeUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

@Service
public class BaseDeptServiceImpl extends ServiceImpl<BaseDeptMapper, BaseDeptDO> implements BaseDeptService {

    @Resource
    BaseDeptRefUserService baseDeptRefUserService;

    /**
     * 新增/修改
     */
    @Override
    @MyTransactional
    public String insertOrUpdate(BaseDeptInsertOrUpdateDTO dto) {

        if (dto.getId() != null && dto.getId().equals(dto.getPid())) {
            R.error(TempBizCodeEnum.PID_CANNOT_BE_EQUAL_TO_ID);
        }

        // uuid不能重复
        if (StrUtil.isNotBlank(dto.getUuid())) {

            boolean exists = lambdaQuery().eq(BaseDeptDO::getUuid, dto.getUuid())
                .ne(dto.getId() != null, TempEntity::getId, dto.getId()).exists();

            if (exists) {
                R.error(BaseBizCodeEnum.UUID_IS_EXIST);
            }

        }

        if (dto.getId() != null) {

            deleteByIdSetSub(CollUtil.newHashSet(dto.getId())); // 先删除：子表数据

        }

        BaseDeptDO baseDeptDO = getDoByDto(dto);

        saveOrUpdate(baseDeptDO);

        insertOrUpdateSub(baseDeptDO, dto); // 新增 子表数据

        return TempBizCodeEnum.OK;

    }

    /**
     * 新增/修改：新增 子表数据
     */
    private void insertOrUpdateSub(BaseDeptDO baseDeptDO, BaseDeptInsertOrUpdateDTO dto) {

        // 如果禁用了，则子表不进行新增操作
        if (BooleanUtil.isFalse(baseDeptDO.getEnableFlag())) {
            return;
        }

        // 新增：部门用户关联表数据
        if (CollUtil.isNotEmpty(dto.getUserIdSet())) {

            List<BaseDeptRefUserDO> insertList =
                dto.getUserIdSet().stream().map(it -> new BaseDeptRefUserDO(baseDeptDO.getId(), it))
                    .collect(Collectors.toList());

            baseDeptRefUserService.saveBatch(insertList);

        }

    }

    /**
     * 通过 dto，获取 do
     */
    private BaseDeptDO getDoByDto(BaseDeptInsertOrUpdateDTO dto) {

        BaseDeptDO baseDeptDO = new BaseDeptDO();

        baseDeptDO.setName(dto.getName());
        baseDeptDO.setPid(MyEntityUtil.getNotNullParentId(dto.getPid()));
        baseDeptDO.setId(dto.getId());
        baseDeptDO.setEnableFlag(BooleanUtil.isTrue(dto.getEnableFlag()));

        baseDeptDO.setRemark(MyEntityUtil.getNotNullStr(dto.getRemark()));
        baseDeptDO.setOrderNo(MyEntityUtil.getNotNullOrderNo(dto.getOrderNo()));

        baseDeptDO.setUuid(MyEntityUtil.getNotNullStr(dto.getUuid(), IdUtil.simpleUUID()));

        return baseDeptDO;

    }

    /**
     * 删除子表数据
     */
    private void deleteByIdSetSub(Set<Long> idSet) {

        // 删除：部门用户关联表
        baseDeptRefUserService.lambdaUpdate().in(BaseDeptRefUserDO::getDeptId, idSet).remove();

    }

    /**
     * 分页排序查询
     */
    @Override
    public Page<BaseDeptDO> myPage(BaseDeptPageDTO dto) {

        return lambdaQuery().like(StrUtil.isNotBlank(dto.getName()), BaseDeptDO::getName, dto.getName())
            .eq(dto.getEnableFlag() != null, TempEntity::getEnableFlag, dto.getEnableFlag())
            .select(TempEntity::getId, TempEntityTree::getPid, BaseDeptDO::getName, TempEntityNoId::getEnableFlag,
                BaseDeptDO::getUuid, TempEntityTree::getOrderNo).orderByDesc(TempEntityTree::getOrderNo)
            .orderByAsc(TempEntity::getId).page(dto.pageOrder());

    }

    /**
     * 查询：树结构
     */
    @SneakyThrows
    @Override
    public List<BaseDeptDO> tree(BaseDeptPageDTO dto) {

        dto.setPageSize(-1); // 不分页

        CountDownLatch countDownLatch = ThreadUtil.newCountDownLatch(1);

        CallBack<List<BaseDeptDO>> allListCallBack = new CallBack<>();

        MyThreadUtil.execute(() -> {

            Page<BaseDeptDO> page = lambdaQuery().select(TempEntity::getId, TempEntityTree::getPid, BaseDeptDO::getName,
                    TempEntityNoId::getEnableFlag, BaseDeptDO::getUuid, TempEntityTree::getOrderNo)
                .orderByDesc(TempEntityTree::getOrderNo).orderByAsc(TempEntity::getId).page(dto.pageOrder());

            allListCallBack.setValue(page.getRecords());

        }, countDownLatch);

        // 根据条件进行筛选，得到符合条件的数据，然后再逆向生成整棵树，并返回这个树结构
        List<BaseDeptDO> baseDeptDoList = myPage(dto).getRecords();

        countDownLatch.await();

        if (baseDeptDoList.size() == 0) {
            return new ArrayList<>();
        }

        if (allListCallBack.getValue().size() == 0) {
            return new ArrayList<>();
        }

        return MyTreeUtil.getFullTreeByDeepNode(baseDeptDoList, allListCallBack.getValue());

    }

    /**
     * 下拉树形列表
     */
    @Override
    public List<BaseDeptDO> dictTreeList() {

        List<BaseDeptDO> baseDeptDoList = lambdaQuery().eq(TempEntityNoId::getEnableFlag, true)
            .select(TempEntity::getId, TempEntityTree::getPid, BaseDeptDO::getName)
            .orderByDesc(TempEntityTree::getOrderNo).orderByAsc(TempEntity::getId).list();

        return MyTreeUtil.listToTree(baseDeptDoList);

    }

    /**
     * 通过主键id，查看详情
     */
    @Override
    public BaseDeptInfoByIdVO infoById(NotNullId notNullId) {

        BaseDeptDO baseDeptDO = lambdaQuery().eq(TempEntity::getId, notNullId.getId()).one();

        if (baseDeptDO == null) {
            return null;
        }

        BaseDeptInfoByIdVO baseDeptInfoByIdVO = BeanUtil.copyProperties(baseDeptDO, BaseDeptInfoByIdVO.class);

        // 设置：用户 idSet
        List<BaseDeptRefUserDO> baseDeptRefUserDoList =
            baseDeptRefUserService.lambdaQuery().eq(BaseDeptRefUserDO::getDeptId, notNullId.getId())
                .select(BaseDeptRefUserDO::getUserId).list();

        baseDeptInfoByIdVO.setUserIdSet(
            baseDeptRefUserDoList.stream().map(BaseDeptRefUserDO::getUserId).collect(Collectors.toSet()));

        // 处理：父级 id
        MyEntityUtil.handleParentId(baseDeptInfoByIdVO);

        return baseDeptInfoByIdVO;

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

        // 如果存在下级，则无法删除
        boolean exists = lambdaQuery().in(TempEntityTree::getPid, idSet).exists();

        if (exists) {
            R.error(TempBizCodeEnum.PLEASE_DELETE_THE_CHILD_NODE_FIRST);
        }

        // 删除子表数据
        deleteByIdSetSub(idSet);

        removeByIds(idSet);

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

        List<BaseDeptDO> baseDeptDoList =
            lambdaQuery().in(TempEntity::getId, dto.getIdSet()).select(TempEntity::getId, TempEntityTree::getOrderNo)
                .list();

        for (BaseDeptDO item : baseDeptDoList) {
            item.setOrderNo((int)(item.getOrderNo() + dto.getNumber()));
        }

        updateBatchById(baseDeptDoList);

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

        lambdaUpdate().in(TempEntity::getId, dto.getIdSet()).set(TempEntityTree::getOrderNo, dto.getNumber()).update();

        return TempBizCodeEnum.OK;

    }

}
