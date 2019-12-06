package xyz.seiyaya.mybatis.tester;

/**
 * @author wangjia
 * @version 1.0
 * @date 2019/12/6 8:53
 */
public class SqlSessionTemplateTester {

    public static void main(String[] args) {
        /**
         * SqlSessionTemplate是作为默认的DefaultSqlSession的代理类使用(sqlSession接口的实现)保证了线程安全
         * 如果是Spring管理事务会已经打开了Session会从事务管理器中回去session(SqlSessionHolder)
         */
    }
}
