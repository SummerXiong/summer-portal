package com.summer.study.serialize;

import com.baidu.bjf.remoting.protobuf.FieldType;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import lombok.Data;

import java.io.Serializable;

/**
 * @author: create by Summer.Xiong
 * @version: v1.0
 * @description: com.summer.study.serialize
 * @date:2019/11/25
 */
@Data
public class Person implements Serializable {

    @Protobuf(fieldType = FieldType.STRING)
    private String name;

    @Protobuf(fieldType = FieldType.INT32)
    private Integer age;

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}
