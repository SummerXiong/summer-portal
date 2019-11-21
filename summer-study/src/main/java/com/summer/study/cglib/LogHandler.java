package com.summer.study.cglib;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author: create by Summer.Xiong
 * @version: v1.0
 * @description: com.summer.study.cglib
 * @date:2019/11/21
 */
public class LogHandler {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public void handle(){
        logger.info("handle logger...");
    }
}
