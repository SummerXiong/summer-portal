#### Zookeeper安装
1. https://zookeeper.apache.org/releases.html 下载ZooKeeper
2. 解压ZooKeeper安装包，进入conf目录cp zoo_sample.cfg为zoo.cfg
3. 进入bin目录下 zkServer.sh start启动zookeeper服务
4. ./zkServer.sh {start|start-foreground|stop|restart|status|upgrade|print-cmd
------
#### Zookeeper集群搭建
1. 修改zoo.cfg
   server.id=ip:port1:port2 <br/>
   port1是follower节点与leader节点交换信息的端口号,port2是leader节点挂掉后,需要一个选举端口 <br/>
   server.1=192.168.11.128:2888:3181 <br/>
   server.1=192.168.11.129:2888:3181 <br/>
   server.1=192.168.11.120:2888:3181:observer <br/>
   
   **zoo.cfg中的dataDir对应的目录下,新建myid文件内容是对应server的id
   如果节点角色是observer，zoo.cfg还需增加peerType=observer**
------
#### Zookeeper客户端工具
    ```xml
        <!-- Curator  是由Netfix公司开源的ZooKeeper客户端 -->
        <dependency>
            <groupId>org.apache.curator</groupId>
            <artifactId>curator-framework</artifactId>
            <version>4.0.1</version>
        </dependency>
        <dependency>
            <groupId>org.apache.curator</groupId>
            <artifactId>curator-recipes</artifactId>
            <version>4.0.1</version>
        </dependency>
    ```
------
#### Zookeeper总结
- ##### Zookeeper会话状态
    1. Not Connected
    2. Connecting
    3. Connected
    4. Close
- ##### Zookeeper的节点(znode)类型
    1. 临时节点
    2. 临时有序节点
    3. 持久化节点
    4. 持久化有序节点
- ##### Zookeeper Watch的事件类型
    1. NodeCreated
    2. NodeDeleted
    3. NodeDataChanged
    4. NodeChildrenChanged 新增或者删除才会触发,内容变化只会触发NodeDataChanged
- ##### Zookeeper ACL数据权限控制
    1. world 所有人
    2. auth 代表已经认证通过的用户
    3. digest 即用户名:
    4. ip 使用ip地址认证
- ##### Zookeeper的服务器角色类型
    1. leader
    2. follower 处理客户端非事务请求,转发事务请求给leader服务器, 参与事务请求proposal的投票</br>
       参与leader的选举投票
    3. obseaver 提供非事务请求的服务
- ##### Zookeeper特性
    1. 原子性: 所有事物请求的处理结果在整个集群中所有机器上的应用情况是一致的，要么成功，要么失败
    2. 单一视图性: 无论客户端连接的是哪个zookeeper服务器，其看到的服务端数据模型都是一致的
    3. 实时性: zookeeper并不是一种强一致性，只能保证顺序一致性和最终一致性，只能称为伪实时性
    4. 可靠性: 服务端成功的应用了一个事务，那么该事务所引起的服务端状态变更将会一直保留下来
    5. 顺序一致性: 从同一个客户端发起的事务请求，最终将会严格按照其发起顺序被应用到zookeeper中
------
#### ZAB协议
    ZAB协议的主要实现
    使用了单一主进程处理所有的事物请求,并采用ZAB的原子广播协议,将服务器数据的状态变更以Proposal的形式广播到所有副本进程上
    保证一个全局的变更序列被顺序应用
    
ZAB协议包括两种基本的模式
- 崩溃恢复 （当整个服务框架在启动过程中，或是当 Leader 服务器出现网络中断、崩溃退出与重启，等异常情况时， ZAB 协议就会进入恢复模式并选举产生新的 Leader 服务器。当选举产生了新的Leader 服务器同时集群中已经有过半的机器与该 Leader 服务器完成了状态同步之后，ZAB 协议就会退出恢复模式）
- 原子消息广播（针对客户端的事务请求， Leader 服务器会为其生成对应的事务 Proposal ,并将其发送给集群中其余所有的机器，然后再分別收集各自的选票，最后进行事务提交）