package org.example.lurpc.fault.retry;

import javax.swing.*;

/**
 * @Author: 鹿又笑
 * @Create: 2024/6/15 16:48
 * @description:
 */
public interface RetryStrategyKeys {

    /**
     * 不重试
     */
    String NO = "no";

    /**
     * 固定时间间隔重试
     */
    String FIXED_INTERVAL = "fixedInterval";

}
