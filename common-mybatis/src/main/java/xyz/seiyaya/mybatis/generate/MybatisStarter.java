package xyz.seiyaya.mybatis.generate;

import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.ui.Model;
import xyz.seiyaya.common.helper.DateHelper;
import xyz.seiyaya.common.helper.FileHelper;
import xyz.seiyaya.common.helper.FreeMarkerHelper;

import java.io.File;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.nio.charset.StandardCharsets.ISO_8859_1;
import static java.nio.charset.StandardCharsets.UTF_8;
import static xyz.seiyaya.common.helper.StringHelper.splitCharTransToUpper;
import static xyz.seiyaya.mybatis.generate.constant.GenerateConstants.DB_MYSQL;

/**
 * mybatis代码生成器
 * 利用freemarker生成
 * @author seiyaya
 * @date 2019/8/8 19:00
 */
@Slf4j
@SpringBootApplication
@PropertySource(value = "classpath:mybatis.yml")
public class MybatisStarter {

    @Value("${dbType}")
    private String dbType;

    @Value("${tableName}")
    private String tableName;
    @Value("${databaseName}")
    private String databaseName;
    @Value("${packageName}")
    private String packageName;

    @Value("${dbClass}")
    private String dbClass;
    @Value("${url}")
    private String dbUrl;
    @Value("${user}")
    private String username;
    @Value("${password}")
    private String password;


    @Value("${classNamePrefix}")
    private String classNamePrefix;
    @Value("${mybatisDir}")
    private String mybatisDir;
    @Value("${commonName}")
    private String commonName;
    @Value("${author}")
    private String classAuthor;
    @Value("${function}")
    private String classFunction;
    @Value("${moduleName}")
    private String moduleName;

    private static final String FILE_SEPARATOR = File.separator;


    public static void main(String[] args) throws Exception {

        ConfigurableApplicationContext run = SpringApplication.run(MybatisStarter.class, args);

        MybatisStarter bean = run.getBean(MybatisStarter.class);
        log.info("bean{}",bean);

        log.info(" code generate start");

        try {
            bean.init();
            bean.start();
        }catch (Exception e){
            log.info("",e);
            run.close();
        }

        log.info("code generate end");
        run.close();
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
        String executeSql = null;
        String showTableSql = null;
        if(DB_MYSQL.equals(dbType)){
            dbClass = "com.mysql.jdbc.Driver";
            showTableSql = "show tables";
            executeSql = String.format("SELECT COLUMN_NAME, DATA_TYPE, COLUMN_COMMENT FROM INFORMATION_SCHEMA.`COLUMNS` WHERE table_name = '%s' and table_schema= '%s'",tableName,databaseName);
        }

        Connection connection = DBHelper.getConnection(dbClass, dbUrl, username, password);
        Statement statement = connection.createStatement();
        ResultSet tableSet = statement.executeQuery(showTableSql);
        while(tableSet.next()){
            String dbTableName = tableSet.getString(1);
            if(!tableName.equals(dbTableName)){
                continue;
            }

            //将首字母大写
            //需要将className的_转为空，且后面第一个字母大写
            String className = convertClassName(classNamePrefix);
            File projectPath = new DefaultResourceLoader().getResource("").getFile();
            while (!new File(projectPath.getPath() + FILE_SEPARATOR + "src").exists()) {
                projectPath = projectPath.getParentFile();
            }

            //模版文件路径
            String templatePath = StringUtils.replace(projectPath + "/src/main/resources/template/", "/", FILE_SEPARATOR);

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
            params.put("functionName", new String(classFunction.getBytes(ISO_8859_1), UTF_8));
            params.put("tableName", StringUtils.lowerCase(tableName));
            params.put("moduleName",moduleName);
            List<Column> columnList = getList(connection, executeSql);
            params.put("list", columnList);
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
            String mapperPath = batisXMLPath+FILE_SEPARATOR+params.get("ClassName")+"Mapper.java";
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
     * 将className转成驼峰形式
     * @param className
     * @return
     */
    private String convertClassName(String className) {
        String[] classNames = className.split("_");
        StringBuilder result = new StringBuilder();
        for(String name : classNames){
            if(!"t".equals(name)){
                result.append(name.substring(0, 1).toUpperCase() + name.substring(1));
            }
        }
        return result.toString();
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
