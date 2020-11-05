package xyz.seiyaya.common.cache.helper;

import org.junit.Test;

/**
 * @author wangjia
 * @version 1.0
 * @date 2019/10/9 13:51
 */
public class CollectionsTester {

    @Test
    public void testHashMapRemove(){
        DBParam param = new DBParam();
        for(int i=0;i<10;i++){
            param.put((i+1)+"",i);
        }

//        param.forEach((key,value)->{
//            if(Integer.parseInt(key) % 2 == 0){
//                param.remove(key);
//            }
//        });

        Object remove = param.remove("1");
        System.out.println(String.format("remove:%s",remove));

        System.out.println(param);
    }
}
