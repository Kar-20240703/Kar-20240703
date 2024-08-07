package com.kar20240703.be.base.web.configuration.security;

import cn.hutool.json.JSONObject;
import com.kar20240703.be.base.web.util.BaseJwtUtil;
import com.kar20240703.be.temp.web.model.configuration.IJwtGenerateConfiguration;
import com.kar20240703.be.temp.web.model.enums.TempRequestCategoryEnum;
import com.kar20240703.be.temp.web.model.vo.SignInVO;
import java.util.function.Consumer;
import org.jetbrains.annotations.Nullable;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BaseJwtConfiguration implements IJwtGenerateConfiguration {

    @Override
    public SignInVO generateJwt(Long userId, @Nullable Consumer<JSONObject> consumer, boolean generateRefreshTokenFlag,
        TempRequestCategoryEnum tempRequestCategoryEnum) {

        return BaseJwtUtil.generateJwt(userId, consumer, generateRefreshTokenFlag, tempRequestCategoryEnum);

    }

}
