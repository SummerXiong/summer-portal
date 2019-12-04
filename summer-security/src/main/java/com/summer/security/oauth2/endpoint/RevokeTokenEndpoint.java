package com.summer.security.oauth2.endpoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.endpoint.AbstractEndpoint;
import org.springframework.security.oauth2.provider.endpoint.FrameworkEndpoint;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import sun.security.util.SecurityConstants;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author: create by Summer.Xiong
 * @version: v1.0
 * @description: com.summer.security.oauth2.endpoint
 * @date:2019/12/3
 */
//@FrameworkEndpoint
@RestController
public class RevokeTokenEndpoint{

    @Autowired()
    @Qualifier("consumerTokenServices")
    ConsumerTokenServices consumerTokenServices;

    @RequestMapping(method = RequestMethod.DELETE, value = "/oauth/logut")
    @ResponseBody
    //@CacheEvict(value = SecurityConstants.TOKEN_USER_DETAIL, key = "#accesstoken")
    public ResponseEntity<String> revokeToken(String access_token) {

        if (consumerTokenServices.revokeToken(access_token)){
            return ResponseEntity.ok("Logout success");
        }else{
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Logout fail");
        }
    }

}
