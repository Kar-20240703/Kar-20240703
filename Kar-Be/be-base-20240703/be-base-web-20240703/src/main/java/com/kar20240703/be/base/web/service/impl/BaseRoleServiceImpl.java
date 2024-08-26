package com.kar20240703.be.base.web.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kar20240703.be.base.web.exception.BaseBizCodeEnum;
import com.kar20240703.be.base.web.mapper.BaseRoleMapper;
import com.kar20240703.be.base.web.model.domain.BaseRoleDO;
import com.kar20240703.be.base.web.model.domain.BaseRoleRefAuthDO;
import com.kar20240703.be.base.web.model.domain.BaseRoleRefMenuDO;
import com.kar20240703.be.base.web.model.domain.BaseRoleRefUserDO;
import com.kar20240703.be.base.web.model.dto.BaseRoleInsertOrUpdateDTO;
import com.kar20240703.be.base.web.model.dto.BaseRolePageDTO;
import com.kar20240703.be.base.web.model.vo.BaseRoleInfoByIdVO;
import com.kar20240703.be.base.web.service.BaseRoleRefAuthService;
import com.kar20240703.be.base.web.service.BaseRoleRefMenuService;
import com.kar20240703.be.base.web.service.BaseRoleRefUserService;
import com.kar20240703.be.base.web.service.BaseRoleService;
import com.kar20240703.be.temp.web.exception.TempBizCodeEnum;
import com.kar20240703.be.temp.web.model.annotation.MyTransactional;
import com.kar20240703.be.temp.web.model.domain.TempEntity;
import com.kar20240703.be.temp.web.model.dto.NotEmptyIdSet;
import com.kar20240703.be.temp.web.model.dto.NotNullId;
import com.kar20240703.be.temp.web.model.vo.R;
import com.kar20240703.be.temp.web.util.MyEntityUtil;
import com.kar20240703.be.temp.web.util.MyMapUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class BaseRoleServiceImpl extends ServiceImpl<BaseRoleMapper, BaseRoleDO> implements BaseRoleService {

    @Resource
    BaseRoleRefUserService baseRoleRefUserService;

    @Resource
    BaseRoleRefMenuService baseRoleRefMenuService;

    @Resource
    BaseRoleRefAuthService baseRoleRefAuthService;

    /**
     * 新增/修改
     */
    @Override
    @MyTransactional
    public String insertOrUpdate(BaseRoleInsertOrUpdateDTO dto) {

        // 角色名，不能重复
        boolean exists =
            lambdaQuery().eq(BaseRoleDO::getName, dto.getName()).ne(dto.getId() != null, TempEntity::getId, dto.getId())
                .exists();

        if (exists) {
            R.error(BaseBizCodeEnum.THE_SAME_ROLE_NAME_EXIST);
        }

        // 如果是默认角色，则取消之前的默认角色
        if (BooleanUtil.isTrue(dto.getDefaultFlag())) {

            lambdaUpdate().set(BaseRoleDO::getDefaultFlag, false).eq(BaseRoleDO::getDefaultFlag, true)
                .ne(dto.getId() != null, TempEntity::getId, dto.getId()).update();

        }

        BaseRoleDO baseRoleDO = new BaseRoleDO();

        baseRoleDO.setName(dto.getName());
        baseRoleDO.setDefaultFlag(BooleanUtil.isTrue(dto.getDefaultFlag()));
        baseRoleDO.setEnableFlag(BooleanUtil.isTrue(dto.getEnableFlag()));
        baseRoleDO.setRemark(MyEntityUtil.getNotNullStr(dto.getRemark()));
        baseRoleDO.setId(dto.getId());

        if (dto.getId() != null) {

            deleteByIdSetSub(CollUtil.newHashSet(dto.getId())); // 先删除子表数据

        }

        saveOrUpdate(baseRoleDO);

        insertOrUpdateSub(dto, baseRoleDO); // 新增 子表数据

        return TempBizCodeEnum.OK;

    }

    /**
     * 新增/修改：新增 子表数据
     */
    private void insertOrUpdateSub(BaseRoleInsertOrUpdateDTO dto, BaseRoleDO baseRoleDO) {

        // 如果禁用了，则子表不进行新增操作
        if (BooleanUtil.isFalse(baseRoleDO.getEnableFlag())) {
            return;
        }

        Long roleId = baseRoleDO.getId();

        if (CollUtil.isNotEmpty(dto.getMenuIdSet())) {

            List<BaseRoleRefMenuDO> insertList =
                new ArrayList<>(MyMapUtil.getInitialCapacity(dto.getMenuIdSet().size()));

            for (Long menuId : dto.getMenuIdSet()) {

                BaseRoleRefMenuDO baseRoleRefMenuDO = new BaseRoleRefMenuDO();

                baseRoleRefMenuDO.setRoleId(roleId);
                baseRoleRefMenuDO.setMenuId(menuId);

                insertList.add(baseRoleRefMenuDO);

            }

            baseRoleRefMenuService.saveBatch(insertList);

        }

        if (CollUtil.isNotEmpty(dto.getUserIdSet())) {

            List<BaseRoleRefUserDO> insertList =
                new ArrayList<>(MyMapUtil.getInitialCapacity(dto.getUserIdSet().size()));

            for (Long userId : dto.getUserIdSet()) {

                BaseRoleRefUserDO baseRoleRefUserDO = new BaseRoleRefUserDO();

                baseRoleRefUserDO.setRoleId(roleId);
                baseRoleRefUserDO.setUserId(userId);

                insertList.add(baseRoleRefUserDO);

            }

            baseRoleRefUserService.saveBatch(insertList);

        }

        if (CollUtil.isNotEmpty(dto.getAuthIdSet())) {

            List<BaseRoleRefAuthDO> insertList =
                new ArrayList<>(MyMapUtil.getInitialCapacity(dto.getAuthIdSet().size()));

            for (Long authId : dto.getAuthIdSet()) {

                BaseRoleRefAuthDO baseRoleRefAuthDO = new BaseRoleRefAuthDO();

                baseRoleRefAuthDO.setRoleId(roleId);
                baseRoleRefAuthDO.setAuthId(authId);

                insertList.add(baseRoleRefAuthDO);

            }

            baseRoleRefAuthService.saveBatch(insertList);

        }

    }

    /**
     * 删除子表数据
     */
    private void deleteByIdSetSub(Set<Long> idSet) {

        // 删除：角色菜单关联表
        baseRoleRefMenuService.removeByIds(idSet);

        // 删除：角色用户关联表
        baseRoleRefUserService.removeByIds(idSet);

        // 删除：角色权限关联表
        baseRoleRefAuthService.removeByIds(idSet);

    }

    /**
     * 分页排序查询
     */
    @Override
    public Page<BaseRoleDO> myPage(BaseRolePageDTO dto) {

        return lambdaQuery().like(StrUtil.isNotBlank(dto.getName()), BaseRoleDO::getName, dto.getName())
            .like(StrUtil.isNotBlank(dto.getRemark()), TempEntity::getRemark, dto.getRemark())
            .eq(dto.getEnableFlag() != null, TempEntity::getEnableFlag, dto.getEnableFlag())
            .eq(dto.getDefaultFlag() != null, BaseRoleDO::getDefaultFlag, dto.getDefaultFlag())
            .orderByDesc(TempEntity::getUpdateTime).page(dto.pageOrder());

    }

    /**
     * 通过主键id，查看详情
     */
    @Override
    public BaseRoleInfoByIdVO infoById(NotNullId notNullId) {

        BaseRoleDO baseRoleDO = lambdaQuery().eq(TempEntity::getId, notNullId.getId()).one();

        if (baseRoleDO == null) {
            return null;
        }

        BaseRoleInfoByIdVO baseRoleInfoByIdVO = BeanUtil.copyProperties(baseRoleDO, BaseRoleInfoByIdVO.class);

        // 完善子表的数据
        List<BaseRoleRefMenuDO> menuList =
            baseRoleRefMenuService.lambdaQuery().eq(BaseRoleRefMenuDO::getRoleId, baseRoleInfoByIdVO.getId())
                .select(BaseRoleRefMenuDO::getMenuId).list();

        List<BaseRoleRefUserDO> userList =
            baseRoleRefUserService.lambdaQuery().eq(BaseRoleRefUserDO::getRoleId, baseRoleInfoByIdVO.getId())
                .select(BaseRoleRefUserDO::getUserId).list();

        List<BaseRoleRefAuthDO> authList =
            baseRoleRefAuthService.lambdaQuery().eq(BaseRoleRefAuthDO::getRoleId, baseRoleInfoByIdVO.getId())
                .select(BaseRoleRefAuthDO::getAuthId).list();

        baseRoleInfoByIdVO.setMenuIdSet(
            menuList.stream().map(BaseRoleRefMenuDO::getMenuId).collect(Collectors.toSet()));
        baseRoleInfoByIdVO.setUserIdSet(
            userList.stream().map(BaseRoleRefUserDO::getUserId).collect(Collectors.toSet()));
        baseRoleInfoByIdVO.setAuthIdSet(
            authList.stream().map(BaseRoleRefAuthDO::getAuthId).collect(Collectors.toSet()));

        return baseRoleInfoByIdVO;

    }

    /**
     * 批量删除
     */
    @Override
    @MyTransactional
    public String deleteByIdSet(NotEmptyIdSet notEmptyIdSet) {

        if (CollUtil.isEmpty(notEmptyIdSet.getIdSet())) {
            return TempBizCodeEnum.OK;
        }

        deleteByIdSetSub(notEmptyIdSet.getIdSet()); // 删除子表数据

        removeByIds(notEmptyIdSet.getIdSet());

        return TempBizCodeEnum.OK;

    }

}
