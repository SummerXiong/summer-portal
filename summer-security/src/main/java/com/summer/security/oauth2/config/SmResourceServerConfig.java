package com.summer.security.oauth2.config;

import com.summer.security.handler.SmAuthenticationFailureHandler;
import com.summer.security.sms.config.SmsCodeAuthenticationSecurityConfig;
import com.summer.security.sms.filter.SmsCodeValidateFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * @author: create by Summer.Xiong
 * @version: v1.0
 * @description: com.summer.security.oauth2.config
 * @date:2019/12/3
 */
@Configuration
@EnableResourceServer
public class SmResourceServerConfig extends ResourceServerConfigurerAdapter {

    @Autowired
    protected SmAuthenticationFailureHandler smAuthenticationFailureHandler;
    @Autowired
    protected SmsCodeAuthenticationSecurityConfig smsCodeAuthenticationSecurityConfig;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        SmsCodeValidateFilter filter = new SmsCodeValidateFilter(smAuthenticationFailureHandler);

        //添加手机+短信验证码登录的验证filter
        http.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);

        http.formLogin()//表单登录
                .loginPage("/authentication/require") //此处的url可以写成一个controller地址, 而后可以在controller处理相关逻辑
                //.loginProcessingUrl() //处理登录请求的url
                .and()
                .authorizeRequests()//对请求进行授权
                .antMatchers("/authentication/require", "/code/sms", "/mobile/login").permitAll() //配置自定义的登录页获取其他不需要授权的路径
                .anyRequest()//如何请求
                .authenticated();//都需要身份认证

        //添加短信认证流程加入到Spring Security中
        http.apply(smsCodeAuthenticationSecurityConfig);
        http.csrf().disable();//关闭跨域请求验证
    }
}
