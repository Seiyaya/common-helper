package xyz.seiyaya;

import org.junit.Test;
import xyz.seiyaya.activiti.bean.TransactionUser;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue()
    {
        int i = new BigDecimal("123.12").intValue();
        System.out.println(i);
    }


    @Test
    public void testUser(){
        List<TransactionUser> transactionUsers = new ArrayList<>();
        for(int i=0;i<10;i++){
            TransactionUser transactionUser = new TransactionUser();
            transactionUser.setId((long) i);
            transactionUser.setName(i+" zhang");
            transactionUsers.add(transactionUser);
        }

        transactionUsers.add(new TransactionUser(1L,"zhang"));

        Map<Long, TransactionUser> collect = transactionUsers.stream().collect(Collectors.toMap(TransactionUser::getId, e -> e));

        System.out.println(collect);
    }
}
