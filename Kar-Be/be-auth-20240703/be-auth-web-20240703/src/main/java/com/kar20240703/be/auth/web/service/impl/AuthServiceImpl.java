package com.kar20240703.be.auth.web.service.impl;

import cn.hutool.core.exceptions.ValidateException;
import cn.hutool.core.util.StrUtil;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTValidator;
import com.kar20240703.be.auth.web.exception.AuthBizCodeEnum;
import com.kar20240703.be.auth.web.mapper.BaseUserAuthMapper;
import com.kar20240703.be.auth.web.model.enums.AuthRedisKeyEnum;
import com.kar20240703.be.auth.web.model.vo.R;
import com.kar20240703.be.auth.web.service.AuthService;
import com.kar20240703.be.auth.web.util.MyJwtUtil;
import com.kar20240703.be.auth.web.util.RequestUtil;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    @Resource
    BaseUserAuthMapper baseUserAuthMapper;

    @Resource
    RedissonClient redissonClient;

    @Resource
    HttpServletRequest httpServletRequest;

    /**
     * 获取权限集合
     * <p>
     * 备注：会检查 jwt的合法性，以及用户的状态
     *
     * @return null 所有权限 空集合 无权限
     */
    @Override
    public List<String> getAuthList() {

        String jwtStr = MyJwtUtil.getJwtStrByRequest(httpServletRequest);

        if (StrUtil.isBlank(jwtStr)) {

            R.error(AuthBizCodeEnum.LOGIN_EXPIRED);

        }

        JWT jwt = null;

        try {

            jwt = JWT.of(jwtStr);

        } catch (Exception e) {

            R.error(AuthBizCodeEnum.LOGIN_EXPIRED);

        }

        jwt.setKey(MyJwtUtil.getJwtSecret().getBytes());

        // 验证算法
        if (jwt.verify() == false) {

            R.error(AuthBizCodeEnum.LOGIN_EXPIRED);

        }

        try {

            // 校验时间字段：如果过期了，这里会抛出 ValidateException异常
            JWTValidator.of(jwt).validateDate(new Date());

        } catch (ValidateException e) {

            R.error(AuthBizCodeEnum.LOGIN_EXPIRED);

        }

        // 获取：userId的值
        Long userId = MyJwtUtil.getPayloadMapUserIdValue(jwt.getPayload().getClaimsJson());

        if (userId == null) {

            R.error(AuthBizCodeEnum.LOGIN_EXPIRED);

        }

        String redisJwt =
            MyJwtUtil.generateRedisJwt(jwtStr, userId, RequestUtil.getRequestCategoryEnum(httpServletRequest));

        boolean exists = redissonClient.getBucket(redisJwt).isExists();

        if (exists == false) {

            R.error(AuthBizCodeEnum.LOGIN_EXPIRED);

        }

        boolean disableFlag =
            redissonClient.getBucket(AuthRedisKeyEnum.PRE_USER_DISABLE.name() + ":" + userId).isExists();

        if (disableFlag) {

            R.error(AuthBizCodeEnum.ACCOUNT_IS_DISABLED);

        }

        // 获取：获取用户权限集合
        return MyJwtUtil.getAuthListByUserId(userId);

    }

}
