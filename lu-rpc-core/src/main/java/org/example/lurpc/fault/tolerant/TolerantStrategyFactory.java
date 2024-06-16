package org.example.lurpc.fault.tolerant;

import org.example.lurpc.spi.SpiLoader;

/**
 * @Author: 鹿又笑
 * @Create: 2024/6/16 9:38
 * @description:
 */
public class TolerantStrategyFactory {

    static {
        SpiLoader.load(TolerantStrategy.class);
    }

    /**
     * 默认容错策略
     */
    public static final TolerantStrategy DEFAULT_TOLERANTSTRATEGY = new FailFastTolerantStrategy();

    /**
     * 获取TolerantStrategy接口的实例对象
     *
     * @param key
     * @return
     */
    public static TolerantStrategy getInstance(String key) {
        return SpiLoader.getInstance(TolerantStrategy.class, key);
    }

}
