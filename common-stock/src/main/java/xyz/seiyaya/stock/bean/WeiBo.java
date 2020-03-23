package xyz.seiyaya.stock.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author wangjia
 * @version 1.0
 * @date 2020/3/18 17:46
 */
@Table(name = "t_weibo")
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class WeiBo {

    @Id
    private Long id;

    private String createAt;

    private String text;
}
