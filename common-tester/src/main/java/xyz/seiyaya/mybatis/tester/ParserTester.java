package xyz.seiyaya.mybatis.tester;

import org.apache.ibatis.parsing.XNode;
import org.apache.ibatis.parsing.XPathParser;
import org.apache.ibatis.reflection.DefaultReflectorFactory;
import org.apache.ibatis.reflection.MetaClass;
import org.junit.Test;
import xyz.seiyaya.mybatis.bean.UserBean;

import java.io.InputStream;
import java.util.Properties;

/**
 * 解析xml，并把xml转成对应的java对象
 * @author wangjia
 * @version 1.0
 * @date 2019/9/27 14:55
 */
public class ParserTester {

    /**
     * 用来测试xpath解析xml结点
     */
    @Test
    public void testXpath(){
        InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream("mybatis-config.xml");
        XPathParser parser = new XPathParser(resourceAsStream);
        XNode xNode = parser.evalNode("/configuration");
        XNode propertiesNode = xNode.evalNode("settings");
        Properties childrenAsProperties = propertiesNode.getChildrenAsProperties();
        System.out.println(childrenAsProperties);
    }

    /**
     * 用来测试mybatis的反射器和反射工厂DefaultReflectorFactory
     */
    @Test
    public void testMetaClassJudgeProperties(){
        MetaClass metaClass = MetaClass.forClass(UserBean.class, new DefaultReflectorFactory());

        boolean nameResult = metaClass.hasSetter("name");
        boolean usernameResult = metaClass.hasSetter("username");
        boolean accountCurrentBalanceResult = metaClass.hasSetter("accountList.currentBalance");

        System.out.println(String.format("nameResult:%s,\nusernameResult:%s,\naccountCurrentBalanceResult:%s",nameResult,usernameResult,accountCurrentBalanceResult));
    }
}
