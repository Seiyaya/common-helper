package xyz.seiyaya.log;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.ThrowableProxy;
import ch.qos.logback.core.filter.Filter;
import ch.qos.logback.core.spi.FilterReply;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;

/**
 * @author wangjia
 * @version 1.0
 * @date 2020/11/2 9:53
 */
@Slf4j
public class ExceptionLogFilter extends Filter<ILoggingEvent> {

    @Override
    public FilterReply decide(ILoggingEvent iLoggingEvent) {
        // 获取前三层堆栈
        ThrowableProxy throwableProxy = (ThrowableProxy)iLoggingEvent.getThrowableProxy();
        Throwable throwable = throwableProxy.getThrowable();
        if(throwable instanceof RuntimeException){
            StackTraceElement[] stackTrace = throwable.getStackTrace();
            System.out.printf("发生异常:%s \t 下面是堆栈信息:\n", throwable.getClass().getSimpleName());
            for(int i=0;i<stackTrace.length;i++){
                System.out.printf("className:%s \t methodName:%s \t  line:%s \n",
                        stackTrace[i].getClassName(),
                        stackTrace[i].getMethodName(),
                        stackTrace[i].getLineNumber());
            }
        }
        return FilterReply.NEUTRAL;
    }

    public static void main(String[] args) {
        try {
            log.info("打印一下");
            List<Integer> integers = Arrays.asList(1, 2, 3);
            System.out.println(integers.remove(0));
            throw new RuntimeException();
        }catch (Exception e){
            log.error("",e);
        }
    }
}
