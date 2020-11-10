package xyz.seiyaya.common.mybatis.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.builder.StaticSqlSource;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import xyz.seiyaya.common.mybatis.annotation.MybatisOptimisticLock;

import javax.persistence.OptimisticLockException;
import java.lang.reflect.Method;
import java.util.Properties;
import java.util.UUID;

/**
 * 乐观锁拦截器，为修改的sql添加set version = ? 和 where version = ?
 * 如果该条数据已经被修改，则抛出指定异常
 * @author wangjia
 * @version v1.0
 * @date 2020/11/10 8:48
 */
@Slf4j
@Intercepts({
        @Signature(type = Executor.class, method = "update", args = {
                MappedStatement.class, Object.class })})
public class OptimisticLockInterceptor implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
        Object parameter = null;
        if (invocation.getArgs().length > 1) {
            parameter = invocation.getArgs()[1];
        }
        BoundSql boundSql = mappedStatement.getBoundSql(parameter);
        String sql = boundSql.getSql();
        StringBuilder stringBuilder = new StringBuilder();
        int where = sql.indexOf("where");
        if(where == -1){
            where = sql.indexOf("WHERE");
        }
        String mappedStatementId = mappedStatement.getId();
        String methodName = mappedStatementId.substring(mappedStatementId.lastIndexOf(".")+1);
        String className = mappedStatementId.substring(0,mappedStatementId.lastIndexOf("."));
        Class<?> itemClass = Class.forName(className);
        Method[] methods = itemClass.getMethods();
        // 判断是否经过乐观锁拦截器的控制
        boolean flag = false;
        String newSql = null;
        for(Method method : methods){
            if(method.getName().equals(methodName)){
                MybatisOptimisticLock optimisticLock = method.getAnnotation(MybatisOptimisticLock.class);
                if(optimisticLock != null){
                    // 判断当前mapper的方法是否需要乐观锁
                    if(mappedStatement.getSqlCommandType() == SqlCommandType.UPDATE){
                        // 参数对象，从里面获取旧的version
                        Object oldObject = getOldValue(optimisticLock,mappedStatement,parameter);
                        if(oldObject == null){
                            break;
                        }
                        Object newObject = getNewValue(optimisticLock.versionType(),oldObject);
                        if(newObject == null){
                            break;
                        }
                        StringBuilder append = stringBuilder.append(sql, 0, where)
                                .append(String.format(" ,%s = %s ",optimisticLock.value(),newObject))
                                .append(sql, where, sql.length())
                                .append(String.format(" and %s = %s",optimisticLock.value(), oldObject));
                        // 这里不能直接设置BoundSql的sql字段为修改后的sql,执行sql都是重新获取的SqlBound,所以这里直接改变sqlSource
                        StaticSqlSource newSqlSource = new StaticSqlSource(mappedStatement.getConfiguration(),append.toString(),boundSql.getParameterMappings());
                        MetaObject metaObject = mappedStatement.getConfiguration().newMetaObject(mappedStatement);
                        metaObject.setValue("sqlSource",newSqlSource);
                        flag = true;
                        newSql = append.toString();
                        break;
                    }
                }
            }
        }
        Object proceed = invocation.proceed();
        if(flag && Integer.parseInt(proceed.toString()) == 0){
            throw new OptimisticLockException(String.format("mybatis乐观锁控制失败,newSql:[%s]",newSql.replaceAll("[\\s]+", " ")));
        }
        return proceed;
    }

    /**
     * 获取新的版本控制字段
     * @param versionType
     * @param oldObject
     * @return
     */
    private Object getNewValue(int versionType, Object oldObject) {
        switch (versionType){
            case 0:
                return String.format("'%s'",UUID.randomUUID().toString());
            case 1:
                int oldValue = Integer.parseInt(oldObject.toString());
                return oldValue+1;
            case 2:
                long longValue = Long.parseLong(oldObject.toString());
                return longValue+1;
            default:
        }
        return null;
    }

    /**
     * 获取旧的版本控制值
     * @param optimisticLock
     * @param mappedStatement
     * @param parameter
     * @return
     */
    private Object getOldValue(MybatisOptimisticLock optimisticLock,MappedStatement mappedStatement,Object parameter) {
        MetaObject itemObject = mappedStatement.getConfiguration().newMetaObject(parameter);
        if(!itemObject.hasGetter(optimisticLock.value())){
            return null;
        }
        Object versionValue = itemObject.getValue(optimisticLock.value());
        if(versionValue == null){
            return null;
        }
        if(optimisticLock.versionType() == 0){
            return String.format("'%s'",versionValue);
        }
        return versionValue;
    }

    @Override
    public Object plugin(Object target) {
        if( target instanceof Executor){
            return Plugin.wrap(target, this);
        }
        return target;
    }

    @Override
    public void setProperties(Properties properties) {

    }
}
