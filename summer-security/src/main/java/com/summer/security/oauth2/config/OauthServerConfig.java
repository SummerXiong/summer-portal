package com.summer.security.oauth2.config;

import com.summer.security.handler.SmAuthenticationFailureHandler;
import com.summer.security.oauth2.jwt.JwtTokenEnhancer;
import com.summer.security.sms.config.SmsCodeAuthenticationSecurityConfig;
import com.summer.security.sms.filter.SmsCodeValidateFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.*;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.security.KeyPair;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author: create by Summer.Xiong
 * @version: v1.0
 * @description: com.summer.security.oauth2.config
 * @date:2019/12/3
 */
@Configuration
public class OauthServerConfig {

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
    public class SmAuthorizationServerConfig extends AuthorizationServerConfigurerAdapter{

        public static final String GRANT_TYPE_PASSWORD = "password";
        public static final String AUTHORIZATION_CODE = "authorization_code";
        public static final String REFRESH_TOKEN = "refresh_token";
        public static final String IMPLICIT = "implicit";
        /**
         * 自定义授权类型 - 短信验证码
         */
        public static final String SMS_CODE = "sms_code";

        @Autowired
        protected AuthenticationManager authenticationManager;
        @Autowired
        protected UserDetailsService userDetailsService;
        @Autowired
        protected TokenStore redisTokenStore;
        @Autowired
        protected TokenEnhancer jwtTokenEnhancer;
        @Autowired
        protected JwtAccessTokenConverter jwtAccessTokenConverter;


        @Override
        public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
            security.tokenKeyAccess("permitAll()").checkTokenAccess("permitAll()").allowFormAuthenticationForClients();
        }

        @Override
        public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {

            //将access_token存储到redis中
            //endpoints.tokenStore(redisTokenStore);


            //增强器链路存在先后顺序，所以要先扩展jwt内容后,再转换为jwt
            //TokenEnhancerChain enhancerChain = new TokenEnhancerChain();
            //List<TokenEnhancer> enhancerList = new ArrayList<>();
            //扩展jwt的内容
            //enhancerList.add(jwtTokenEnhancer);
            //将access_token转换成jwt格式
            //enhancerList.add(jwtAccessTokenConverter);
            //enhancerChain.setTokenEnhancers(enhancerList);
            //endpoints.tokenEnhancer(enhancerChain).accessTokenConverter(jwtAccessTokenConverter);

            //注入一个AuthenticationManager，自动开启密码授权类型
            endpoints.authenticationManager(authenticationManager);
            //注入UserDetailsService，那么将会启动刷新token授权类型，会判断用户是否还是存活的
            endpoints.userDetailsService(userDetailsService);
            //是否复用refresh token
            endpoints.reuseRefreshTokens(true);
            //
            endpoints.tokenServices(authorizationServerTokenServices());
        }

        @Override
        public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
            clients.inMemory()
                    .withClient("summer")
                    .secret("summer_secret")
                    .authorizedGrantTypes(REFRESH_TOKEN)
                    .refreshTokenValiditySeconds(-1)
                    .accessTokenValiditySeconds(100);
        }

        /**
         * 针对Spring boot 1.5.9以前的版本
         * 如果使用@FrameworkEndpoint, 并注入ConsumerTokenServices实现退出登录会导致启动失败
         * 因此替换 public void configure(AuthorizationServerEndpointsConfigurer endpoints)方法下面对token的处理
         * @return
         */
        @Bean("authorizationServerTokenServices")
        public AuthorizationServerTokenServices authorizationServerTokenServices() {
            DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
            defaultTokenServices.setTokenStore(redisTokenStore);

            //增强器链路存在先后顺序，所以要先扩展jwt内容后,再转换为jwt
            TokenEnhancerChain enhancerChain = new TokenEnhancerChain();
            List<TokenEnhancer> enhancerList = new ArrayList<>();
            //扩展jwt的内容
            enhancerList.add(jwtTokenEnhancer);
            //将access_token转换成jwt格式
            enhancerList.add(jwtAccessTokenConverter);
            enhancerChain.setTokenEnhancers(enhancerList);

            defaultTokenServices.setTokenEnhancer(enhancerChain);
            defaultTokenServices.setSupportRefreshToken(true);
            return defaultTokenServices;
        }
    }


    /**
     * @author: create by Summer.Xiong
     * @version: v1.0
     * @description:
     *          OAuth2 资源服务器实现
     *          @EnableResourceServer  即开启了 Oauth2资源服务器
     * @date:2019/12/3
     */
    @Configuration
    @EnableResourceServer
    public class SmResourceServerConfig extends ResourceServerConfigurerAdapter{

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


    @Configuration
    public class TokenStoreConfig{

        @Autowired
        private RedisConnectionFactory redisConnectionFactory;

        @Bean("redisTokenStore")
        public TokenStore redisTokenStore(){
            return new RedisTokenStore(redisConnectionFactory);
        }

        /**
         * 对应私钥的公钥
         * @return
         */
        //-----BEGIN PUBLIC KEY-----MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAnGp/Q5lh0P8nPL21oMMrt2RrkT9AW5jgYwLfSUnJVc9G6uR3cXRRDCjHqWU5WYwivcF180A6CWp/ireQFFBNowgc5XaA0kPpzEtgsA5YsNX7iSnUibB004iBTfU9hZ2Rbsc8cWqynT0RyN4TP1RYVSeVKvMQk4GT1r7JCEC+TNu1ELmbNwMQyzKjsfBXyIOCFU/E94ktvsTZUHF4Oq44DBylCDsS1k7/sfZC2G5EU7Oz0mhG8+Uz6MSEQHtoIi6mc8u64Rwi3Z3tscuWG2ShtsUFuNSAFNkY7LkLn+/hxLCu2bNISMaESa8dG22CIMuIeRLVcAmEWEWH5EEforTg+QIDAQAB-----END PUBLIC KEY-----
        @Bean("jwtAccessTokenConverter")
        public JwtAccessTokenConverter jwtAccessTokenConverter(){
            JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
            KeyPair keyPair = new KeyStoreKeyFactory(new ClassPathResource("keystore.jks"), "foobar".toCharArray()).getKeyPair("test");
            converter.setKeyPair(keyPair);
            return converter;
        }

        @Bean("jwtTokenEnhancer")
        public TokenEnhancer jwtTokenEnhancer(){
            return new JwtTokenEnhancer();
        }

    }


    /*
     * Setup the refresh_token functionality to work with the custom
     * UserDetailsService
     */
    @Configuration
    protected static class GlobalAuthenticationManagerConfiguration extends GlobalAuthenticationConfigurerAdapter {

        @Autowired
        protected UserDetailsService userDetailsService;
        @Autowired
        protected PasswordEncoder passwordEncoder;

        @Override
        public void init(AuthenticationManagerBuilder auth) throws Exception {
            auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
        }
    }

    /**
     * 可以查看有哪些PasswordEncoder的实现类
     * 实现类: BCryptPasswordEncoder 同一个密码加密的结果是不一样的，因为有随机盐
     */
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

}
