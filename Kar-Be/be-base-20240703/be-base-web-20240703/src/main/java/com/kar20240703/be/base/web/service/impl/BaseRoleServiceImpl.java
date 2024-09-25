package com.kar20240703.be.base.web.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.ChainWrappers;
import com.kar20240703.be.base.web.exception.BaseBizCodeEnum;
import com.kar20240703.be.base.web.mapper.BaseAuthMapper;
import com.kar20240703.be.base.web.mapper.BaseRoleMapper;
import com.kar20240703.be.base.web.model.domain.BaseAuthDO;
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
import com.kar20240703.be.temp.web.model.domain.TempEntityNoId;
import com.kar20240703.be.temp.web.model.dto.NotEmptyIdSet;
import com.kar20240703.be.temp.web.model.dto.NotNullId;
import com.kar20240703.be.temp.web.model.enums.TempRedisKeyEnum;
import com.kar20240703.be.temp.web.model.vo.DictVO;
import com.kar20240703.be.temp.web.model.vo.R;
import com.kar20240703.be.temp.web.util.MyEntityUtil;
import com.kar20240703.be.temp.web.util.MyMapUtil;
import com.kar20240703.be.temp.web.util.TransactionUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import javax.annotation.Resource;
import org.redisson.api.RBatch;
import org.redisson.api.RSet;
import org.redisson.api.RSetAsync;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

@Service
public class BaseRoleServiceImpl extends ServiceImpl<BaseRoleMapper, BaseRoleDO> implements BaseRoleService {

    private static BaseRoleRefUserService baseRoleRefUserService;

    @Resource
    public void setBaseRoleRefUserService(BaseRoleRefUserService baseRoleRefUserService) {
        BaseRoleServiceImpl.baseRoleRefUserService = baseRoleRefUserService;
    }

    @Resource
    BaseRoleRefMenuService baseRoleRefMenuService;

    private static BaseRoleRefAuthService baseRoleRefAuthService;

    @Resource
    public void setBaseRoleRefAuthService(BaseRoleRefAuthService baseRoleRefAuthService) {
        BaseRoleServiceImpl.baseRoleRefAuthService = baseRoleRefAuthService;
    }

    private static RedissonClient redissonClient;

    @Resource
    public void setRedissonClient(RedissonClient redissonClient) {
        BaseRoleServiceImpl.redissonClient = redissonClient;
    }

    private static BaseAuthMapper baseAuthMapper;

    @Resource
    public void setBaseAuthMapper(BaseAuthMapper baseAuthMapper) {
        BaseRoleServiceImpl.baseAuthMapper = baseAuthMapper;
    }

    private static BaseRoleMapper baseRoleMapper;

    @Resource
    public void setBaseRoleMapper(BaseRoleMapper baseRoleMapper) {
        BaseRoleServiceImpl.baseRoleMapper = baseRoleMapper;
    }

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

        // uuid不能重复
        if (StrUtil.isNotBlank(dto.getUuid())) {

            exists = lambdaQuery().eq(BaseRoleDO::getUuid, dto.getUuid())
                .ne(dto.getId() != null, TempEntity::getId, dto.getId()).exists();

            if (exists) {
                R.error(BaseBizCodeEnum.UUID_IS_EXIST);
            }

        }

        // 如果是默认角色，则取消之前的默认角色
        if (BooleanUtil.isTrue(dto.getDefaultFlag())) {

            lambdaUpdate().set(BaseRoleDO::getDefaultFlag, false).eq(BaseRoleDO::getDefaultFlag, true)
                .ne(dto.getId() != null, TempEntity::getId, dto.getId()).update();

        }

        BaseRoleDO baseRoleDO = new BaseRoleDO();

        baseRoleDO.setName(dto.getName());
        baseRoleDO.setUuid(MyEntityUtil.getNotNullStr(dto.getUuid(), IdUtil.simpleUUID()));

        baseRoleDO.setDefaultFlag(BooleanUtil.isTrue(dto.getDefaultFlag()));
        baseRoleDO.setEnableFlag(BooleanUtil.isTrue(dto.getEnableFlag()));
        baseRoleDO.setRemark(MyEntityUtil.getNotNullStr(dto.getRemark()));
        baseRoleDO.setId(dto.getId());

        Set<Long> oldUserIdSet = new HashSet<>();

        if (dto.getId() != null) {

            oldUserIdSet = baseRoleRefUserService.lambdaQuery().eq(BaseRoleRefUserDO::getRoleId, dto.getId())
                .select(BaseRoleRefUserDO::getUserId).list().stream().map(BaseRoleRefUserDO::getUserId)
                .collect(Collectors.toSet());

            deleteByIdSetSub(CollUtil.newHashSet(dto.getId())); // 先删除子表数据

        }

        saveOrUpdate(baseRoleDO);

        insertOrUpdateSub(dto, baseRoleDO); // 新增 子表数据

        updateCache(dto, oldUserIdSet, null); // 更新缓存

        return TempBizCodeEnum.OK;

    }

    /**
     * 更新缓存
     */
    public static void updateCache(@Nullable BaseRoleInsertOrUpdateDTO dto, Set<Long> userIdSet,
        @Nullable Set<Long> roleIdSet) {

        TransactionUtil.exec(() -> {

            if (dto == null) {

                if (CollUtil.isNotEmpty(roleIdSet)) {

                    BaseRoleDO baseRoleDO =
                        ChainWrappers.lambdaQueryChain(baseRoleMapper).in(TempEntity::getId, roleIdSet)
                            .eq(BaseRoleDO::getDefaultFlag, true).select(TempEntity::getId).one();

                    if (baseRoleDO != null) {

                        Set<Long> authIdSet =
                            baseRoleRefAuthService.lambdaQuery().eq(BaseRoleRefAuthDO::getRoleId, baseRoleDO.getId())
                                .select(BaseRoleRefAuthDO::getAuthId).list().stream().map(BaseRoleRefAuthDO::getAuthId)
                                .collect(Collectors.toSet());

                        // 更新：默认缓存
                        updateCacheForDefault(authIdSet);

                    }

                }

            } else {

                if (BooleanUtil.isTrue(dto.getDefaultFlag())) {

                    // 更新：默认缓存
                    updateCacheForDefault(dto.getAuthIdSet());

                }

                if (CollUtil.isNotEmpty(dto.getUserIdSet())) {

                    userIdSet.addAll(dto.getUserIdSet());

                }

            }

            if (CollUtil.isEmpty(userIdSet)) {
                return;
            }

            // 通过：userIdSet，更新缓存
            updateCacheByUserIdSet(userIdSet);

        });

    }

    /**
     * 通过：userIdSet，更新缓存
     */
    private static void updateCacheByUserIdSet(Set<Long> userIdSet) {

        List<BaseRoleRefUserDO> baseRoleRefUserDoList =
            baseRoleRefUserService.lambdaQuery().in(BaseRoleRefUserDO::getUserId, userIdSet)
                .select(BaseRoleRefUserDO::getRoleId, BaseRoleRefUserDO::getUserId).list();

        Set<Long> roleIdSet = new HashSet<>();

        Map<Long, Set<Long>> userIdRefRoleIdSetMap = new HashMap<>();

        for (BaseRoleRefUserDO item : baseRoleRefUserDoList) {

            Long userId = item.getUserId();

            Long roleId = item.getRoleId();

            roleIdSet.add(roleId);

            Set<Long> userIdRefRoleIdSet = userIdRefRoleIdSetMap.computeIfAbsent(userId, k -> new HashSet<>());

            userIdRefRoleIdSet.add(roleId);

        }

        Set<Long> authIdSet = new HashSet<>();

        Map<Long, Set<Long>> roleIdRefAuthIdSetMap = new HashMap<>();

        if (CollUtil.isNotEmpty(roleIdSet)) {

            List<BaseRoleRefAuthDO> baseRoleRefAuthDoList =
                baseRoleRefAuthService.lambdaQuery().in(BaseRoleRefAuthDO::getRoleId, roleIdSet)
                    .select(BaseRoleRefAuthDO::getRoleId, BaseRoleRefAuthDO::getAuthId).list();

            for (BaseRoleRefAuthDO item : baseRoleRefAuthDoList) {

                Long authId = item.getAuthId();

                Long roleId = item.getRoleId();

                authIdSet.add(authId);

                Set<Long> roleIdRefAuthIdSet = roleIdRefAuthIdSetMap.computeIfAbsent(roleId, k -> new HashSet<>());

                roleIdRefAuthIdSet.add(authId);

            }

        }

        Map<Long, String> authMap = new HashMap<>();

        if (CollUtil.isNotEmpty(authIdSet)) {

            authMap = ChainWrappers.lambdaQueryChain(baseAuthMapper).in(TempEntity::getId, authIdSet)
                .select(TempEntity::getId, BaseAuthDO::getAuth).list().stream()
                .collect(Collectors.toMap(TempEntity::getId, BaseAuthDO::getAuth));

        }

        RBatch rBatch = redissonClient.createBatch();

        for (Long userId : userIdSet) {

            Set<Long> userIdRefRoleIdSet = userIdRefRoleIdSetMap.get(userId);

            RSetAsync<String> rBatchSet = rBatch.getSet(TempRedisKeyEnum.PRE_USER_AUTH.name() + ":" + userId);

            rBatchSet.deleteAsync();

            if (CollUtil.isEmpty(userIdRefRoleIdSet)) {
                continue;
            }

            Set<String> authSet = new HashSet<>();

            for (Long roleId : userIdRefRoleIdSet) {

                Set<Long> roleIdRefAuthIdSet = roleIdRefAuthIdSetMap.get(roleId);

                if (CollUtil.isEmpty(roleIdRefAuthIdSet)) {
                    continue;
                }

                for (Long authId : roleIdRefAuthIdSet) {

                    String auth = authMap.get(authId);

                    if (StrUtil.isNotBlank(auth)) {
                        authSet.add(auth);
                    }

                }

            }

            rBatchSet.addAllAsync(authSet);

        }

        rBatch.execute();

    }

    /**
     * 更新：默认缓存
     */
    private static void updateCacheForDefault(Set<Long> authIdSet) {

        RSet<String> rSet = redissonClient.getSet(TempRedisKeyEnum.DEFAULT_USER_AUTH_CACHE.name());

        rSet.delete();

        if (CollUtil.isEmpty(authIdSet)) {
            return;
        }

        Set<String> authSet =
            ChainWrappers.lambdaQueryChain(baseAuthMapper).in(TempEntity::getId, authIdSet).select(BaseAuthDO::getAuth)
                .list().stream().map(BaseAuthDO::getAuth).collect(Collectors.toSet());

        rSet.addAll(authSet);

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
     * 下拉列表
     */
    @Override
    public Page<DictVO> dictList() {

        List<DictVO> dictVOList =
            lambdaQuery().eq(TempEntityNoId::getEnableFlag, true).select(TempEntity::getId, BaseRoleDO::getName).list()
                .stream().map(it -> new DictVO(it.getId(), it.getName())).collect(Collectors.toList());

        return new Page<DictVO>().setTotal(dictVOList.size()).setRecords(dictVOList);

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

        Set<Long> userIdSet =
            baseRoleRefUserService.lambdaQuery().in(BaseRoleRefUserDO::getRoleId, notEmptyIdSet.getIdSet())
                .select(BaseRoleRefUserDO::getUserId).list().stream().map(BaseRoleRefUserDO::getUserId)
                .collect(Collectors.toSet());

        deleteByIdSetSub(notEmptyIdSet.getIdSet()); // 删除子表数据

        removeByIds(notEmptyIdSet.getIdSet());

        updateCache(null, userIdSet, notEmptyIdSet.getIdSet()); // 更新缓存

        return TempBizCodeEnum.OK;

    }

}
