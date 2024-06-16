package org.example.lurpc.fault.retry;

import org.example.lurpc.spi.SpiLoader;

/**
 * 重试策略工厂
 *
 * @Author: 鹿又笑
 * @Create: 2024/6/15 16:51
 * @description:
 */
public class RetryStrategyFactory {

    static {
        SpiLoader.load(RetryStrategy.class);
    }

    /**
     * 默认重试器
     */
    public static final RetryStrategy DEFAULT_RETRY_STRATEGY = new NoRetryStrategy();

    public static RetryStrategy getInstance(String key) {
        return SpiLoader.getInstance(RetryStrategy.class, key);
    }

}
