package xyz.seiyaya;

import org.apache.ibatis.scripting.xmltags.ExpressionEvaluator;
import org.assertj.core.util.Lists;
import org.junit.Test;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import xyz.seiyaya.mybatis.bean.UserBean;

import java.io.File;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.*;
import java.util.stream.Stream;

/**
 * Unit test for simple App.
 */
public class AppTest {
    /**
     * Rigorous Test :-)
     */
    @Test
    public void testPerformance() {
        ArrayList<Integer> score = Lists.newArrayList(80, 90, 85, 80, 85, 90, 80, 80);
        ArrayList<Integer> percents = Lists.newArrayList(65, 5, 5, 5, 5, 5, 5, 5);
        double sum = 0;
        double scoreResult = 0;
        for (int i = 0; i < score.size(); i++) {
            sum += score.get(i) * percents.get(i);
            scoreResult += score.get(i);
        }


        System.out.println(String.format("sumResult:%s  scoreResult:%s", sum / 100 , scoreResult));
    }

    @Test
    public void testIf(){
        UserBean userBean = new UserBean();
        userBean.setId(0);
        ExpressionEvaluator expressionEvaluator = new ExpressionEvaluator();

        boolean b = expressionEvaluator.evaluateBoolean("id != null and id != ''", userBean);
        System.out.println(b);
    }

    @Test
    public void testSpringExpression(){
        ExpressionParser parser = new SpelExpressionParser();
        UserBean userBean = new UserBean();
        userBean.setName("zhangsan");
        Expression expression = parser.parseExpression("name.concat('123')");
        Object value = expression.getValue(userBean);
        System.out.println(value);
    }

    @Test
    public void testFilePath(){
        Properties properties = System.getProperties();
        Set<String> strings = properties.stringPropertyNames();
        for(String key : strings){
            String property = properties.getProperty(key);
            System.out.println(key+"--"+property);
        }

        String filePath = AppTest.class.getClassLoader().getResource("mapper/UserBeanMapper.xml").getFile();
        File file = new File(filePath);
        System.out.println(file.exists());
    }

    @Test
    public void testAddContent(){
        Stream.of(1, 2, 3, 4).filter(model -> model == 5).findFirst().ifPresent(System.out::println);
    }

    @Test
    public void testOptional(){
        BigDecimal bigDecimal = null;
        BigDecimal bigDecimal1 = Optional.ofNullable(bigDecimal).orElse(BigDecimal.ONE);
        System.out.println(bigDecimal1);
    }

    @Test
    public void testBigDecimal(){
        NumberFormat currency = NumberFormat.getNumberInstance();
        currency.setMinimumFractionDigits(2);
        BigDecimal bigDecimal = new BigDecimal("1000000");
        String format = currency.format(bigDecimal);
        System.out.println(format);
    }
    
    @Test
    public void testSubString(){
        String str = "23次";
        System.out.println(str.substring(0,str.length()-1));
    }

    @Test
    public void testStream(){
        List<String> v = new ArrayList<>();
        v.add("1");
        v.add("2");
        v.add("3");
        v.add("4");
        v.add("5");
        Map<String,String> companyOfficeMap = new HashMap<>();
        companyOfficeMap.put("3","1");
        companyOfficeMap.put("4","1");
        companyOfficeMap.put("5","1");
        long count = v.stream().map(companyOfficeMap::get).filter(Objects::nonNull).distinct().count();
        System.out.println(count);
    }
}
