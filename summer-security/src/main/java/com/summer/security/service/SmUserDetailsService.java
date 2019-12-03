package com.summer.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * @author: create by Summer.Xiong
 * @version: v1.0
 * @description:
 *
 *       SpringSecurity获取登录用户信息和数据库用户信息对比
 *
 * @date:2019/11/28
 */
@Service
public class SmUserDetailsService implements UserDetailsService {


    @Autowired
    protected PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        /**
         * 根据用户名查找用户信息，使用用户信息组装成UserDetails
         * 此User对象,是Spring Security的类,并实现UserDetails接口
         * 参数3是用户Spring Security授权
         * AuthorityUtils的工具类可用于生成权限集合
         * 通过不同的User构造方法,可做账号的其他信息验证
         * return new User(username, password, authorities);
         */

        /**
         * 各种错误异常抛出
         */
        String password = passwordEncoder.encode("123456");
        return new User(username, password, true, true, true, true, AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_USER"));
    }
}
