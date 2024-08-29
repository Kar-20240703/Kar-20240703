package com.kar20240703.be.base.web.service.impl;

import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.DesensitizedUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.toolkit.ChainWrappers;
import com.kar20240703.be.base.web.mapper.BaseUserInfoMapper;
import com.kar20240703.be.base.web.mapper.BaseUserMapper;
import com.kar20240703.be.base.web.model.dto.BaseUserSelfUpdateInfoDTO;
import com.kar20240703.be.base.web.model.vo.BaseUserSelfInfoVO;
import com.kar20240703.be.base.web.service.BaseUserSelfService;
import com.kar20240703.be.temp.web.exception.TempBizCodeEnum;
import com.kar20240703.be.temp.web.model.constant.TempConstant;
import com.kar20240703.be.temp.web.model.domain.TempEntity;
import com.kar20240703.be.temp.web.model.domain.TempUserDO;
import com.kar20240703.be.temp.web.model.domain.TempUserInfoDO;
import com.kar20240703.be.temp.web.properties.TempSecurityProperties;
import com.kar20240703.be.temp.web.util.MyEntityUtil;
import com.kar20240703.be.temp.web.util.MyThreadUtil;
import com.kar20240703.be.temp.web.util.MyUserUtil;
import com.kar20240703.be.temp.web.util.NicknameUtil;
import java.util.concurrent.CountDownLatch;
import javax.annotation.Resource;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

@Service
public class BaseUserSelfServiceImpl implements BaseUserSelfService {

    @Resource
    TempSecurityProperties tempSecurityProperties;

    @Resource
    BaseUserInfoMapper baseUserInfoMapper;

    @Resource
    BaseUserMapper baseUserMapper;

    /**
     * 获取：当前用户，基本信息
     */
    @SneakyThrows
    @Override
    public BaseUserSelfInfoVO userSelfInfo() {

        Long currentUserId = MyUserUtil.getCurrentUserId();

        BaseUserSelfInfoVO baseUserSelfInfoVO = new BaseUserSelfInfoVO();

        baseUserSelfInfoVO.setId(currentUserId);

        if (MyUserUtil.getCurrentUserAdminFlag(currentUserId)) {

            baseUserSelfInfoVO.setAvatarFileId(TempConstant.SYS_ID);
            baseUserSelfInfoVO.setNickname(tempSecurityProperties.getAdminNickname());
            baseUserSelfInfoVO.setBio("");
            baseUserSelfInfoVO.setEmail("");
            baseUserSelfInfoVO.setPasswordFlag(true);

            return baseUserSelfInfoVO;

        }

        CountDownLatch countDownLatch = ThreadUtil.newCountDownLatch(2);

        MyThreadUtil.execute(() -> {

            TempUserInfoDO tempUserInfoDO =
                ChainWrappers.lambdaQueryChain(baseUserInfoMapper).eq(TempUserInfoDO::getId, currentUserId)
                    .select(TempUserInfoDO::getAvatarFileId, TempUserInfoDO::getNickname, TempUserInfoDO::getBio).one();

            if (tempUserInfoDO != null) {

                baseUserSelfInfoVO.setAvatarFileId(tempUserInfoDO.getAvatarFileId());
                baseUserSelfInfoVO.setNickname(tempUserInfoDO.getNickname());
                baseUserSelfInfoVO.setBio(tempUserInfoDO.getBio());

            }

        }, countDownLatch);

        MyThreadUtil.execute(() -> {

            TempUserDO tempUserDO = ChainWrappers.lambdaQueryChain(baseUserMapper).eq(TempEntity::getId, currentUserId)
                .select(TempUserDO::getEmail, TempUserDO::getPassword, TempUserDO::getUsername, TempUserDO::getPhone,
                    TempUserDO::getWxOpenId, TempUserDO::getCreateTime, TempUserDO::getWxAppId).one();

            if (tempUserDO != null) {

                // 备注：要和 userMyPage接口保持一致
                baseUserSelfInfoVO.setEmail(DesensitizedUtil.email(tempUserDO.getEmail())); // 脱敏
                baseUserSelfInfoVO.setUsername(DesensitizedUtil.chineseName(tempUserDO.getUsername())); // 脱敏
                baseUserSelfInfoVO.setPhone(DesensitizedUtil.mobilePhone(tempUserDO.getPhone())); // 脱敏
                // 脱敏：只显示前 3位，后 4位
                baseUserSelfInfoVO.setWxOpenId(
                    StrUtil.hide(tempUserDO.getWxOpenId(), 3, tempUserDO.getWxOpenId().length() - 4));
                // 脱敏：只显示前 3位，后 4位
                baseUserSelfInfoVO.setWxAppId(
                    StrUtil.hide(tempUserDO.getWxAppId(), 3, tempUserDO.getWxAppId().length() - 4));

                baseUserSelfInfoVO.setPasswordFlag(StrUtil.isNotBlank(tempUserDO.getPassword()));
                baseUserSelfInfoVO.setCreateTime(tempUserDO.getCreateTime());

            }

        }, countDownLatch);

        countDownLatch.await();

        return baseUserSelfInfoVO;

    }

    /**
     * 当前用户：基本信息：修改
     */
    @Override
    public String userSelfUpdateInfo(BaseUserSelfUpdateInfoDTO dto) {

        Long currentUserIdNotAdmin = MyUserUtil.getCurrentUserIdNotAdmin();

        TempUserInfoDO tempUserInfoDO = new TempUserInfoDO();

        tempUserInfoDO.setId(currentUserIdNotAdmin);
        tempUserInfoDO.setNickname(MyEntityUtil.getNotNullStr(dto.getNickname(), NicknameUtil.getRandomNickname()));
        tempUserInfoDO.setBio(MyEntityUtil.getNotNullAndTrimStr(dto.getBio()));

        baseUserInfoMapper.updateById(tempUserInfoDO);

        return TempBizCodeEnum.OK;

    }

    /**
     * 当前用户：重置头像
     */
    @Override
    public String userSelfResetAvatar() {

        Long currentUserIdNotAdmin = MyUserUtil.getCurrentUserIdNotAdmin();

        ChainWrappers.lambdaUpdateChain(baseUserInfoMapper).eq(TempUserInfoDO::getId, currentUserIdNotAdmin)
            .set(TempUserInfoDO::getAvatarFileId, -1).update();

        return TempBizCodeEnum.OK;

    }

}
