package com.kar20240703.be.temp.web.model.configuration;

import java.util.List;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public interface IJwtConfiguration {

    /**
     * 获取：权限
     */
    List<SimpleGrantedAuthority> getSimpleGrantedAuthorityListByUserId(Long userId);

}
