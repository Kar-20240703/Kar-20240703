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
import com.kar20240703.be.base.web.mapper.BaseAreaMapper;
import com.kar20240703.be.base.web.model.domain.BaseAreaDO;
import com.kar20240703.be.base.web.model.domain.BaseAreaRefUserDO;
import com.kar20240703.be.base.web.model.dto.BaseAreaInsertOrUpdateDTO;
import com.kar20240703.be.base.web.model.dto.BaseAreaPageDTO;
import com.kar20240703.be.base.web.model.vo.BaseAreaInfoByIdVO;
import com.kar20240703.be.base.web.service.BaseAreaRefUserService;
import com.kar20240703.be.base.web.service.BaseAreaService;
import com.kar20240703.be.temp.web.exception.TempBizCodeEnum;
import com.kar20240703.be.temp.web.model.annotation.MyTransactional;
import com.kar20240703.be.temp.web.model.domain.TempEntity;
import com.kar20240703.be.temp.web.model.domain.TempEntityNoId;
import com.kar20240703.be.temp.web.model.domain.TempEntityTree;
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
public class BaseAreaServiceImpl extends ServiceImpl<BaseAreaMapper, BaseAreaDO> implements BaseAreaService {

    @Resource
    BaseAreaRefUserService baseAreaRefUserService;

    /**
     * 新增/修改
     */
    @Override
    @MyTransactional
    public String insertOrUpdate(BaseAreaInsertOrUpdateDTO dto) {

        if (dto.getId() != null && dto.getId().equals(dto.getPid())) {
            R.error(TempBizCodeEnum.PID_CANNOT_BE_EQUAL_TO_ID);
        }

        // uuid不能重复
        if (StrUtil.isNotBlank(dto.getUuid())) {

            boolean exists = lambdaQuery().eq(BaseAreaDO::getUuid, dto.getUuid())
                .ne(dto.getId() != null, TempEntity::getId, dto.getId()).exists();

            if (exists) {
                R.error(BaseBizCodeEnum.UUID_IS_EXIST);
            }

        }

        if (dto.getId() != null) {

            deleteByIdSetSub(CollUtil.newHashSet(dto.getId())); // 先删除：子表数据

        }

        BaseAreaDO baseAreaDO = getDoByDto(dto);

        saveOrUpdate(baseAreaDO);

        insertOrUpdateSub(baseAreaDO, dto); // 新增 子表数据

        return TempBizCodeEnum.OK;

    }

    /**
     * 新增/修改：新增 子表数据
     */
    private void insertOrUpdateSub(BaseAreaDO baseAreaDO, BaseAreaInsertOrUpdateDTO dto) {

        // 如果禁用了，则子表不进行新增操作
        if (BooleanUtil.isFalse(baseAreaDO.getEnableFlag())) {
            return;
        }

        // 新增：区域用户关联表数据
        if (CollUtil.isNotEmpty(dto.getUserIdSet())) {

            List<BaseAreaRefUserDO> insertList =
                dto.getUserIdSet().stream().map(it -> new BaseAreaRefUserDO(baseAreaDO.getId(), it))
                    .collect(Collectors.toList());

            baseAreaRefUserService.saveBatch(insertList);

        }

    }

    /**
     * 通过 dto，获取 do
     */
    private BaseAreaDO getDoByDto(BaseAreaInsertOrUpdateDTO dto) {

        BaseAreaDO baseAreaDO = new BaseAreaDO();

        baseAreaDO.setName(dto.getName());
        baseAreaDO.setPid(MyEntityUtil.getNotNullParentId(dto.getPid()));
        baseAreaDO.setId(dto.getId());
        baseAreaDO.setEnableFlag(BooleanUtil.isTrue(dto.getEnableFlag()));

        baseAreaDO.setRemark(MyEntityUtil.getNotNullStr(dto.getRemark()));
        baseAreaDO.setOrderNo(MyEntityUtil.getNotNullOrderNo(dto.getOrderNo()));

        baseAreaDO.setUuid(MyEntityUtil.getNotNullStr(dto.getUuid(), IdUtil.simpleUUID()));

        return baseAreaDO;

    }

    /**
     * 删除子表数据
     */
    private void deleteByIdSetSub(Set<Long> idSet) {

        // 删除：部门用户关联表
        baseAreaRefUserService.lambdaUpdate().in(BaseAreaRefUserDO::getAreaId, idSet).remove();

    }

    /**
     * 分页排序查询
     */
    @Override
    public Page<BaseAreaDO> myPage(BaseAreaPageDTO dto) {

        return lambdaQuery().like(StrUtil.isNotBlank(dto.getName()), BaseAreaDO::getName, dto.getName())
            .eq(dto.getEnableFlag() != null, TempEntity::getEnableFlag, dto.getEnableFlag())
            .select(TempEntity::getId, TempEntityTree::getPid, BaseAreaDO::getName, TempEntityNoId::getEnableFlag,
                BaseAreaDO::getUuid, TempEntityTree::getOrderNo).orderByDesc(TempEntityTree::getOrderNo)
            .orderByAsc(TempEntity::getId).page(dto.pageOrder());

    }

    /**
     * 查询：树结构
     */
    @SneakyThrows
    @Override
    public List<BaseAreaDO> tree(BaseAreaPageDTO dto) {

        dto.setPageSize(-1); // 不分页

        CountDownLatch countDownLatch = ThreadUtil.newCountDownLatch(1);

        CallBack<List<BaseAreaDO>> allListCallBack = new CallBack<>();

        MyThreadUtil.execute(() -> {

            Page<BaseAreaDO> page = lambdaQuery().select(TempEntity::getId, TempEntityTree::getPid, BaseAreaDO::getName,
                    TempEntityNoId::getEnableFlag, BaseAreaDO::getUuid, TempEntityTree::getOrderNo)
                .orderByDesc(TempEntityTree::getOrderNo).orderByAsc(TempEntity::getId).page(dto.pageOrder());

            allListCallBack.setValue(page.getRecords());

        }, countDownLatch);

        // 根据条件进行筛选，得到符合条件的数据，然后再逆向生成整棵树，并返回这个树结构
        List<BaseAreaDO> baseAreaDoList = myPage(dto).getRecords();

        countDownLatch.await();

        if (baseAreaDoList.size() == 0) {
            return new ArrayList<>();
        }

        if (allListCallBack.getValue().size() == 0) {
            return new ArrayList<>();
        }

        return MyTreeUtil.getFullTreeByDeepNode(baseAreaDoList, allListCallBack.getValue());

    }

    /**
     * 下拉树形列表
     */
    @Override
    public List<BaseAreaDO> dictTreeList() {

        List<BaseAreaDO> baseAreaDoList = lambdaQuery().eq(TempEntityNoId::getEnableFlag, true)
            .select(TempEntity::getId, TempEntityTree::getPid, BaseAreaDO::getName)
            .orderByDesc(TempEntityTree::getOrderNo).orderByAsc(TempEntity::getId).list();

        return MyTreeUtil.listToTree(baseAreaDoList);

    }

    /**
     * 通过主键id，查看详情
     */
    @Override
    public BaseAreaInfoByIdVO infoById(NotNullId notNullId) {

        BaseAreaDO baseAreaDO = lambdaQuery().eq(TempEntity::getId, notNullId.getId()).one();

        if (baseAreaDO == null) {
            return null;
        }

        BaseAreaInfoByIdVO baseAreaInfoByIdVO = BeanUtil.copyProperties(baseAreaDO, BaseAreaInfoByIdVO.class);

        // 设置：用户 idSet
        List<BaseAreaRefUserDO> baseAreaRefUserDoList =
            baseAreaRefUserService.lambdaQuery().eq(BaseAreaRefUserDO::getAreaId, notNullId.getId())
                .select(BaseAreaRefUserDO::getUserId).list();

        baseAreaInfoByIdVO.setUserIdSet(
            baseAreaRefUserDoList.stream().map(BaseAreaRefUserDO::getUserId).collect(Collectors.toSet()));

        // 处理：父级 id
        MyEntityUtil.handleParentId(baseAreaInfoByIdVO);

        return baseAreaInfoByIdVO;

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

}
