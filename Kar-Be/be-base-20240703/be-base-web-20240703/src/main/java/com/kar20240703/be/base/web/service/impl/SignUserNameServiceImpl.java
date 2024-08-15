package com.kar20240703.be.base.web.service.impl;

import cn.hutool.core.util.BooleanUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.jwt.JWT;
import com.baomidou.mybatisplus.extension.toolkit.ChainWrappers;
import com.kar20240703.be.base.web.mapper.BaseUserMapper;
import com.kar20240703.be.base.web.model.domain.BaseUserConfigurationDO;
import com.kar20240703.be.base.web.model.dto.SignUserNameJwtRefreshTokenDTO;
import com.kar20240703.be.base.web.model.dto.SignUserNameSignDeleteDTO;
import com.kar20240703.be.base.web.model.dto.SignUserNameSignInPasswordDTO;
import com.kar20240703.be.base.web.model.dto.SignUserNameSignUpDTO;
import com.kar20240703.be.base.web.model.dto.SignUserNameUpdatePasswordDTO;
import com.kar20240703.be.base.web.model.dto.SignUserNameUpdateUserNameDTO;
import com.kar20240703.be.base.web.model.enums.BaseRedisKeyEnum;
import com.kar20240703.be.base.web.service.BaseUserConfigurationService;
import com.kar20240703.be.base.web.service.SignUserNameService;
import com.kar20240703.be.base.web.util.BaseJwtUtil;
import com.kar20240703.be.base.web.util.SignUtil;
import com.kar20240703.be.temp.web.exception.TempBizCodeEnum;
import com.kar20240703.be.temp.web.model.constant.UserConfigurationConstant;
import com.kar20240703.be.temp.web.model.domain.TempUserDO;
import com.kar20240703.be.temp.web.model.vo.R;
import com.kar20240703.be.temp.web.model.vo.SignInVO;
import com.kar20240703.be.temp.web.util.MyJwtUtil;
import com.kar20240703.be.temp.web.util.RequestUtil;
import javax.annotation.Resource;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

@Service
public class SignUserNameServiceImpl implements SignUserNameService {

    private static final BaseRedisKeyEnum PRE_REDIS_KEY_ENUM = BaseRedisKeyEnum.PRE_USER_NAME;

    @Resource
    BaseUserConfigurationService baseUserConfigurationService;

    @Resource
    BaseUserMapper baseUserMapper;

    @Resource
    RedissonClient redissonClient;

    /**
     * 注册
     */
    @Override
    public String signUp(SignUserNameSignUpDTO dto) {

        BaseUserConfigurationDO baseUserConfigurationDO =
            baseUserConfigurationService.getById(UserConfigurationConstant.ID);

        if (BooleanUtil.isFalse(baseUserConfigurationDO.getUserNameSignUpEnable())) {
            R.errorMsg("操作失败：不允许用户名注册，请联系管理员");
        }

        return SignUtil.signUp(dto.getPassword(), dto.getOriginPassword(), null, PRE_REDIS_KEY_ENUM, dto.getUsername());

    }

    /**
     * 账号密码登录
     */
    @Override
    public SignInVO signInPassword(SignUserNameSignInPasswordDTO dto) {

        return SignUtil.signInPassword(
            ChainWrappers.lambdaQueryChain(baseUserMapper).eq(TempUserDO::getUsername, dto.getUsername()),
            dto.getPassword(), dto.getUsername(), RequestUtil.getRequestCategoryEnum());

    }

    /**
     * 修改密码
     */
    @Override
    public String updatePassword(SignUserNameUpdatePasswordDTO dto) {

        SignUtil.checkWillError(PRE_REDIS_KEY_ENUM, null, null); // 检查：是否可以进行操作

        return SignUtil.updatePassword(dto.getNewPassword(), dto.getOriginNewPassword(), PRE_REDIS_KEY_ENUM, null,
            dto.getOldPassword());

    }

    /**
     * 修改用户名
     */
    @Override
    public String updateUserName(SignUserNameUpdateUserNameDTO dto) {

        SignUtil.checkWillError(PRE_REDIS_KEY_ENUM, null, null); // 检查：是否可以进行操作

        return SignUtil.updateAccount(null, null, PRE_REDIS_KEY_ENUM, PRE_REDIS_KEY_ENUM, dto.getNewUserName(),
            dto.getCurrentPassword(), null);

    }

    /**
     * 账号注销
     */
    @Override
    public String signDelete(SignUserNameSignDeleteDTO dto) {

        SignUtil.checkWillError(PRE_REDIS_KEY_ENUM, null, null); // 检查：是否可以进行操作

        return SignUtil.signDelete(null, PRE_REDIS_KEY_ENUM, dto.getCurrentPassword(), null);

    }

    /**
     * 刷新token
     */
    @Override
    public SignInVO jwtRefreshToken(SignUserNameJwtRefreshTokenDTO dto) {

        JWT jwtRefreshToken = JWT.of(dto.getJwtRefreshToken());

        JSONObject claimsJson = jwtRefreshToken.getPayload().getClaimsJson();

        // 获取：userId的值
        Long userId = MyJwtUtil.getPayloadMapUserIdValue(claimsJson);

        String jwtRefreshTokenRedisKey = MyJwtUtil.generateRedisJwtRefreshToken(dto.getJwtRefreshToken(), userId);

        boolean exists = redissonClient.<String>getBucket(jwtRefreshTokenRedisKey).isExists();

        if (!exists) {
            R.error(TempBizCodeEnum.LOGIN_EXPIRED);
        }

        return BaseJwtUtil.generateJwt(userId, null, false, RequestUtil.getRequestCategoryEnum(),
            dto.getJwtRefreshToken());

    }

}
