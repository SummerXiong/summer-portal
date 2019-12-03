package com.summer.security.oauth2.config;

import com.summer.security.handler.SmAuthenticationFailureHandler;
import com.summer.security.sms.config.SmsCodeAuthenticationSecurityConfig;
import com.summer.security.sms.filter.SmsCodeValidateFilter;
import com.summer.security.sms.handler.SmsCodeAuthenticationSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.client.InMemoryClientDetailsService;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * @author: create by Summer.Xiong
 * @version: v1.0
 * @description:
 *          OAuth2 认证服务器实现
 *          @EnableAuthorizationServer  即开启了 Oauth2认证服务器
 * @date:2019/12/3
 */
@Configuration
@EnableAuthorizationServer
public class SmAuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    public static final String GRANT_TYPE_PASSWORD = "password";
    public static final String AUTHORIZATION_CODE = "authorization_code";
    public static final String REFRESH_TOKEN = "refresh_token";
    public static final String IMPLICIT = "implicit";
    /**
     * 自定义授权类型 - 短信验证码
     */
    public static final String SMS_CODE = "sms_code";


    /**/

    /**
     * 可以查看有哪些PasswordEncoder的实现类
     * 实现类: BCryptPasswordEncoder 同一个密码加密的结果是不一样的，因为有随机盐
     */
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    /*@Bean
    public ClientDetailsService clientDetailsService(){
        return new InMemoryClientDetailsService();
    }

    @Bean
    public AuthorizationServerTokenServices authorizationServerTokenServices(){
        DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
        defaultTokenServices.setTokenStore(new InMemoryTokenStore());
        return defaultTokenServices;
    }*/
}
