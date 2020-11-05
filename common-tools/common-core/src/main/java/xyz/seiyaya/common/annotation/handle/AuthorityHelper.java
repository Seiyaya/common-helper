package xyz.seiyaya.common.annotation.handle;

import xyz.seiyaya.common.annotation.AuthorityTableData;
import xyz.seiyaya.common.bean.AuthorityHeadTitle;
import xyz.seiyaya.common.bean.AuthorityInfo;
import xyz.seiyaya.common.cache.helper.StringHelper;

import java.lang.reflect.Field;

/**
 * 返回给前端的权限工具类，主要是用来解析TableData
 * @author wangjia
 * @version 1.0
 * @date 2019/11/27 15:03
 */
public class AuthorityHelper {


    public AuthorityInfo getAuthorityField(Class<?> clazz){
        AuthorityInfo authorityInfo = new AuthorityInfo();

        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields ){
            field.setAccessible(true);
            AuthorityTableData authorityTableData = field.getAnnotation(AuthorityTableData.class);
            if(authorityTableData!=null){
                AuthorityHeadTitle headTitle = getHeadTitle(field,authorityTableData);
            }
        }
        return authorityInfo;
    }

    /**
     * 封装前端表格的一列
     * @param field
     * @param authorityTableData
     * @return
     */
    private AuthorityHeadTitle getHeadTitle(Field field, AuthorityTableData authorityTableData) {
        AuthorityHeadTitle headTitle = new AuthorityHeadTitle();
        if(StringHelper.isNotEmpty(authorityTableData.dbField())){
            headTitle.setDbField(authorityTableData.dbField());
        }
        if(StringHelper.isNotEmpty(authorityTableData.title())){
            headTitle.setFieldName(authorityTableData.title());
        }
        headTitle.setFiled(field.getName());
        return headTitle;
    }
}
