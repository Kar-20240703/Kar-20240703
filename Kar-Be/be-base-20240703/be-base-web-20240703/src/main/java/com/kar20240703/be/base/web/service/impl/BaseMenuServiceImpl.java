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
import com.kar20240703.be.base.web.mapper.BaseMenuMapper;
import com.kar20240703.be.base.web.model.domain.BaseMenuDO;
import com.kar20240703.be.base.web.model.domain.BaseRoleRefMenuDO;
import com.kar20240703.be.base.web.model.dto.BaseMenuInsertOrUpdateDTO;
import com.kar20240703.be.base.web.model.dto.BaseMenuPageDTO;
import com.kar20240703.be.base.web.model.vo.BaseMenuInfoByIdVO;
import com.kar20240703.be.base.web.service.BaseMenuService;
import com.kar20240703.be.base.web.service.BaseRoleRefMenuService;
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
import com.kar20240703.be.temp.web.util.MyUserUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

@Service
public class BaseMenuServiceImpl extends ServiceImpl<BaseMenuMapper, BaseMenuDO> implements BaseMenuService {

    @Resource
    BaseRoleRefMenuService baseRoleRefMenuService;

    /**
     * 新增/修改
     */
    @Override
    @MyTransactional
    public String insertOrUpdate(BaseMenuInsertOrUpdateDTO dto) {

        if (dto.getId() != null && dto.getId().equals(dto.getPid())) {
            R.error(TempBizCodeEnum.PID_CANNOT_BE_EQUAL_TO_ID);
        }

        // path不能重复
        if (StrUtil.isNotBlank(dto.getPath())) {

            boolean exists = lambdaQuery().eq(BaseMenuDO::getPath, dto.getPath())
                .ne(dto.getId() != null, TempEntity::getId, dto.getId()).exists();

            if (exists) {
                R.error(BaseBizCodeEnum.MENU_PATH_IS_EXIST);
            }

        }

        // uuid不能重复
        if (StrUtil.isNotBlank(dto.getUuid())) {

            boolean exists = lambdaQuery().eq(BaseMenuDO::getUuid, dto.getUuid())
                .ne(dto.getId() != null, TempEntity::getId, dto.getId()).exists();

            if (exists) {
                R.error(BaseBizCodeEnum.UUID_IS_EXIST);
            }

        }

        if (dto.getId() != null) {

            deleteByIdSetSub(CollUtil.newHashSet(dto.getId())); // 先删除：子表数据

        }

        BaseMenuDO baseMenuDO = getDoByDto(dto);

        saveOrUpdate(baseMenuDO);

        insertOrUpdateSub(baseMenuDO, dto); // 新增 子表数据

        return TempBizCodeEnum.OK;

    }

    /**
     * 通过 dto，获取 do
     */
    private BaseMenuDO getDoByDto(BaseMenuInsertOrUpdateDTO dto) {

        BaseMenuDO baseMenuDO = new BaseMenuDO();

        baseMenuDO.setName(dto.getName());
        baseMenuDO.setPath(MyEntityUtil.getNotNullStr(dto.getPath()));
        baseMenuDO.setIcon(MyEntityUtil.getNotNullStr(dto.getIcon()));
        baseMenuDO.setPid(MyEntityUtil.getNotNullParentId(dto.getPid()));
        baseMenuDO.setId(dto.getId());
        baseMenuDO.setEnableFlag(BooleanUtil.isTrue(dto.getEnableFlag()));

        baseMenuDO.setRouter(MyEntityUtil.getNotNullStr(dto.getRouter()));
        baseMenuDO.setRedirect(MyEntityUtil.getNotNullStr(dto.getRedirect()));
        baseMenuDO.setRemark(MyEntityUtil.getNotNullStr(dto.getRemark()));
        baseMenuDO.setOrderNo(MyEntityUtil.getNotNullOrderNo(dto.getOrderNo()));

        baseMenuDO.setShowFlag(BooleanUtil.isTrue(dto.getShowFlag()));
        baseMenuDO.setLinkFlag(BooleanUtil.isTrue(dto.getLinkFlag()));

        baseMenuDO.setUuid(MyEntityUtil.getNotNullStr(dto.getUuid(), IdUtil.simpleUUID()));

        return baseMenuDO;

    }

    /**
     * 新增/修改：新增 子表数据
     */
    private void insertOrUpdateSub(BaseMenuDO baseMenuDO, BaseMenuInsertOrUpdateDTO dto) {

        // 如果禁用了，则子表不进行新增操作
        if (BooleanUtil.isFalse(baseMenuDO.getEnableFlag())) {
            return;
        }

        // 新增：角色菜单，菜单用户 关联表数据
        if (CollUtil.isNotEmpty(dto.getRoleIdSet())) {

            List<BaseRoleRefMenuDO> insertList =
                dto.getRoleIdSet().stream().map(it -> new BaseRoleRefMenuDO(it, baseMenuDO.getId()))
                    .collect(Collectors.toList());

            baseRoleRefMenuService.saveBatch(insertList);

        }

    }

    /**
     * 删除子表数据
     */
    private void deleteByIdSetSub(Set<Long> idSet) {

        // 删除：角色菜单关联表
        baseRoleRefMenuService.lambdaUpdate().in(BaseRoleRefMenuDO::getMenuId, idSet).remove();

    }

    /**
     * 分页排序查询
     */
    @Override
    public Page<BaseMenuDO> myPage(BaseMenuPageDTO dto) {

        return lambdaQuery().like(StrUtil.isNotBlank(dto.getName()), BaseMenuDO::getName, dto.getName())
            .like(StrUtil.isNotBlank(dto.getPath()), BaseMenuDO::getPath, dto.getPath())
            .like(StrUtil.isNotBlank(dto.getRedirect()), BaseMenuDO::getRedirect, dto.getRedirect())
            .eq(StrUtil.isNotBlank(dto.getRouter()), BaseMenuDO::getRouter, dto.getRouter())
            .eq(dto.getPid() != null, BaseMenuDO::getPid, dto.getPid())
            .eq(dto.getEnableFlag() != null, TempEntity::getEnableFlag, dto.getEnableFlag())
            .eq(dto.getLinkFlag() != null, BaseMenuDO::getLinkFlag, dto.getLinkFlag())
            .eq(dto.getShowFlag() != null, BaseMenuDO::getShowFlag, dto.getShowFlag())
            .select(TempEntity::getId, TempEntityTree::getPid, BaseMenuDO::getName, BaseMenuDO::getPath,
                BaseMenuDO::getRouter, BaseMenuDO::getShowFlag, TempEntityNoId::getEnableFlag, BaseMenuDO::getRedirect,
                BaseMenuDO::getUuid, TempEntityTree::getOrderNo, BaseMenuDO::getIcon)
            .orderByDesc(TempEntityTree::getOrderNo).orderByAsc(TempEntity::getId).page(dto.pageOrder());

    }

    /**
     * 查询：树结构
     */
    @SneakyThrows
    @Override
    public List<BaseMenuDO> tree(BaseMenuPageDTO dto) {

        CountDownLatch countDownLatch = ThreadUtil.newCountDownLatch(1);

        CallBack<List<BaseMenuDO>> allListCallBack = new CallBack<>();

        MyThreadUtil.execute(() -> {

            allListCallBack.setValue(baseMapper.getMenuListByUserId(null));

        }, countDownLatch);

        // 根据条件进行筛选，得到符合条件的数据，然后再逆向生成整棵树，并返回这个树结构
        dto.setPageSize(-1); // 不分页
        List<BaseMenuDO> baseMenuDoList = myPage(dto).getRecords();

        countDownLatch.await();

        if (baseMenuDoList.size() == 0) {
            return new ArrayList<>();
        }

        if (allListCallBack.getValue().size() == 0) {
            return new ArrayList<>();
        }

        return MyTreeUtil.getFullTreeByDeepNode(baseMenuDoList, allListCallBack.getValue());

    }

    /**
     * 下拉树形列表
     */
    @Override
    public List<BaseMenuDO> dictTreeList() {

        List<BaseMenuDO> baseMenuDoList = lambdaQuery().eq(TempEntityNoId::getEnableFlag, true)
            .select(TempEntity::getId, TempEntityTree::getPid, BaseMenuDO::getName)
            .orderByDesc(TempEntityTree::getOrderNo).orderByAsc(TempEntity::getId).list();

        return MyTreeUtil.listToTree(baseMenuDoList);

    }

    /**
     * 通过主键id，查看详情
     */
    @Override
    public BaseMenuInfoByIdVO infoById(NotNullId notNullId) {

        BaseMenuDO baseMenuDO = lambdaQuery().eq(TempEntity::getId, notNullId.getId()).one();

        if (baseMenuDO == null) {
            return null;
        }

        BaseMenuInfoByIdVO baseMenuInfoByIdVO = BeanUtil.copyProperties(baseMenuDO, BaseMenuInfoByIdVO.class);

        // 设置：角色 idSet
        List<BaseRoleRefMenuDO> sysRoleRefMenuDOList =
            baseRoleRefMenuService.lambdaQuery().eq(BaseRoleRefMenuDO::getMenuId, notNullId.getId())
                .select(BaseRoleRefMenuDO::getRoleId).list();

        baseMenuInfoByIdVO.setRoleIdSet(
            sysRoleRefMenuDOList.stream().map(BaseRoleRefMenuDO::getRoleId).collect(Collectors.toSet()));

        // 处理：父级 id
        MyEntityUtil.handleParentId(baseMenuInfoByIdVO);

        return baseMenuInfoByIdVO;

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
        boolean exists = lambdaQuery().in(BaseMenuDO::getPid, idSet).exists();

        if (exists) {
            R.error(TempBizCodeEnum.PLEASE_DELETE_THE_CHILD_NODE_FIRST);
        }

        // 删除子表数据
        deleteByIdSetSub(idSet);

        removeByIds(idSet);

        return TempBizCodeEnum.OK;

    }

    /**
     * 获取：当前用户绑定的菜单
     */
    @Override
    public List<BaseMenuDO> userSelfMenuList() {

        Long userId = MyUserUtil.getCurrentUserId();

        if (MyUserUtil.getCurrentUserAdminFlag(userId)) {

            userId = null;

        }

        return baseMapper.getMenuListByUserId(userId);

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

        List<BaseMenuDO> baseMenuDoList =
            lambdaQuery().in(TempEntity::getId, dto.getIdSet()).select(TempEntity::getId, TempEntityTree::getOrderNo)
                .list();

        for (BaseMenuDO item : baseMenuDoList) {
            item.setOrderNo((int)(item.getOrderNo() + dto.getNumber()));
        }

        updateBatchById(baseMenuDoList);

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
