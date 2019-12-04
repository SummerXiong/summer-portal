package com.summer.security.oauth2.jwt;

import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: create by Summer.Xiong
 * @version: v1.0
 * @description: com.summer.security.oauth2.jwt
 * @date:2019/12/3
 */
public class JwtTokenEnhancer implements TokenEnhancer {

    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {

        Map<String, Object> usefInfo = new HashMap<>();
        usefInfo.put("userId", "admin");

        ((DefaultOAuth2AccessToken)accessToken).setAdditionalInformation(usefInfo);

        return accessToken;
    }
}
