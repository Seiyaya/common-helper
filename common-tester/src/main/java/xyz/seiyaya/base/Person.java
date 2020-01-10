package xyz.seiyaya.base;

import lombok.Data;

/**
 * @author wangjia
 * @version 1.0
 * @date 2020/1/8 15:45
 */
public class Person {


    private String name;

    public Person(String name) {
        this.name = name;
    }

    public Person() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    protected void finalize() throws Throwable {
        System.out.println("person被回收了");
    }

    @Override
    public int hashCode() {
        return 13;
    }
}
