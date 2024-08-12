package com.kar20240703.be.base.web.configuration.security;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.kar20240703.be.temp.web.model.configuration.ISecurityPermitConfiguration;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class AbstractSignHelperSecurityPermitConfiguration implements ISecurityPermitConfiguration {

    private static final String BASE_PRE_URI = "/sign/";

    private static final Set<String> URI_TEMP_SET =
        CollUtil.newHashSet("/signUp/**", "/signIn/**", "/forgetPassword/**");

    protected abstract String getSignPreUri();

    @Override
    public Set<String> anyPermitAllSet() {

        if (StrUtil.isBlank(getSignPreUri())) {
            return null;
        }

        return URI_TEMP_SET.stream().map(it -> BASE_PRE_URI + getSignPreUri() + it).collect(Collectors.toSet());

    }

}
