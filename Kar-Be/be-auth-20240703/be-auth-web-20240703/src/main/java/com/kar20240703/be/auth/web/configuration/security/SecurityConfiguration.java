package com.kar20240703.be.auth.web.configuration.security;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import com.kar20240703.be.auth.web.configuration.base.AuthConfiguration;
import com.kar20240703.be.auth.web.model.configuration.ISecurityPermitConfiguration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Slf4j
public class SecurityConfiguration {

    /**
     * @param authConfiguration 不要删除，目的：让 springboot实例化该对象
     */
    @SneakyThrows
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity, AuthConfiguration authConfiguration,
        List<ISecurityPermitConfiguration> iSecurityPermitConfigurationList) {

        boolean prodFlag = AuthConfiguration.prodFlag();

        Set<String> permitAllSet = new HashSet<>();

        if (CollUtil.isNotEmpty(iSecurityPermitConfigurationList)) {

            for (ISecurityPermitConfiguration item : iSecurityPermitConfigurationList) {

                if (prodFlag) {

                    CollUtil.addAll(permitAllSet, item.prodPermitAllSet());

                } else {

                    CollUtil.addAll(permitAllSet, item.devPermitAllSet());

                }

                CollUtil.addAll(permitAllSet, item.anyPermitAllSet());

            }

        }

        log.info("permitAllSet：{}", permitAllSet);

        httpSecurity.authorizeRequests().antMatchers(ArrayUtil.toArray(permitAllSet, String.class))
            .permitAll() // 可以匿名访问的请求
            .anyRequest().authenticated(); // 拦截所有请求

        httpSecurity.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS); // 不需要session

        // 用户没有登录，但是访问需要权限的资源时，而报出的错误
        httpSecurity.exceptionHandling().authenticationEntryPoint(new MyAuthenticationEntryPoint());

        httpSecurity.csrf().disable(); // 禁用 CSRF保护

        httpSecurity.logout().disable(); // 禁用 logout

        httpSecurity.formLogin().disable(); // 禁用 login

        httpSecurity.httpBasic().disable(); // 禁用 http basic认证

        return httpSecurity.build();

    }

}
