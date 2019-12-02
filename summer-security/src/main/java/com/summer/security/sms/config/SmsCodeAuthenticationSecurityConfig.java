package com.summer.security.sms.config;

import com.summer.security.handler.SmAuthenticationFailureHandler;
import com.summer.security.handler.SmAuthenticationSuccessHandler;
import com.summer.security.sms.filter.SmsCodeAuthenticationFilter;
import com.summer.security.sms.provider.SmsCodeAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

/**
 * @author: create by Summer.Xiong
 * @version: v1.0
 * @description:
 *          将手机短信登录的相关类配置到Spring Security中
 *          注意:此处声明的SmsCodeAuthenticationSecurityConfig, 仅仅是组装了一个AuthenticationSecurityConfig还没有应用到Spring Security
 *          如何此配置要生效,需要在WebSecurityConfigurerAdapter中apply一下
 *          apply的位置也代表这段配置在什么位置开始生效
 * @date:2019/11/29
 */
@Component
public class SmsCodeAuthenticationSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    /**
     * 此处的成功,失败处理器,和查询用户的DetailService都是我们自己写的
     */
    @Autowired
    private SmAuthenticationSuccessHandler smAuthenticationSuccessHandler;
    @Autowired
    private SmAuthenticationFailureHandler smAuthenticationFailureHandler;
    @Autowired
    private UserDetailsService smUserDetailService;

    @Override
    public void configure(HttpSecurity http) throws Exception {

        SmsCodeAuthenticationFilter smsCodeAuthenticationFilter = new SmsCodeAuthenticationFilter();
        smsCodeAuthenticationFilter.setAuthenticationManager(http.getSharedObject(AuthenticationManager.class));
        smsCodeAuthenticationFilter.setAuthenticationSuccessHandler(smAuthenticationSuccessHandler);
        smsCodeAuthenticationFilter.setAuthenticationFailureHandler(smAuthenticationFailureHandler);

        SmsCodeAuthenticationProvider smsCodeAuthenticationProvider = new SmsCodeAuthenticationProvider();
        smsCodeAuthenticationProvider.setUserDetailsService(smUserDetailService);

        http.authenticationProvider(smsCodeAuthenticationProvider)
                .addFilterAfter(smsCodeAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
