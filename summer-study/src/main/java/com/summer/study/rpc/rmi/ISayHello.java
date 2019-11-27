package com.summer.study.rpc.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * @author: create by Summer.Xiong
 * @version: v1.0
 * @description: com.summer.study.rpc.rmi
 * @date:2019/11/26
 */
public interface ISayHello extends Remote {

    String sayHello(String username) throws RemoteException;
}
