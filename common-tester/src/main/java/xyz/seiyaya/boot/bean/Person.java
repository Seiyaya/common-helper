package xyz.seiyaya.boot.bean;

import lombok.Data;

/**
 * @author seiyaya
 * @date 2019/11/3 23:42
 */
@Data
public class Person {

    private String name;
    private int age;

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }
}
