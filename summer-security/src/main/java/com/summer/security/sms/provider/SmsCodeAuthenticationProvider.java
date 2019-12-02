package com.summer.security.sms.provider;

import com.summer.security.sms.token.SmsCodeAuthenticationToken;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;


/**
 * @author: create by Summer.Xiong
 * @version: v1.0
 * @description: com.summer.security.sms.provider
 * @date:2019/11/29
 */
public class SmsCodeAuthenticationProvider implements AuthenticationProvider{

    private UserDetailsService userDetailsService;

    /**
     * 身份认证处理
     * @param authentication
     * @return
     * @throws AuthenticationException
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        SmsCodeAuthenticationToken authenticationToken = (SmsCodeAuthenticationToken) authentication;
        UserDetails user = userDetailsService.loadUserByUsername((String)authenticationToken.getPrincipal());

        if(null == user){
            throw new InternalAuthenticationServiceException("Can't find user:" + authenticationToken.getPrincipal());
        }

        // 创建已认证的authenticationToken
        // 未认证的authenticationToken,已经存在了Details,我们可以直接取过来赋值到已认证的Token中
        // 未认证的authenticationToken中的Details是在对应的Filter中设置进去的
        SmsCodeAuthenticationToken resultToken = new SmsCodeAuthenticationToken(user, user.getAuthorities());
        resultToken.setDetails(authenticationToken.getDetails());

        return resultToken;
    }

    /**
     * 根据传进来的authentication来判断,当前的Provider是否有能力处理这个Token
     * @param authentication
     * @return
     */
    @Override
    public boolean supports(Class<?> authentication) {
        return SmsCodeAuthenticationToken.class.isAssignableFrom(authentication);
    }

    public UserDetailsService getUserDetailsService() {
        return userDetailsService;
    }

    public void setUserDetailsService(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }
}
