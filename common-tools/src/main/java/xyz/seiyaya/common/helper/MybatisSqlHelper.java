package xyz.seiyaya.common.helper;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.builder.xml.XMLMapperEntityResolver;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.parsing.XNode;
import org.apache.ibatis.parsing.XPathParser;
import org.apache.ibatis.scripting.xmltags.XMLScriptBuilder;
import org.apache.ibatis.session.Configuration;
import org.assertj.core.util.Lists;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用来获取需要执行的sql
 * 调试过程中复制xml中的sql无法直接执行，可以通过传递的参数快速得到
 * @author wangjia
 * @version 1.0
 * @date 2019/12/6 16:39
 */
@Slf4j
public class MybatisSqlHelper {

    /**
     * 根据sqlId(即MappedStatement.id)
     * @param sqlId
     * @param param
     * @return
     */
    public static SqlInfo getSqlInfo(String sqlId , Map<String,Object> param) throws Exception {
        if(sqlId == null || sqlId.isEmpty()){
            return null;
        }
        String namespace = sqlId.substring(0, sqlId.lastIndexOf("."));
        String fileName = namespace.substring(namespace.lastIndexOf(".")+1);
        String id = sqlId.substring(sqlId.lastIndexOf(".")+1);
        InputStream inputStream = new FileInputStream(MybatisSqlHelper.class.getClassLoader().getResource("mapper/"+fileName+".xml").getFile());
        XPathParser parser = new XPathParser(inputStream, false, null, new XMLMapperEntityResolver());
        List<XNode> xNodeList = parser.evalNodes("mapper/select|mapper/update|mapper/delete|mapper/insert");
        SqlInfo sqlInfo = null;
        if(xNodeList != null && !xNodeList.isEmpty()){
            for(XNode xNode : xNodeList){
                if(id.equals(xNode.getStringAttribute("id"))){
                    sqlInfo = getSqlInfo(xNode,param);
                    break;
                }
            }
        }

        return sqlInfo;
    }

    /**
     * 直接通过字符串来生成sql信息
     * @param xmlPart
     * @param param
     * @return
     */
    public static SqlInfo getSqlInfoByString(String xmlPart , Map<String,Object> param){
        if(xmlPart == null || xmlPart.isEmpty()){
            return null;
        }

        XPathParser xPathParser = new XPathParser(xmlPart);
        XNode xNode = xPathParser.evalNode("select|update|delete|insert");
        return getSqlInfo(xNode,param);
    }

    /**
     * 获取sqlInfo信息
     * @param xNode
     * @param param
     * @return
     */
    public static SqlInfo getSqlInfo(XNode xNode, Map<String,Object> param){
        Configuration configuration = new Configuration();
        XMLScriptBuilder builder = new XMLScriptBuilder(configuration, xNode);
        SqlSource sqlSource = builder.parseScriptNode();
        BoundSql boundSql = sqlSource.getBoundSql(param);
        SqlInfo sqlInfo = convertSqlInfo(boundSql);
        return sqlInfo;
    }

    /**
     * 将boundSql转换成sqlInfo
     * @param boundSql
     */
    private static SqlInfo convertSqlInfo(BoundSql boundSql) {
        SqlInfo sqlInfo = new SqlInfo();
        //这里的sql是?号形式的
        sqlInfo.setSql(boundSql.getSql());

        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
        if(parameterMappings != null && !parameterMappings.isEmpty()){
            List<String> paramNameList = new ArrayList<>();
            List<Object> valueList = new ArrayList<>();
            for(ParameterMapping parameterMapping : parameterMappings){
                String key = parameterMapping.getProperty();
                Map parameterObject = (Map)boundSql.getParameterObject();
                Object value = null;
                if(boundSql.hasAdditionalParameter(key)){
                    // 这里主要是mybatis内部生成的参数名称获取，比如foreach
                    value = boundSql.getAdditionalParameter(key);
                }else if(parameterObject != null && parameterObject.containsKey(key)){
                    // 这里主要是外部传递过去的参数的map
                    value = parameterObject.get(key);
                }

                paramNameList.add(key);
                valueList.add(value);
            }
            sqlInfo.setParamNameList(paramNameList);
            sqlInfo.setValueList(valueList);
        }
        return sqlInfo;
    }


    @Data
    public static class SqlInfo {

        /**
         * 需要执行的sql
         */
        private String sql;

        /**
         * 参数名称信息
         */
        private List<String> paramNameList;

        /**
         * 参数值信息
         */
        private List<Object> valueList;

        public String formatSql(){
            //将value替换到每一个?上面
            if(valueList!= null && !valueList.isEmpty()){
                StringBuilder result = new StringBuilder();
                String[] split = (sql+";").split("\\?");
                for(int i=0;i<split.length-1;i++){
                    result.append(split[i]).append(valueList.get(i));
                }
                result.append(split[split.length-1]).deleteCharAt(result.length()-1);
                return result.toString();
            }
            return sql;
        }
    }

    public static void main(String[] args) throws Exception {
        Map<String,Object> map = new HashMap<>();
        map.put("id",1);
        ArrayList<Object> list = Lists.newArrayList();
        list.add(1);
        list.add(2);
        map.put("list",list);
        SqlInfo sqlInfo = MybatisSqlHelper.getSqlInfo("xyz.seiyaya.mybatis.mapper.UserBeanMapper.sqlExecuteWithMapParamsAndForeach", map);
        log.info("[{}]",sqlInfo.formatSql());
    }
}
