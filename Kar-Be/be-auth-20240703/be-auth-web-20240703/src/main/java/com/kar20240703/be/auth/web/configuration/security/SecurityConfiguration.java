package com.kar20240703.be.auth.web.configuration.security;

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

    @SneakyThrows
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) {

        httpSecurity.authorizeRequests().anyRequest().authenticated(); // 拦截所有请求

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
