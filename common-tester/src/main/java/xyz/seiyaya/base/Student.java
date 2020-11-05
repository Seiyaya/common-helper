package xyz.seiyaya.base;

import lombok.Data;
import xyz.seiyaya.common.cache.helper.DateHelper;

import java.util.Date;

/**
 * @author wangjia
 * @version 1.0
 * @date 2020/7/16 11:43
 */
@Data
public class Student {

    private Long id;

    private String name;

    private Date birthday;


    public String getBirthday(){
        if(birthday == null){
            return "";
        }
        return DateHelper.formatDate(birthday);
    }

    public Long getId(){
        return id + 1;
    }

    public static void main(String[] args) {
        Student student = new Student();
        student.setBirthday(new Date());
        student.setId(3L);
        System.out.println(student.getBirthday());
        System.out.println(student.getId());
    }
}
