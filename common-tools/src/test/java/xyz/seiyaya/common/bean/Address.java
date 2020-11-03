package xyz.seiyaya.common.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import xyz.seiyaya.common.annotation.DictFormat;
import xyz.seiyaya.common.annotation.UpdateLogInfo;

import java.util.Objects;

/**
 * @author wangjia
 * @version 1.0
 * @date 2019/10/16 11:40
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Address {

    private Long id;

    @UpdateLogInfo("详细地址")
    private String addressName;

    @UpdateLogInfo("路程")
    private Integer route;

    @DictFormat(type = "ADDRESS_STATUS",fieldName = "statusStr")
    private Integer status;

    public Address(Long id, String addressName, Integer route) {
        this.id = id;
        this.addressName = addressName;
        this.route = route;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Address)) return false;
        Address address = (Address) o;
        return id.equals(address.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
