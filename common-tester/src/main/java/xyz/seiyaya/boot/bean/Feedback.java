package xyz.seiyaya.boot.bean;

import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * @author wangjia
 * @version 1.0
 * @date 2020/1/7 11:15
 */
@Table(name = "t_feedback")
@Data
public class Feedback implements Serializable {

    @Id
    private Long id;

    private String content;

    private Long userId;
}
