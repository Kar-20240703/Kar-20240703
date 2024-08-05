package com.kar20240703.be.temp.web.model.configuration;

import java.util.Set;

/**
 * Security 不用权限就可以访问的 url配置类
 */
public interface ISecurityPermitConfiguration {

    /**
     * 开发环境 不用权限就可以访问的 url
     */
    default Set<String> devPermitAllSet() {
        return null;
    }

    /**
     * 生产环境 不用权限就可以访问的 url
     */
    default Set<String> prodPermitAllSet() {
        return null;
    }

    /**
     * 所有环境 不用权限就可以访问的 url
     */
    Set<String> anyPermitAllSet();

}
