package xyz.seiyaya.jieba;

import com.huaban.analysis.jieba.JiebaSegmenter;

import java.util.List;

/**
 * @author wangjia
 * @version 1.0
 * @date 2020/8/19 10:41
 */
public class JieBaDemo {

    public static void main(String[] args) {
        JiebaSegmenter segmenter = new JiebaSegmenter();
        List<String> strings = segmenter.sentenceProcess("黑夜总会来临");
        strings.forEach(System.out::println);


    }
}
