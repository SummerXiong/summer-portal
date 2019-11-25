package com.summer.study.serialize.protobuf;

import com.baidu.bjf.remoting.protobuf.Codec;
import com.baidu.bjf.remoting.protobuf.ProtobufProxy;
import com.summer.study.serialize.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StopWatch;

import java.io.IOException;

/**
 * @author: create by Summer.Xiong
 * @version: v1.0
 * @description:
 *          protobuf序列化后字节少,网络传输快，且序列化时间短,效率较高
 * @date:2019/11/25
 */
public class ProtobufSerialize {

    private static Logger logger = LoggerFactory.getLogger(ProtobufSerialize.class);

    public static void main(String[] args) {
        try {
            StopWatch stopWatch = new StopWatch();

            Person person = new Person();
            person.setName("William");
            person.setAge(18);

            //此段代码比较耗时
            Codec<Person> personCodec = ProtobufProxy.create(Person.class, false);

            stopWatch.start();
            byte[] bytes = null;
            for (int i = 0; i <= 100; i++){
                bytes = personCodec.encode(person);
            }
            stopWatch.stop();
            logger.info("protobuf序列化时间: " + stopWatch.getTotalTimeSeconds() + "s, siez: " + bytes.length);

            Person person1 = personCodec.decode(bytes);
            logger.info(person1.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
