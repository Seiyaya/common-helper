package xyz.seiyaya;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import xyz.seiyaya.common.helper.DateHelper;
import xyz.seiyaya.stock.StockApplication;
import xyz.seiyaya.stock.bean.Stock;
import xyz.seiyaya.stock.bean.dto.StockDto;
import xyz.seiyaya.stock.mapper.StockMapper;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 统计相关
 *
 * @author wangjia
 * @version 1.0
 * @date 2020/3/19 13:40
 */
@Slf4j
@RunWith(SpringRunner.class)
@EnableAutoConfiguration
@SpringBootTest(classes = StockApplication.class)
public class CountTester {

    @Resource
    private StockMapper stockMapper;

    private Map<Integer, Integer> totalCount = new HashMap<>();

    @Test
    public void testCountWeek() {
        List<Stock> byCondition = stockMapper.findByCondition(StockDto.builder().startDate(20020101).build());

        Map<Integer, Integer> count = new HashMap<>();

        Calendar instance = Calendar.getInstance();
        byCondition.forEach(model -> {
            String createDate = model.getCreateDate().toString();
            instance.set(Integer.parseInt(createDate.substring(0, 4)), Integer.parseInt(createDate.substring(4, 6)) - 1, Integer.parseInt(createDate.substring(5)));

            switch (instance.get(Calendar.DAY_OF_WEEK)) {
                case Calendar.MONDAY:
                    increase(count, Calendar.MONDAY,model);
                    break;
                case Calendar.TUESDAY:
                    increase(count, Calendar.TUESDAY,model);
                    break;
                case Calendar.WEDNESDAY:
                    increase(count, Calendar.WEDNESDAY,model);
                    break;
                case Calendar.THURSDAY:
                    increase(count, Calendar.THURSDAY,model);
                    break;
                case Calendar.FRIDAY:
                    increase(count, Calendar.FRIDAY,model);
                    break;
                case Calendar.SATURDAY:
                    increase(count, Calendar.SATURDAY,model);
                    break;
                case Calendar.SUNDAY:
                    increase(count, Calendar.SUNDAY,model);
                    break;
            }
        });

        count.forEach((k,v)->{
            Integer totalCountValue = totalCount.get(k);
            log.info("{} --> {}   result:{}" , k,v,new BigDecimal(v).divide(new BigDecimal(totalCountValue),5,BigDecimal.ROUND_HALF_UP));
        });
    }

    private void increase(Map<Integer, Integer> count, int monday,Stock model) {
        Integer orDefault = count.getOrDefault(monday, 0);
        if(model.getIncrease().compareTo(BigDecimal.ZERO) > 0 ){
            orDefault++;
            count.put(monday, orDefault);
        }

        Integer totalCountOrDefault = this.totalCount.getOrDefault(monday, 0);
        totalCountOrDefault++;
        totalCount.put(monday,totalCountOrDefault);
    }

    @Test
    public void testCalendar() {
        Calendar instance = Calendar.getInstance();
        String count = "20180203";

        instance.set(Integer.parseInt(count.substring(0, 4)), Integer.parseInt(count.substring(4, 6)) - 1, Integer.parseInt(count.substring(6)));
        System.out.println(DateHelper.formatDate(instance.getTime()));
    }

}
