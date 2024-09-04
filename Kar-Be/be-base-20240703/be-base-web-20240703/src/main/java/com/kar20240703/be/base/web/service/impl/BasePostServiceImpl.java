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
import com.kar20240703.be.base.web.mapper.BasePostMapper;
import com.kar20240703.be.base.web.model.domain.BasePostDO;
import com.kar20240703.be.base.web.model.domain.BasePostRefUserDO;
import com.kar20240703.be.base.web.model.dto.BasePostInsertOrUpdateDTO;
import com.kar20240703.be.base.web.model.dto.BasePostPageDTO;
import com.kar20240703.be.base.web.model.vo.BasePostInfoByIdVO;
import com.kar20240703.be.base.web.service.BasePostRefUserService;
import com.kar20240703.be.base.web.service.BasePostService;
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
public class BasePostServiceImpl extends ServiceImpl<BasePostMapper, BasePostDO> implements BasePostService {

    @Resource
    BasePostRefUserService basePostRefUserService;

    /**
     * 新增/修改
     */
    @Override
    @MyTransactional
    public String insertOrUpdate(BasePostInsertOrUpdateDTO dto) {

        if (dto.getId() != null && dto.getId().equals(dto.getPid())) {
            R.error(TempBizCodeEnum.PID_CANNOT_BE_EQUAL_TO_ID);
        }

        // uuid不能重复
        if (StrUtil.isNotBlank(dto.getUuid())) {

            boolean exists = lambdaQuery().eq(BasePostDO::getUuid, dto.getUuid())
                .ne(dto.getId() != null, TempEntity::getId, dto.getId()).exists();

            if (exists) {
                R.error(BaseBizCodeEnum.UUID_IS_EXIST);
            }

        }

        if (dto.getId() != null) {

            deleteByIdSetSub(CollUtil.newHashSet(dto.getId())); // 先删除：子表数据

        }

        BasePostDO basePostDO = getDoByDto(dto);

        saveOrUpdate(basePostDO);

        insertOrUpdateSub(basePostDO, dto); // 新增 子表数据

        return TempBizCodeEnum.OK;

    }

    /**
     * 新增/修改：新增 子表数据
     */
    private void insertOrUpdateSub(BasePostDO basePostDO, BasePostInsertOrUpdateDTO dto) {

        // 如果禁用了，则子表不进行新增操作
        if (BooleanUtil.isFalse(basePostDO.getEnableFlag())) {
            return;
        }

        // 新增：岗位用户关联表数据
        if (CollUtil.isNotEmpty(dto.getUserIdSet())) {

            List<BasePostRefUserDO> insertList =
                dto.getUserIdSet().stream().map(it -> new BasePostRefUserDO(basePostDO.getId(), it))
                    .collect(Collectors.toList());

            basePostRefUserService.saveBatch(insertList);

        }

    }

    /**
     * 通过 dto，获取 do
     */
    private BasePostDO getDoByDto(BasePostInsertOrUpdateDTO dto) {

        BasePostDO basePostDO = new BasePostDO();

        basePostDO.setName(dto.getName());
        basePostDO.setPid(MyEntityUtil.getNotNullParentId(dto.getPid()));
        basePostDO.setId(dto.getId());
        basePostDO.setEnableFlag(BooleanUtil.isTrue(dto.getEnableFlag()));

        basePostDO.setRemark(MyEntityUtil.getNotNullStr(dto.getRemark()));
        basePostDO.setOrderNo(MyEntityUtil.getNotNullOrderNo(dto.getOrderNo()));

        basePostDO.setUuid(MyEntityUtil.getNotNullStr(dto.getUuid(), IdUtil.simpleUUID()));

        return basePostDO;

    }

    /**
     * 删除子表数据
     */
    private void deleteByIdSetSub(Set<Long> idSet) {

        // 删除：岗位用户关联表
        basePostRefUserService.lambdaUpdate().in(BasePostRefUserDO::getPostId, idSet).remove();

    }

    /**
     * 分页排序查询
     */
    @Override
    public Page<BasePostDO> myPage(BasePostPageDTO dto) {

        return lambdaQuery().like(StrUtil.isNotBlank(dto.getName()), BasePostDO::getName, dto.getName())
            .eq(dto.getEnableFlag() != null, TempEntity::getEnableFlag, dto.getEnableFlag())
            .select(TempEntity::getId, TempEntityTree::getPid, BasePostDO::getName, TempEntityNoId::getEnableFlag,
                BasePostDO::getUuid, TempEntityTree::getOrderNo).orderByDesc(TempEntityTree::getOrderNo)
            .orderByAsc(TempEntity::getId).page(dto.pageOrder());

    }

    /**
     * 查询：树结构
     */
    @SneakyThrows
    @Override
    public List<BasePostDO> tree(BasePostPageDTO dto) {

        dto.setPageSize(-1); // 不分页

        CountDownLatch countDownLatch = ThreadUtil.newCountDownLatch(1);

        CallBack<List<BasePostDO>> allListCallBack = new CallBack<>();

        MyThreadUtil.execute(() -> {

            Page<BasePostDO> page = lambdaQuery().select(TempEntity::getId, TempEntityTree::getPid, BasePostDO::getName,
                    TempEntityNoId::getEnableFlag, BasePostDO::getUuid, TempEntityTree::getOrderNo)
                .orderByDesc(TempEntityTree::getOrderNo).orderByAsc(TempEntity::getId).page(dto.pageOrder());

            allListCallBack.setValue(page.getRecords());

        }, countDownLatch);

        // 根据条件进行筛选，得到符合条件的数据，然后再逆向生成整棵树，并返回这个树结构
        List<BasePostDO> basePostDoList = myPage(dto).getRecords();

        countDownLatch.await();

        if (basePostDoList.size() == 0) {
            return new ArrayList<>();
        }

        if (allListCallBack.getValue().size() == 0) {
            return new ArrayList<>();
        }

        return MyTreeUtil.getFullTreeByDeepNode(basePostDoList, allListCallBack.getValue());

    }

    /**
     * 下拉树形列表
     */
    @Override
    public List<BasePostDO> dictTreeList() {

        List<BasePostDO> basePostDoList = lambdaQuery().eq(TempEntityNoId::getEnableFlag, true)
            .select(TempEntity::getId, TempEntityTree::getPid, BasePostDO::getName)
            .orderByDesc(TempEntityTree::getOrderNo).orderByAsc(TempEntity::getId).list();

        return MyTreeUtil.listToTree(basePostDoList);

    }

    /**
     * 通过主键id，查看详情
     */
    @Override
    public BasePostInfoByIdVO infoById(NotNullId notNullId) {

        BasePostDO basePostDO = lambdaQuery().eq(TempEntity::getId, notNullId.getId()).one();

        if (basePostDO == null) {
            return null;
        }

        BasePostInfoByIdVO basePostInfoByIdVO = BeanUtil.copyProperties(basePostDO, BasePostInfoByIdVO.class);

        // 设置：用户 idSet
        List<BasePostRefUserDO> basePostRefUserDoList =
            basePostRefUserService.lambdaQuery().eq(BasePostRefUserDO::getPostId, notNullId.getId())
                .select(BasePostRefUserDO::getUserId).list();

        basePostInfoByIdVO.setUserIdSet(
            basePostRefUserDoList.stream().map(BasePostRefUserDO::getUserId).collect(Collectors.toSet()));

        // 处理：父级 id
        MyEntityUtil.handleParentId(basePostInfoByIdVO);

        return basePostInfoByIdVO;

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
