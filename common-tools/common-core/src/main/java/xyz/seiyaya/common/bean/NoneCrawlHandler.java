package xyz.seiyaya.common.bean;

import xyz.seiyaya.common.annotation.handle.CrawlHandler;

/**
 * 用来表示null的特殊字段处理
 * @author seiyaya
 */
public class NoneCrawlHandler implements CrawlHandler {
    @Override
    public Object handle(Object item) {
        return null;
    }
}
