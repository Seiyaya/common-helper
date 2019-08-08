package xyz.seiyaya.mybatis.generate;

import freemarker.template.Configuration;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.DefaultResourceLoader;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

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
        String executeSQL;
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
        }
    }
}
