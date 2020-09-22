package xyz.seiyaya.proxy.impl;

import xyz.seiyaya.proxy.Test;

/**
 * @author wangjia
 * @version 1.0
 * @date 2020/9/21 14:42
 */
public class TestImpl implements Test {

    @Override
    public int test(int i) {
        return i+1;
    }
}
