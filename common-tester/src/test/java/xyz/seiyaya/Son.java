package xyz.seiyaya;

import lombok.Data;

/**
 * @author wangjia
 * @version 1.0
 * @date 2019/11/29 17:22
 */
@Data
public class Son extends Parent {

    private String studentNo;

    public Son(){
    }

    public Son(String name ,Integer age ,String studentNo){
        setName(name);
        setAge(age);
        this.studentNo = studentNo;
    }
}
