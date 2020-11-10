package xyz.seiyaya.common.helper;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * @author seiyaya
 * @date 2019/8/13 15:31
 */
@Slf4j
public class StringTester {


    /**
     * @see StringHelper#splitCharTransToUpper(String)
     */
    @Test
    public void testSplitCharTransToUpper(){
        String name = "user_name";
        log.info("{}",StringHelper.splitCharTransToUpper(name));
    }
}
