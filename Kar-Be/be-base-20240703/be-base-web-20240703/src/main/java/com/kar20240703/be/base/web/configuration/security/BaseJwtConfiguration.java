package com.kar20240703.be.base.web.configuration.security;

import cn.hutool.json.JSONObject;
import com.kar20240703.be.base.web.util.BaseJwtUtil;
import com.kar20240703.be.temp.web.model.configuration.IJwtConfiguration;
import com.kar20240703.be.temp.web.model.configuration.IJwtGenerateConfiguration;
import com.kar20240703.be.temp.web.model.enums.TempRequestCategoryEnum;
import com.kar20240703.be.temp.web.model.vo.SignInVO;
import com.kar20240703.be.temp.web.util.MyJwtUtil;
import java.util.List;
import java.util.function.Consumer;
import org.jetbrains.annotations.Nullable;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@Configuration
public class BaseJwtConfiguration implements IJwtConfiguration, IJwtGenerateConfiguration {

    @Override
    public List<SimpleGrantedAuthority> getSimpleGrantedAuthorityListByUserId(Long userId) {

        return MyJwtUtil.getSimpleGrantedAuthorityListByUserId(userId);

    }

    @Override
    public SignInVO generateJwt(Long userId, @Nullable Consumer<JSONObject> consumer, boolean generateRefreshTokenFlag,
        TempRequestCategoryEnum tempRequestCategoryEnum) {

        return BaseJwtUtil.generateJwt(userId, consumer, generateRefreshTokenFlag, tempRequestCategoryEnum);

    }

}
