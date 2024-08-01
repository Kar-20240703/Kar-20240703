package com.kar20240703.be.temp.web.configuration.security;

import cn.hutool.jwt.JWT;
import com.kar20240703.be.temp.web.exception.TempBizCodeEnum;
import com.kar20240703.be.temp.web.exception.TempException;
import com.kar20240703.be.temp.web.model.configuration.IJwtValidatorConfiguration;
import com.kar20240703.be.temp.web.model.vo.R;
import com.kar20240703.be.temp.web.util.MyJwtUtil;
import com.kar20240703.be.temp.web.util.ResponseUtil;
import com.kar20240703.be.temp.web.util.UserUtil;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

@Component
public class SecurityJwtValidatorConfiguration implements IJwtValidatorConfiguration {

    /**
     * 判断：用户是否被封禁，或者用户所属的租户是否被封禁
     */
    @Override
    public boolean validator(JWT jwt, String requestUri, HttpServletResponse response) {

        // 获取：userId的值
        Long userId = MyJwtUtil.getPayloadMapUserIdValue(jwt.getPayload().getClaimsJson());

        if (userId == null) {

            return true;

        }

        // 检查：用户：是否被冻结
        if (UserUtil.getDisable(userId)) {

            try {

                R.error(TempBizCodeEnum.ACCOUNT_IS_DISABLED); // 这里肯定会抛出 BaseException异常

            } catch (TempException e) {

                ResponseUtil.out(response, e.getMessage(), false);

            }

            return false;

        }

        return true;

    }

}
