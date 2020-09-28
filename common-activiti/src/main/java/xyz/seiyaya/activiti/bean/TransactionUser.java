package xyz.seiyaya.activiti.bean;


import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Table(name = "t_user")
public class TransactionUser {

    @Id
    private Long id;

    private String name;

    private Integer age;
}
