package xyz.seiyaya.mybatis.tester;

import org.junit.Test;
import xyz.seiyaya.common.helper.DateHelper;

import java.util.Calendar;

/**
 * @author wangjia
 * @version 1.0
 * @date 2019/9/29 17:18
 */
public class DataSourceTester {
    /**
     * mybatis的数据源主要有,配置是通过mybatis-config.xml中的/environments/environment/dataSource的type属性决定时使用线程池还是不用
     * @see org.apache.ibatis.datasource.unpooled.UnpooledDataSource
     * @see org.apache.ibatis.datasource.pooled.PooledDataSource
     */

    @Test
    public void testDate(){
        Calendar ca = Calendar.getInstance();
        ca.add(12, 15);

        System.out.println(DateHelper.formatDate(ca.getTime()));
    }
}
