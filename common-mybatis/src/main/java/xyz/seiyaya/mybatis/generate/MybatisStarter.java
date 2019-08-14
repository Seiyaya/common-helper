package xyz.seiyaya.mybatis.generate;

import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.ui.Model;
import xyz.seiyaya.common.helper.DateHelper;
import xyz.seiyaya.common.helper.FileHelper;
import xyz.seiyaya.common.helper.FreeMarkerHelper;

import java.io.File;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static xyz.seiyaya.common.helper.StringHelper.splitCharTransToUpper;

/**
 * mybatis代码生成器
 * 利用freemarker生成
 * @author seiyaya
 * @date 2019/8/8 19:00
 */
@Slf4j
public class MybatisStarter {

    private String dbType;

    private String tableName;
    private String databaseName;
    private String packageName;


    private String dbClass = "";
    private String dbUrl;
    private String username;
    private String password;


    private String configTable;
    private String classNamePrefix;
    private String mybatisDir;
    private String commonName;
    private String classAuthor;
    private String classFunction;

    private static final String FILE_SEPARATOR = File.separator;


    public static void main(String[] args) throws Exception {


        log.info("代码生成开始====================");

        MybatisStarter starter = new MybatisStarter();
        starter.init();
        starter.start();

        log.info("代码生成结束====================");
    }

    /**
     * 读取配置
     */
    private void init() {
    }

    /**
     * 开始生成
     */
    private void start() throws Exception {
        String executeSQL = null;
        String showTableSql = null;
        if("mysql".equals(dbType)){
            dbClass = "com.mysql.jdbc.Driver";
            showTableSql = "show tables";
            executeSQL = "SELECT COLUMN_NAME, DATA_TYPE, COLUMN_COMMENT FROM INFORMATION_SCHEMA.`COLUMNS` WHERE table_name = '"+tableName+" ' and table_scheme= '"+databaseName+"'";
        }

        Connection connection = DBHelper.getConnection(dbClass, dbUrl, username, password);
        Statement statement = connection.createStatement();
        ResultSet tableSet = statement.executeQuery(showTableSql);
        while(tableSet.next()){
            String dbTableName = tableSet.getString(0);
            if(!configTable.equals(dbTableName)){
                continue;
            }

            //将首字母大写
            String className = new StringBuilder(classNamePrefix.substring(0,1).toUpperCase()).append(classNamePrefix.substring(1,classNamePrefix.length())).toString();
            File projectPath = new DefaultResourceLoader().getResource("").getFile();
            while (!new File(projectPath.getPath() + FILE_SEPARATOR + "src").exists()) {
                projectPath = projectPath.getParentFile();
            }

            //模版文件路径
            String templatePath = StringUtils.replace(projectPath + "/main/resources/template/mysql", "/", FILE_SEPARATOR);

            //java文件路径
            String javaPath = StringUtils.replaceEach(projectPath + "/src/main/java/" + StringUtils.lowerCase(packageName), new String[] { "/", "." },
                    new String[] { FILE_SEPARATOR, FILE_SEPARATOR });

            //xml文件生成路径
            String batisXMLPath = StringUtils.replace(projectPath + "/src/main/resources/" + mybatisDir , "/", FILE_SEPARATOR);

            Configuration cfg = new Configuration();
            cfg.setDirectoryForTemplateLoading(new File(templatePath));
            Map<String,Object> params = new HashMap<>();
            params.put("commonName",StringUtils.lowerCase(commonName));
            params.put("packageName", StringUtils.lowerCase(packageName));
            params.put("className", StringUtils.uncapitalize(className));
            params.put("ClassName", className);
            params.put("classAuthor", classAuthor);
            params.put("classDate", DateHelper.formatNowDate());
            params.put("functionName", classFunction);
            params.put("tableName", StringUtils.lowerCase(tableName));
            List<Column> columnList = getList(connection, executeSQL);
            params.put("list", getList(connection,executeSQL));
            params.put("listSize", columnList.size());
            // mybatis {替代单词
            params.put("leftBraces", "{");
            // mybatis }替代单词
            params.put("rightBraces", "}");
            // mybatis $替代单词
            params.put("dollarSign", "$");

            //生成mybatis文件
            String mybatisContent = FreeMarkerHelper.renderTemplate(cfg.getTemplate("dbMapper.ftl"), params);
            String mybatisPath = batisXMLPath+FILE_SEPARATOR+params.get("ClassName")+"Mapper.xml";
            FileHelper.writeFile(mybatisContent,mybatisPath);

            //生成bean文件
            String beanContent = FreeMarkerHelper.renderTemplate(cfg.getTemplate("bean.ftl"), params);
            String beanPath = batisXMLPath+FILE_SEPARATOR+params.get("ClassName")+".java";
            FileHelper.writeFile(beanContent,beanPath);

            //生成接口mapper
            String mapperContent = FreeMarkerHelper.renderTemplate(cfg.getTemplate("mapper.ftl"), params);
            String mapperPath = batisXMLPath+FILE_SEPARATOR+params.get("ClassName")+".java";
            FileHelper.writeFile(mapperContent,mapperPath);

            //生成service接口和service实现类
            String serviceContent = FreeMarkerHelper.renderTemplate(cfg.getTemplate("service.ftl"), params);
            String servicePath = batisXMLPath+FILE_SEPARATOR+params.get("ClassName")+"Service.java";
            FileHelper.writeFile(serviceContent,servicePath);

            String serviceImplContent = FreeMarkerHelper.renderTemplate(cfg.getTemplate("serviceImpl.ftl"), params);
            String serviceImplPath = batisXMLPath+FILE_SEPARATOR+params.get("ClassName")+"ServiceImpl.java";
            FileHelper.writeFile(serviceImplContent,serviceImplPath);
        }

        connection.close();
        statement.close();
    }

    /**
     * 获取对应数据库上的表的所有字段
     * @param connection
     * @param executeSQL
     * @return
     */
    private List<Column> getList(Connection connection, String executeSQL) {
        List<Column> list = new ArrayList<>();
        Statement statement = null;
        try {
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(executeSQL);

            while(resultSet.next()){
                Column table = new Column();
                table.setTypeName(StringUtils.lowerCase(resultSet.getString("COLUMN_NAME")));
                // 将所有的字段名改为小写方便命名
                table.setColumnName(StringUtils.uncapitalize(splitCharTransToUpper((StringUtils.lowerCase(resultSet.getString("COLUMN_NAME"))))));
                table.setColumnNameUpper(StringUtils.capitalize(table.getColumnName()));
                table.setDataType(sqlTypeToJavaType(resultSet.getString("DATA_TYPE")));
                table.setJdbcType(sqlTypeToJavaType(resultSet.getString("DATA_TYPE")));
                table.setColumnComment(resultSet.getString("COLUMN_COMMENT"));
                list.add(table);
            }
        } catch (SQLException e) {
            log.error("出现异常",e);
        }finally {
            if(statement != null){
                try {
                    statement.close();
                } catch (SQLException e) {
                    log.error("关闭statement出现异常",e);
                }
            }
        }
        return list;
    }

    /**
     * sql的类型转为java里面的数据类型
     * @param sqlType
     * @return
     */
    private String sqlTypeToJavaType(String sqlType) {
        if ("bit".equalsIgnoreCase(sqlType)) {
            return "Boolean";
        } else if ("tinyint".equalsIgnoreCase(sqlType)) {
            return "Integer";
        } else if ("smallint".equalsIgnoreCase(sqlType)) {
            return "short";
        } else if ("int".equalsIgnoreCase(sqlType)) {
            return "Integer";
        } else if ("bigint".equalsIgnoreCase(sqlType)) {
            return "Long";
        } else if ("float".equalsIgnoreCase(sqlType)) {
            return "float";
        } else if ("decimal".equalsIgnoreCase(sqlType) || "numeric".equalsIgnoreCase(sqlType)
                || "real".equalsIgnoreCase(sqlType) || "money".equalsIgnoreCase(sqlType)
                || "smallmoney".equalsIgnoreCase(sqlType) || "double".equalsIgnoreCase(sqlType)) {
            return "Double";
        } else if ("varchar".equalsIgnoreCase(sqlType) || "char".equalsIgnoreCase(sqlType)
                || "nvarchar".equalsIgnoreCase(sqlType) || "nchar".equalsIgnoreCase(sqlType)
                || "text".equalsIgnoreCase(sqlType)) {
            return "String";
        } else if ("datetime".equalsIgnoreCase(sqlType) || "date".equalsIgnoreCase(sqlType)) {
            return "Date";
        } else if ("image".equalsIgnoreCase(sqlType)) {
            return "Blod";
        }
        return "String";
    }
}
