package com.summer.security.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: create by Summer.Xiong
 * @version: v1.0
 * @description: com.summer.security.controller
 * @date:2019/11/28
 */
@RestController
public class DemoController {

    @GetMapping("test")
    public String demo(){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = "";
        if(principal instanceof UserDetails){
             username = ((UserDetails)principal).getUsername();
        }else{
             username = principal.toString();
        }
        return username;
    }
}
