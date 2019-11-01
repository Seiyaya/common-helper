package xyz.seiyaya.collection;

import java.util.Date;
import java.util.Objects;

/**
 * @author wangjia
 * @version 1.0
 * @date 2019/10/31 17:33
 */
public class HashA {

    private int code;
    private Date date;

    public HashA(int code){
        this.code = code;
        this.date = new Date();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HashA)) return false;
        HashA hashA = (HashA) o;
        return code == hashA.code &&
                Objects.equals(date, hashA.date);
    }

    @Override
    public int hashCode() {
        return code;
    }
}
