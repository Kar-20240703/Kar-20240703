package com.kar20240703.be.base.web.util;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.toolkit.ChainWrappers;
import com.kar20240703.be.base.web.exception.BaseBizCodeEnum;
import com.kar20240703.be.base.web.mapper.BaseUserInfoMapper;
import com.kar20240703.be.base.web.mapper.BaseUserMapper;
import com.kar20240703.be.base.web.model.enums.BaseRedisKeyEnum;
import com.kar20240703.be.temp.web.exception.TempBizCodeEnum;
import com.kar20240703.be.temp.web.model.constant.TempConstant;
import com.kar20240703.be.temp.web.model.constant.TempRegexConstant;
import com.kar20240703.be.temp.web.model.domain.TempEntity;
import com.kar20240703.be.temp.web.model.domain.TempUserDO;
import com.kar20240703.be.temp.web.model.domain.TempUserInfoDO;
import com.kar20240703.be.temp.web.model.enums.TempRequestCategoryEnum;
import com.kar20240703.be.temp.web.model.interfaces.IRedisKey;
import com.kar20240703.be.temp.web.model.vo.R;
import com.kar20240703.be.temp.web.model.vo.SignInVO;
import com.kar20240703.be.temp.web.properties.MySecurityProperties;
import com.kar20240703.be.temp.web.util.CodeUtil;
import com.kar20240703.be.temp.web.util.MyEntityUtil;
import com.kar20240703.be.temp.web.util.MyJwtUtil;
import com.kar20240703.be.temp.web.util.NicknameUtil;
import com.kar20240703.be.temp.web.util.RedissonUtil;
import com.kar20240703.be.temp.web.util.RequestUtil;
import com.kar20240703.be.temp.web.util.UserUtil;
import java.time.Duration;
import java.util.Map;
import javax.annotation.Resource;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.redisson.api.RAtomicLong;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

@Component
public class SignUtil {

    private static RedissonClient redissonClient;

    @Resource
    public void setRedissonClient(RedissonClient redissonClient) {
        SignUtil.redissonClient = redissonClient;
    }

    private static BaseUserMapper baseUserMapper;

    @Resource
    public void setBaseUserMapper(BaseUserMapper baseUserMapper) {
        SignUtil.baseUserMapper = baseUserMapper;
    }

    private static BaseUserInfoMapper baseUserInfoMapper;

    @Resource
    public void setBaseUserInfoMapper(BaseUserInfoMapper baseUserInfoMapper) {
        SignUtil.baseUserInfoMapper = baseUserInfoMapper;
    }

    private static MySecurityProperties mySecurityProperties;

    @Resource
    public void setSecurityProperties(MySecurityProperties mySecurityProperties) {
        SignUtil.mySecurityProperties = mySecurityProperties;
    }

    /**
     * 注册
     */
    public static String signUp(String password, String originPassword, String code,
        Enum<? extends IRedisKey> redisKeyEnum, String account) {

        if (TempConstant.ADMIN_ACCOUNT.equals(account)) {

            R.error(TempBizCodeEnum.THE_ADMIN_ACCOUNT_DOES_NOT_SUPPORT_THIS_OPERATION);

        }

        if (StrUtil.isNotBlank(password) && StrUtil.isNotBlank(originPassword)) {

            password = MyRsaUtil.rsaDecrypt(password);

            originPassword = MyRsaUtil.rsaDecrypt(originPassword);

            if (BooleanUtil.isFalse(ReUtil.isMatch(TempRegexConstant.PASSWORD_REGEXP, originPassword))) {

                R.error(BaseBizCodeEnum.PASSWORD_RESTRICTIONS); // 不合法直接抛出异常

            }

        }

        String key = redisKeyEnum + ":" + account;

        String finalPassword = password;

        RBucket<String> bucket = redissonClient.getBucket(key);

        boolean checkCodeFlag =
            BaseRedisKeyEnum.PRE_EMAIL.equals(redisKeyEnum) || BaseRedisKeyEnum.PRE_PHONE.equals(redisKeyEnum);

        return RedissonUtil.doLock(key, () -> {

            if (checkCodeFlag) {
                CodeUtil.checkCode(code, bucket.get()); // 检查 code是否正确
            }

            // 检查：注册的登录账号是否存在
            boolean exist = accountIsExists(redisKeyEnum, account, null, null);

            if (exist) {

                if (checkCodeFlag) {
                    bucket.delete(); // 删除：验证码
                }

                R.error(BaseBizCodeEnum.THE_ACCOUNT_HAS_ALREADY_BEEN_REGISTERED);

            }

            Map<Enum<? extends IRedisKey>, String> accountMap = MapUtil.newHashMap();

            accountMap.put(redisKeyEnum, account);

            // 新增：用户
            SignUtil.insertUser(finalPassword, accountMap, true, null, null);

            if (checkCodeFlag) {
                bucket.delete(); // 删除：验证码
            }

            return "注册成功";

        });

    }

    /**
     * 新增：用户
     */
    @NotNull
    public static TempUserDO insertUser(String password, Map<Enum<? extends IRedisKey>, String> accountMap,
        boolean checkPasswordBlank, @Nullable TempUserInfoDO tempTempUserInfoDO, Boolean enableFlag) {

        // 获取：TempUserDO对象
        TempUserDO tempUserDO = insertUserGetTempUserDO(password, accountMap, checkPasswordBlank, enableFlag);

        return TransactionUtil.exec(() -> {

            baseUserMapper.insert(tempUserDO); // 保存：用户

            TempUserInfoDO tempUserInfoDO = new TempUserInfoDO();

            tempUserInfoDO.setId(tempUserDO.getId());

            tempUserInfoDO.setLastActiveTime(tempUserDO.getCreateTime());

            if (tempTempUserInfoDO == null) {

                tempUserInfoDO.setNickname(NicknameUtil.getRandomNickname());
                tempUserInfoDO.setBio("");

                tempUserInfoDO.setAvatarFileId(-1L);

                tempUserInfoDO.setSignUpType(RequestUtil.getRequestCategoryEnum());

                tempUserInfoDO.setLastIp(RequestUtil.getIp());

            } else {

                tempUserInfoDO.setNickname(
                    MyEntityUtil.getNotNullStr(tempTempUserInfoDO.getNickname(), NicknameUtil.getRandomNickname()));

                tempUserInfoDO.setBio(MyEntityUtil.getNotNullStr(tempTempUserInfoDO.getBio()));

                tempUserInfoDO.setAvatarFileId(MyEntityUtil.getNotNullLong(tempTempUserInfoDO.getAvatarFileId()));

                tempUserInfoDO.setSignUpType(MyEntityUtil.getNotNullObject(tempTempUserInfoDO.getSignUpType(),
                    RequestUtil.getRequestCategoryEnum()));

                tempUserInfoDO.setLastIp(
                    MyEntityUtil.getNotNullStr(tempTempUserInfoDO.getLastIp(), RequestUtil.getIp()));

            }

            tempUserInfoDO.setLastRegion(Ip2RegionUtil.getRegion(tempUserInfoDO.getLastIp()));

            baseUserInfoMapper.insert(tempUserInfoDO); // 保存：用户基本信息

            return tempUserDO;

        });

    }

    /**
     * 获取：TempUserDO对象
     */
    @NotNull
    private static TempUserDO insertUserGetTempUserDO(String password,
        Map<Enum<? extends IRedisKey>, String> accountMap, boolean checkPasswordBlank, Boolean enableFlag) {

        TempUserDO tempUserDO = new TempUserDO();

        if (enableFlag == null) {
            tempUserDO.setEnableFlag(true);
        } else {
            tempUserDO.setEnableFlag(enableFlag);
        }

        tempUserDO.setEmail("");
        tempUserDO.setUsername("");
        tempUserDO.setPhone("");
        tempUserDO.setWxOpenId("");
        tempUserDO.setWxAppId("");
        tempUserDO.setWxUnionId("");

        for (Map.Entry<Enum<? extends IRedisKey>, String> item : accountMap.entrySet()) {

            if (BaseRedisKeyEnum.PRE_EMAIL.equals(item.getKey())) {

                tempUserDO.setEmail(item.getValue());

            } else if (BaseRedisKeyEnum.PRE_USER_NAME.equals(item.getKey())) {

                tempUserDO.setUsername(item.getValue());

            } else if (BaseRedisKeyEnum.PRE_PHONE.equals(item.getKey())) {

                tempUserDO.setPhone(item.getValue());

            } else if (BaseRedisKeyEnum.PRE_WX_APP_ID.equals(item.getKey())) {

                tempUserDO.setWxAppId(item.getValue());

            } else if (BaseRedisKeyEnum.PRE_WX_OPEN_ID.equals(item.getKey())) {

                tempUserDO.setWxOpenId(item.getValue());

            } else if (BaseRedisKeyEnum.PRE_WX_UNION_ID.equals(item.getKey())) {

                tempUserDO.setWxUnionId(item.getValue());

            }

        }

        tempUserDO.setPassword(PasswordConvertUtil.convert(password, checkPasswordBlank));

        return tempUserDO;

    }

    /**
     * 检查登录账号是否存在
     */
    public static boolean accountIsExists(Enum<? extends IRedisKey> redisKeyEnum, String newAccount, @Nullable Long id,
        String appId) {

        LambdaQueryChainWrapper<TempUserDO> lambdaQueryChainWrapper =
            ChainWrappers.lambdaQueryChain(baseUserMapper).ne(id != null, TempEntity::getId, id);

        if (BaseRedisKeyEnum.PRE_EMAIL.equals(redisKeyEnum)) {

            lambdaQueryChainWrapper.eq(TempUserDO::getEmail, newAccount);

        } else if (BaseRedisKeyEnum.PRE_USER_NAME.equals(redisKeyEnum)) {

            lambdaQueryChainWrapper.eq(TempUserDO::getUsername, newAccount);

        } else if (BaseRedisKeyEnum.PRE_PHONE.equals(redisKeyEnum)) {

            lambdaQueryChainWrapper.eq(TempUserDO::getPhone, newAccount);

        } else if (BaseRedisKeyEnum.PRE_WX_OPEN_ID.equals(redisKeyEnum)) {

            lambdaQueryChainWrapper.eq(TempUserDO::getWxAppId, appId).eq(TempUserDO::getWxOpenId, newAccount);

        } else {

            R.sysError();

        }

        return lambdaQueryChainWrapper.exists();

    }

    /**
     * 账号密码登录
     */
    public static SignInVO signInPassword(LambdaQueryChainWrapper<TempUserDO> lambdaQueryChainWrapper, String password,
        String account, TempRequestCategoryEnum tempRequestCategoryEnum) {

        // 密码解密
        password = MyRsaUtil.rsaDecrypt(password);

        // 如果是 admin账户
        if (TempConstant.ADMIN_ACCOUNT.equals(account)) {

            if (signInPasswordForAdmin(password)) {
                return BaseJwtUtil.generateJwt(TempConstant.ADMIN_ID, null, true, tempRequestCategoryEnum);
            }

        }

        // 登录时，获取账号信息
        TempUserDO tempUserDO = signInGetTempUserDO(lambdaQueryChainWrapper, true);

        if (tempUserDO == null || StrUtil.isBlank(tempUserDO.getPassword())) {
            R.error(BaseBizCodeEnum.NO_PASSWORD_SET); // 未设置密码，请点击【忘记密码】，进行密码设置
        }

        if (BooleanUtil.isFalse(PasswordConvertUtil.match(tempUserDO.getPassword(), password))) {

            // 密码输入错误处理
            passwordErrorHandlerWillError(tempUserDO.getId());

        }

        // 登录时，获取：jwt
        return signInGetJwt(tempUserDO, true, tempRequestCategoryEnum);

    }

    /**
     * admin登录，登录成功返回 true
     */
    private static boolean signInPasswordForAdmin(String password) {

        if (UserUtil.getDisable(TempConstant.ADMIN_ID)) { // admin是否被冻结

            R.error(TempBizCodeEnum.ACCOUNT_IS_DISABLED);

            return false;

        } else {

            // 判断：密码错误次数过多
            checkTooManyPasswordWillError(TempConstant.ADMIN_ID);

            if (BooleanUtil.isFalse(mySecurityProperties.getAdminPassword().equals(password))) {

                // 密码输入错误处理
                passwordErrorHandlerWillError(TempConstant.ADMIN_ID);

                return false;

            } else {

                return true;

            }

        }

    }

    /**
     * 登录时，获取：jwt
     */
    @Nullable
    public static SignInVO signInGetJwt(TempUserDO tempUserDO, boolean generateRefreshTokenFlag,
        TempRequestCategoryEnum tempRequestCategoryEnum) {

        // 校验密码，成功之后，再判断是否被冻结，免得透露用户被封号的信息
        if (BooleanUtil.isFalse(tempUserDO.getEnableFlag())) {

            R.error(TempBizCodeEnum.ACCOUNT_IS_DISABLED);

        }

        // 颁发，并返回 jwt
        return BaseJwtUtil.generateJwt(tempUserDO.getId(), payloadMap -> {

            payloadMap.set(MyJwtUtil.PAYLOAD_MAP_WX_APP_ID_KEY, tempUserDO.getWxAppId());

            payloadMap.set(MyJwtUtil.PAYLOAD_MAP_WX_OPEN_ID_KEY, tempUserDO.getWxOpenId());

        }, generateRefreshTokenFlag, tempRequestCategoryEnum);

    }

    /**
     * 密码错误次数过多，直接锁定账号，可以进行【忘记密码】操作，解除锁定
     */
    private static void passwordErrorHandlerWillError(Long userId) {

        if (userId == null) {
            R.sysError();
        }

        RAtomicLong atomicLong =
            redissonClient.getAtomicLong(BaseRedisKeyEnum.PRE_PASSWORD_ERROR_COUNT.name() + ":" + userId);

        long count = atomicLong.incrementAndGet(); // 次数 + 1

        if (count == 1) {
            atomicLong.expire(Duration.ofMillis(TempConstant.DAY_30_EXPIRE_TIME)); // 等于 1表示，是第一次访问，则设置过期时间
        }

        if (count >= 10) {

            // 超过十次密码错误，则封禁账号
            redissonClient.<String>getBucket(BaseRedisKeyEnum.PRE_TOO_MANY_PASSWORD_ERROR.name() + ":" + userId)
                .set("密码错误次数过多，被锁定的账号");

            atomicLong.delete(); // 清空错误次数

            R.error(BaseBizCodeEnum.TOO_MANY_PASSWORD_ERROR);

        }

        R.error(BaseBizCodeEnum.ACCOUNT_OR_PASSWORD_NOT_VALID);

    }

    /**
     * 登录时，获取：账号信息
     */
    @Nullable
    private static TempUserDO signInGetTempUserDO(LambdaQueryChainWrapper<TempUserDO> lambdaQueryChainWrapper,
        boolean errorFlag) {

        TempUserDO tempUserDO = lambdaQueryChainWrapper.one();

        // 账户是否存在
        if (tempUserDO == null) {

            if (errorFlag) {

                R.error(BaseBizCodeEnum.ACCOUNT_OR_PASSWORD_NOT_VALID);

            } else {

                return null;

            }

        }

        // 判断：密码错误次数过多
        checkTooManyPasswordWillError(tempUserDO.getId());

        return tempUserDO;

    }

    /**
     * 判断：密码错误次数过多
     */
    public static void checkTooManyPasswordWillError(Long userId) {

        boolean exists =
            redissonClient.getBucket(BaseRedisKeyEnum.PRE_TOO_MANY_PASSWORD_ERROR.name() + ":" + userId).isExists();

        if (exists) {
            R.error(BaseBizCodeEnum.TOO_MANY_PASSWORD_ERROR);
        }

    }

}
