package com.kar20240703.be.temp.web.model.configuration;

import cn.hutool.json.JSONObject;
import com.kar20240703.be.temp.web.model.enums.TempRequestCategoryEnum;
import com.kar20240703.be.temp.web.model.vo.SignInVO;
import java.util.List;
import java.util.function.Consumer;
import org.jetbrains.annotations.Nullable;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public interface IJwtConfiguration {

    /**
     * 获取：权限
     */
    List<SimpleGrantedAuthority> getSimpleGrantedAuthorityListByUserId(Long userId);

    SignInVO generateJwt(Long userId, @Nullable Consumer<JSONObject> consumer, boolean generateRefreshTokenFlag,
        TempRequestCategoryEnum tempRequestCategoryEnum);

}
