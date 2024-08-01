package com.kar20240703.be.temp.web.model.configuration;

import cn.hutool.json.JSONObject;
import com.kar20240703.be.temp.web.model.enums.TempRequestCategoryEnum;
import com.kar20240703.be.temp.web.model.vo.SignInVO;
import java.util.function.Consumer;
import org.jetbrains.annotations.Nullable;

public interface IJwtGenerateConfiguration {

    /**
     * 生成 jwt
     */
    @Nullable
    SignInVO generateJwt(Long userId, @Nullable Consumer<JSONObject> consumer, boolean generateRefreshTokenFlag,
        TempRequestCategoryEnum tempRequestCategoryEnum);

}
