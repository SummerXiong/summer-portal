package com.summer.study.serialize.hessian;

import com.caucho.hessian.io.HessianInput;
import com.caucho.hessian.io.HessianOutput;
import com.summer.study.serialize.Person;
import com.summer.study.serialize.protobuf.ProtobufSerialize;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StopWatch;

import java.io.*;

/**
 * @author: create by Summer.Xiong
 * @version: v1.0
 * @description:
 *             hessian序列化后字节多,但序列化时间短,效率比protobuf还高
 * @date:2019/11/25
 */
public class HessianSerialize {

    private static Logger logger = LoggerFactory.getLogger(HessianSerialize.class);

    public static void main(String[] args) {

        Person person = new Person();
        person.setName("William");
        person.setAge(18);

        try {
            StopWatch stopWatch = new StopWatch();
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            HessianOutput ho = new HessianOutput(os);

            stopWatch.start();
            ho.writeObject(person);
            stopWatch.stop();
            logger.info("hessian序列化时间: " + stopWatch.getTotalTimeSeconds() + "s, size: " + os.toByteArray().length);

            HessianInput hi = new HessianInput(new ByteArrayInputStream(os.toByteArray()));
            Person person1 = (Person)hi.readObject();
            logger.info(person1.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
