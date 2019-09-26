package xyz.seiyaya;

import org.assertj.core.util.Lists;
import org.junit.Test;

import java.util.ArrayList;

/**
 * Unit test for simple App.
 */
public class AppTest {
    /**
     * Rigorous Test :-)
     */
    @Test
    public void testPerformance() {
        ArrayList<Integer> score = Lists.newArrayList(75, 90, 90, 85, 90, 90, 90, 90);
        ArrayList<Integer> percents = Lists.newArrayList(65, 5, 5, 5, 5, 5, 5, 5);
        double sum = 0;
        double scoreResult = 0;
        for (int i = 0; i < score.size(); i++) {
            sum += score.get(i) * percents.get(i);
            scoreResult += score.get(i);
        }


        System.out.println(String.format("sumResult:%s  scoreResult:%s", sum / 100 , scoreResult));
    }
}
