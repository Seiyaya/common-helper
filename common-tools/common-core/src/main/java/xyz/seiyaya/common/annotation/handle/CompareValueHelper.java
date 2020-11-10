package xyz.seiyaya.common.annotation.handle;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import xyz.seiyaya.common.annotation.UpdateLogInfo;
import xyz.seiyaya.common.helper.CollectionHelper;
import xyz.seiyaya.common.helper.DateHelper;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * @author wangjia
 * @version 1.0
 * @date 2020/11/3 10:14
 */
public class CompareValueHelper {

    /**
     * 比较对象
     * @param oldObj
     * @param newObj
     * @return
     * @throws Exception
     */
    public static List<String> compareValue(Object oldObj,Object newObj) throws Exception {
        if(oldObj == null){
            throw new NullPointerException("旧对象为null");
        }
        return compareValue(oldObj.getClass(),oldObj,newObj);
    }

    /**
     * 比较对象的修改
     * @param clazz
     * @param oldObj
     * @param newObj
     * @return
     * @throws Exception
     */
    public static List<String> compareValue(Class<?> clazz,Object oldObj, Object newObj) throws Exception {
        if (oldObj == null || newObj == null) {
            throw new RuntimeException("比较的值为空");
        }
        if (!oldObj.getClass().equals(newObj.getClass())) {
            throw new RuntimeException("比较的值不是同一个类型");
        }
        Field[] fields = clazz.getDeclaredFields();
        List<String> result = Lists.newArrayList();
        for (Field field : fields) {
            field.setAccessible(true);
            UpdateLogInfo updateLogInfo = field.getDeclaredAnnotation(UpdateLogInfo.class);
            if (updateLogInfo != null) {
                Object oldObject = field.get(oldObj);
                Object newObject = field.get(newObj);
                if (oldObject == null) {
                    oldObject = "";
                }
                if (newObject == null) {
                    newObject = "";
                }
                // 日期格式的处理
                if (Date.class.equals(field.getType())) {
                    // 日期类型的比较格式化之后的字符串
                    if (!"".equals(oldObject)) {
                        Date oldDate = (Date) oldObject;
                        oldObject = DateHelper.formatDate(oldDate, updateLogInfo.datePattern());
                    }
                    if (!"".equals(newObject)) {
                        Date newDate = (Date) newObject;
                        newObject = DateHelper.formatDate(newDate, updateLogInfo.datePattern());
                    }
                }
                if (List.class.equals(field.getType())) {
                    List<String> strings = compareListValue((List<Object>) field.get(oldObj), (List<Object>) field.get(newObj), updateLogInfo.value());
                    result.addAll(strings);
                    continue;
                }
                if (!oldObject.equals(newObject)) {
                    String updateDesc = String.format(updateLogInfo.updatePattern(), oldObject, newObject);
                    result.add(String.format("修改了%s %s", updateLogInfo.value(), updateDesc));
                }
            }
        }
        return result;
    }

    /**
     * 比较list
     * 新增、删除、修改
     *
     * @param oldList
     * @param newList
     * @return
     */
    public static List<String> compareListValue(List<Object> oldList, List<Object> newList,String attributeName) throws Exception {
        if(oldList == null){
            oldList = Lists.newArrayList();
        }
        if(newList == null){
            newList = Lists.newArrayList();
        }
        List<Object> addList = Lists.newArrayList();
        List<Object> delList = Lists.newArrayList();
        List<Object> updateOldList = Lists.newArrayList();
        List<Object> updateNewList = Lists.newArrayList();
        List<String> result = Lists.newArrayList();
        HashMap<Object,Object> newMap = Maps.newHashMap();
        newList.forEach(model-> newMap.put(model,model));
        for (Object oldObj : oldList) {
            if (newMap.containsKey(oldObj)) {
                updateOldList.add(oldObj);
                updateNewList.add(newMap.remove(oldObj));
            } else {
                delList.add(oldObj);
            }
        }
        addList.addAll(newMap.keySet());

        if(CollectionHelper.isNotEmpty(addList)){
            result.add(String.format("新增:%s",attributeName));
        }
        for(Object addObj : addList){
            List<String> updateString = compareValue(addObj.getClass(), addObj.getClass().newInstance(), addObj);
            for(int i=0;i<updateString.size();i++){
                updateString.set(i,"\t" + updateString.get(i));
            }
            result.addAll(updateString);
        }
        if(CollectionHelper.isNotEmpty(delList)){
            result.add(String.format("删除:%s",attributeName));
        }
        for(Object delObj : delList){
            List<String> updateString = compareValue(delObj.getClass(), delObj, delObj.getClass().newInstance());
            for(int i=0;i<updateString.size();i++){
                updateString.set(i,"\t" + updateString.get(i));
            }
            result.addAll(updateString);
        }
        if(CollectionHelper.isNotEmpty(updateOldList)){
            result.add(String.format("修改:%s",attributeName));
        }
        for(int i=0;i<updateOldList.size();i++){
            List<String> updateString = compareValue(updateOldList.get(i), updateNewList.get(i));
            for(int j=0;j<updateString.size();j++){
                updateString.set(j,"\t" + updateString.get(j));
            }
            result.addAll(updateString);
        }
        return result;
    }
}
