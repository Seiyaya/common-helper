package xyz.seiyaya.common.annotation.handle;


/**
 * 对于特殊字段进行通用处理
 * @author seiyaya
 */
@FunctionalInterface
public interface CrawlHandler {


    /**
     * 将目标数据进行处理
     * @param item
     * @return
     */
    Object handle(Object item);
}
