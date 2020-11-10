package xyz.seiyaya.common.mybatis.helper;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.builder.MapperBuilderAssistant;
import org.apache.ibatis.builder.xml.XMLIncludeTransformer;
import org.apache.ibatis.builder.xml.XMLMapperEntityResolver;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.parsing.XNode;
import org.apache.ibatis.parsing.XPathParser;
import org.apache.ibatis.scripting.xmltags.XMLScriptBuilder;
import org.apache.ibatis.session.Configuration;

import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
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
     * mapper文件后缀
     */
    public static final String SUFFIX = "Mapper";


    /**
     * 获取sql相关信息
     * @param sqlId
     * @param param
     * @return
     * @throws Exception
     */
    public static SqlInfo getSqlInfo(String sqlId, Map<String, Object> param) throws Exception {
        return getSqlInfo(sqlId,param,"");
    }

    /**
     * 根据sqlId(即MappedStatement.id)
     * @param sqlId
     * @param param
     * @return
     */
    public static SqlInfo getSqlInfo(String sqlId , Map<String,Object> param, String subDir) throws Exception {
        if(sqlId == null || sqlId.isEmpty()){
            return null;
        }
        String namespace = sqlId.substring(0, sqlId.lastIndexOf("."));
        String fileName = namespace.substring(namespace.lastIndexOf(".")+1);
        if(!fileName.endsWith(SUFFIX)){
            throw new RuntimeException(String.format("请输入正确的mapper[%s],检查是否没有添加需要转换的sql片段",fileName));
        }
        String id = sqlId.substring(sqlId.lastIndexOf(".")+1);
        String resourceName = "mapper/" + subDir + "/" +fileName+".xml";
        URL resource = MybatisSqlHelper.class.getClassLoader().getResource(resourceName);
        if(resource == null){
            throw new RuntimeException(String.format("请检查配置mapper[%s]文件是否存在",resourceName));
        }
        String filePath = resource.getFile();
        InputStream inputStream = new FileInputStream(filePath);
        XPathParser parser = new XPathParser(inputStream, false, null, new XMLMapperEntityResolver());
        List<XNode> xNodeList = parser.evalNodes("mapper/select|mapper/update|mapper/delete|mapper/insert");
        List<XNode> sqlNodeList = parser.evalNodes("mapper/sql");
        SqlInfo sqlInfo = null;
        boolean flag = false;
        if(xNodeList != null && !xNodeList.isEmpty()){
            for(XNode xNode : xNodeList){
                if(id.equals(xNode.getStringAttribute("id"))){
                    sqlInfo = getSqlInfo(xNode,param,resourceName,namespace,sqlNodeList);
                    flag = true;
                    break;
                }
            }
        }
        if(!flag){
            throw new RuntimeException(String.format("请检查mapper文件是否有对应的sql片段,mapper[%s]   sqlId[%s]",resourceName,id));
        }
        return sqlInfo;
    }

    /**
     * 直接通过字符串来生成sql信息
     * @param xmlPart
     * @param param
     * @return
     */
    public static SqlInfo getSqlInfoByString(String xmlPart , Map<String,Object> param,String resourceName,String namespace){
        if(xmlPart == null || xmlPart.isEmpty()){
            return null;
        }

        XPathParser xPathParser = new XPathParser(xmlPart);
        XNode xNode = xPathParser.evalNode("select|update|delete|insert");

        return getSqlInfo(xNode,param,resourceName,namespace,null);
    }

    /**
     * 获取sqlInfo信息
     * @param xNode
     * @param param
     * @return
     */
    public static SqlInfo getSqlInfo(XNode xNode, Map<String,Object> param,String resource,String namespace,List<XNode> sqlNodeList){
        Configuration configuration = new Configuration();
        XMLScriptBuilder builder = new XMLScriptBuilder(configuration, xNode);
        // 将include标签替换到需要执行的代码段
        replaceInclude(configuration,xNode,resource,namespace,sqlNodeList);
        // 得到具体的sql
        SqlSource sqlSource = builder.parseScriptNode();
        BoundSql boundSql = sqlSource.getBoundSql(param);
        SqlInfo sqlInfo = convertSqlInfo(boundSql);
        return sqlInfo;
    }

    /**
     * 解析include标签并将它设置到configuration中
     * 此处暂时不考虑 <include/>标签中含有表达式的情况
     * @param configuration
     * @param xNode
     * @param resource
     * @param namespace
     * @param sqlNodeList
     */
    private static void replaceInclude(Configuration configuration,XNode xNode, String resource, String namespace, List<XNode> sqlNodeList) {
        MapperBuilderAssistant builderAssistant = new MapperBuilderAssistant(configuration, resource);
        builderAssistant.setCurrentNamespace(namespace);
        for (XNode context : sqlNodeList) {
            String id = context.getStringAttribute("id");
            id = builderAssistant.applyCurrentNamespace(id, false);
            configuration.getSqlFragments().put(id, context);
        }
        XMLIncludeTransformer includeParser = new XMLIncludeTransformer(configuration, builderAssistant);
        includeParser.applyIncludes(xNode.getNode());
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
}
