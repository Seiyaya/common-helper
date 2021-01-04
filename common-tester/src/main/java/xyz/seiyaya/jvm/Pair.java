package xyz.seiyaya.jvm;

/**
 * @author wangjia
 * @version v1.0
 * @date 2021/1/4 17:33
 */
public class Pair<K,V> {

    private K first;

    private V second;

    public Pair(K first,V second){
        this.first = first;
        this.second = second;
    }


    public K getFirst() {
        return first;
    }

    public void setFirst(K first) {
        this.first = first;
    }

    public V getSecond() {
        return second;
    }

    public void setSecond(V second) {
        this.second = second;
    }


    public static void main(String[] args) {
        // 数组类型不能进行泛型
//        Pair<String,String>[] pair = new Pair<>[5];
    }
}
