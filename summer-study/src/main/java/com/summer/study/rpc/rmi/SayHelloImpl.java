package com.summer.study.rpc.rmi;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * @author: create by Summer.Xiong
 * @version: v1.0
 * @description: com.summer.study.rpc.rmi
 * @date:2019/11/26
 */
public class SayHelloImpl extends UnicastRemoteObject implements ISayHello {


    public SayHelloImpl() throws RemoteException{
        
    }

    @Override
    public String sayHello(String username) throws RemoteException {
        return "你好!" + username;
    }
}
