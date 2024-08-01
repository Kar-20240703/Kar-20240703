package com.kar20240703.be.temp.web.configuration.knife4j;

import cn.hutool.core.collection.CollUtil;
import com.kar20240703.be.temp.web.model.configuration.ISecurityPermitConfiguration;
import java.util.Set;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Knife4JSecurityPermitConfiguration implements ISecurityPermitConfiguration {

    @Override
    public Set<String> devPermitAllSet() {
        return null;
    }

    @Override
    public Set<String> prodPermitAllSet() {
        return null;
    }

    @Override
    public Set<String> anyPermitAllSet() {
        return CollUtil.newHashSet("/v3/api-docs/**", "/webjars/**", "/doc.html/**", "/favicon.ico");
    }

}
