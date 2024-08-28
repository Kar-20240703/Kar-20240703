package com.kar20240703.be.base.web.service.impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.DesensitizedUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.ChainWrappers;
import com.kar20240703.be.base.web.mapper.BaseRoleRefUserMapper;
import com.kar20240703.be.base.web.mapper.BaseUserInfoMapper;
import com.kar20240703.be.base.web.mapper.BaseUserMapper;
import com.kar20240703.be.base.web.model.domain.BaseRoleRefUserDO;
import com.kar20240703.be.base.web.model.dto.BaseUserDictListDTO;
import com.kar20240703.be.base.web.model.dto.BaseUserInsertOrUpdateDTO;
import com.kar20240703.be.base.web.model.dto.BaseUserPageDTO;
import com.kar20240703.be.base.web.model.dto.BaseUserUpdatePasswordDTO;
import com.kar20240703.be.base.web.model.enums.BaseRedisKeyEnum;
import com.kar20240703.be.base.web.model.vo.BaseUserInfoByIdVO;
import com.kar20240703.be.base.web.model.vo.BaseUserPageVO;
import com.kar20240703.be.base.web.service.BaseUserService;
import com.kar20240703.be.temp.web.model.constant.ParamConstant;
import com.kar20240703.be.temp.web.model.domain.TempUserDO;
import com.kar20240703.be.temp.web.model.dto.NotEmptyIdSet;
import com.kar20240703.be.temp.web.model.dto.NotNullId;
import com.kar20240703.be.temp.web.model.vo.DictVO;
import com.kar20240703.be.temp.web.util.CallBack;
import com.kar20240703.be.temp.web.util.MyMapUtil;
import com.kar20240703.be.temp.web.util.MyThreadUtil;
import com.kar20240703.be.temp.web.util.MyParamUtil;
import com.kar20240703.be.temp.web.util.MyUserUtil;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import lombok.SneakyThrows;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

@Service
public class BaseUserServiceImpl extends ServiceImpl<BaseUserMapper, TempUserDO> implements BaseUserService {

    @Resource
    BaseRoleRefUserMapper baseRoleRefUserMapper;

    private static RedissonClient redissonClient;

    @Resource
    public void setRedissonClient(RedissonClient redissonClient) {
        BaseUserServiceImpl.redissonClient = redissonClient;
    }

    @Resource
    BaseUserInfoMapper baseUserInfoMapper;

    /**
     * 分页排序查询
     */
    @Override
    public Page<BaseUserPageVO> myPage(BaseUserPageDTO dto) {

        Page<BaseUserPageVO> dtoPage = dto.pageOrder(false);

        // 备注：mysql 是先 group by 再 order by
        Page<BaseUserPageVO> page = baseMapper.myPage(dtoPage, dto);

        Set<Long> userIdSet = new HashSet<>(MyMapUtil.getInitialCapacity(page.getRecords().size()));

        for (BaseUserPageVO item : page.getRecords()) {

            // 备注：要和 userSelfInfo接口保持一致
            item.setEmail(DesensitizedUtil.email(item.getEmail())); // 脱敏
            item.setUsername(DesensitizedUtil.chineseName(item.getUsername())); // 脱敏
            item.setPhone(DesensitizedUtil.mobilePhone(item.getPhone())); // 脱敏
            item.setWxOpenId(StrUtil.hide(item.getWxOpenId(), 3, item.getWxOpenId().length() - 4)); // 脱敏：只显示前 3位，后 4位
            item.setWxAppId(StrUtil.hide(item.getWxAppId(), 3, item.getWxAppId().length() - 4)); // 脱敏：只显示前 3位，后 4位

            userIdSet.add(item.getId());

        }

        if (userIdSet.size() != 0) {

            // 处理：关联的数据
            handleRefData(page, userIdSet);

        }

        return page;

    }

    /**
     * 处理：关联的数据
     */
    @SneakyThrows
    private void handleRefData(Page<BaseUserPageVO> page, Set<Long> userIdSet) {

        CountDownLatch countDownLatch = ThreadUtil.newCountDownLatch(1);

        CallBack<Map<Long, Set<Long>>> userIdRefRoleIdSetCallBack = new CallBack<>();

        MyThreadUtil.execute(() -> {

            Map<Long, Set<Long>> userIdRefRoleIdSetMap =
                ChainWrappers.lambdaQueryChain(baseRoleRefUserMapper).in(BaseRoleRefUserDO::getUserId, userIdSet)
                    .select(BaseRoleRefUserDO::getUserId, BaseRoleRefUserDO::getRoleId).list().stream().collect(
                        Collectors.groupingBy(BaseRoleRefUserDO::getUserId,
                            Collectors.mapping(BaseRoleRefUserDO::getRoleId, Collectors.toSet())));

            userIdRefRoleIdSetCallBack.setValue(userIdRefRoleIdSetMap);

        }, countDownLatch);

        countDownLatch.await();

        page.getRecords().forEach(it -> {

            it.setRoleIdSet(userIdRefRoleIdSetCallBack.getValue().get(it.getId()));

            // 获取
            Boolean manageSignInFlag = getManageSignInFlag(it.getId());

            it.setManageSignInFlag(manageSignInFlag);

        });

    }

    /**
     * 获取：是否允许后台登录
     */
    public static boolean getManageSignInFlag(Long userId) {

        if (MyUserUtil.getCurrentUserAdminFlag(userId)) {
            return true;
        }

        Boolean manageSignInFlag =
            redissonClient.<Boolean>getBucket(BaseRedisKeyEnum.PRE_USER_MANAGE_SIGN_IN_FLAG.name() + ":" + userId)
                .get();

        if (manageSignInFlag == null) {

            String defaultManageSignInFlagStr = MyParamUtil.getValueByUuid(ParamConstant.DEFAULT_MANAGE_SIGN_IN_FLAG);

            return Convert.toBool(defaultManageSignInFlagStr, false);

        }

        return manageSignInFlag;

    }

    /**
     * 下拉列表
     */
    @Override
    public Page<DictVO> dictList(BaseUserDictListDTO dto) {

        // 获取所有：用户信息
        List<SysUserInfoDO> sysUserInfoDOList = ChainWrappers.lambdaQueryChain(sysUserInfoMapper)
            .select(SysUserInfoDO::getId, SysUserInfoDO::getNickname, SysUserInfoDO::getTenantId)
            .orderByDesc(SysUserInfoDO::getId).list();

        return null;

    }

    /**
     * 新增/修改
     */
    @Override
    public String insertOrUpdate(BaseUserInsertOrUpdateDTO dto) {
        return "";
    }

    /**
     * 通过主键id，查看详情
     */
    @Override
    public BaseUserInfoByIdVO infoById(NotNullId notNullId) {
        return null;
    }

    /**
     * 是否允许后台登录
     */
    @Override
    public Boolean manageSignInFlag() {
        return null;
    }

    /**
     * 批量：注销用户
     */
    @Override
    public String deleteByIdSet(NotEmptyIdSet notEmptyIdSet) {
        return "";
    }

    /**
     * 批量：重置头像
     */
    @Override
    public String resetAvatar(NotEmptyIdSet notEmptyIdSet) {
        return "";
    }

    /**
     * 批量：修改密码
     */
    @Override
    public String updatePassword(BaseUserUpdatePasswordDTO dto) {
        return "";
    }

    /**
     * 批量：解冻
     */
    @Override
    public String thaw(NotEmptyIdSet notEmptyIdSet) {
        return "";
    }

    /**
     * 批量：冻结
     */
    @Override
    public String freeze(NotEmptyIdSet notEmptyIdSet) {
        return "";
    }

}
