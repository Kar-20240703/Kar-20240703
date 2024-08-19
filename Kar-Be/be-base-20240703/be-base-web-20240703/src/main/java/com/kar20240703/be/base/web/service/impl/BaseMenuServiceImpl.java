package com.kar20240703.be.base.web.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kar20240703.be.base.web.exception.BaseBizCodeEnum;
import com.kar20240703.be.base.web.mapper.BaseMenuMapper;
import com.kar20240703.be.base.web.model.domain.BaseMenuDO;
import com.kar20240703.be.base.web.model.domain.BaseMenuRefUserDO;
import com.kar20240703.be.base.web.model.domain.BaseRoleRefMenuDO;
import com.kar20240703.be.base.web.model.domain.BaseRoleRefUserDO;
import com.kar20240703.be.base.web.model.dto.BaseMenuInsertOrUpdateDTO;
import com.kar20240703.be.base.web.model.dto.BaseMenuPageDTO;
import com.kar20240703.be.base.web.service.BaseMenuRefUserService;
import com.kar20240703.be.base.web.service.BaseMenuService;
import com.kar20240703.be.base.web.service.BaseRoleRefMenuService;
import com.kar20240703.be.base.web.service.BaseRoleRefUserService;
import com.kar20240703.be.temp.web.exception.TempBizCodeEnum;
import com.kar20240703.be.temp.web.model.domain.TempEntity;
import com.kar20240703.be.temp.web.model.dto.ChangeNumberDTO;
import com.kar20240703.be.temp.web.model.dto.NotEmptyIdSet;
import com.kar20240703.be.temp.web.model.dto.NotNullId;
import com.kar20240703.be.temp.web.model.vo.R;
import com.kar20240703.be.temp.web.util.MyEntityUtil;
import com.kar20240703.be.temp.web.util.UserUtil;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BaseMenuServiceImpl extends ServiceImpl<BaseMenuMapper, BaseMenuDO> implements BaseMenuService {

    @Resource
    BaseRoleRefMenuService baseRoleRefMenuService;

    @Resource
    BaseRoleRefUserService baseRoleRefUserService;

    @Resource
    BaseMenuRefUserService baseMenuRefUserService;

    /**
     * 新增/修改
     */
    @Override
    @Transactional
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
                R.error(BaseBizCodeEnum.MENU_UUID_IS_EXIST);
            }

        }

        if (dto.getId() != null) {
            deleteByIdSetSub(CollUtil.newHashSet(dto.getId())); // 先删除：子表数据
        }

        BaseMenuDO sysMenuDO = getDoByDto(dto);

        saveOrUpdate(sysMenuDO);

        insertOrUpdateSub(sysMenuDO, dto); // 新增 子表数据

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

        baseMenuDO.setUuid(MyEntityUtil.getNotNullStr(dto.getUuid()));

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

            List<BaseRoleRefUserDO> baseRoleRefUserDoList =
                baseRoleRefUserService.lambdaQuery().in(BaseRoleRefUserDO::getRoleId, dto.getRoleIdSet())
                    .select(BaseRoleRefUserDO::getUserId).list();

            if (CollUtil.isNotEmpty(baseRoleRefUserDoList)) {

                List<BaseMenuRefUserDO> baseMenuRefUserDoList =
                    baseRoleRefUserDoList.stream().map(it -> new BaseMenuRefUserDO(dto.getId(), it.getUserId()))
                        .collect(Collectors.toList());

                baseMenuRefUserService.saveBatch(baseMenuRefUserDoList);

            }

        }

    }

    /**
     * 删除子表数据
     */
    private void deleteByIdSetSub(Set<Long> idSet) {

        // 删除：角色菜单关联表
        baseRoleRefMenuService.lambdaUpdate().in(BaseRoleRefMenuDO::getMenuId, idSet).remove();

        // 删除：菜单用户关联表
        baseRoleRefMenuService.lambdaUpdate().in(BaseRoleRefMenuDO::getMenuId, idSet).remove();

    }

    /**
     * 分页排序查询
     */
    @Override
    public Page<BaseMenuDO> myPage(BaseMenuPageDTO dto) {
        return null;
    }

    /**
     * 查询：树结构
     */
    @Override
    public List<BaseMenuDO> tree(BaseMenuPageDTO dto) {
        return Collections.emptyList();
    }

    /**
     * 通过主键id，查看详情
     */
    @Override
    public BaseMenuDO infoById(NotNullId notNullId) {
        return null;
    }

    /**
     * 批量删除
     */
    @Override
    public String deleteByIdSet(NotEmptyIdSet notEmptyIdSet, boolean checkChildrenFlag, boolean checkDeleteFlag) {
        return "";
    }

    /**
     * 获取：当前用户绑定的菜单
     */
    @Override
    public List<BaseMenuDO> userSelfMenuList() {

        Long userId = UserUtil.getCurrentUserId();

        if (UserUtil.getCurrentUserAdminFlag(userId)) {

            userId = null;

        }

        return baseMapper.getMenuListByUserId(userId);

    }

    /**
     * 通过主键 idSet，加减排序号
     */
    @Override
    public String addOrderNo(ChangeNumberDTO dto) {
        return "";
    }

}
