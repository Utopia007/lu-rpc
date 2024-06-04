package org.example.lurpc.loadbalancer;

/**
 * @Author: 鹿又笑
 * @Create: 2024/6/4 17:48
 * @description:
 */
public interface LoadBalancerKeys {

    /**
     * 轮询
     */
    String ROUND_ROBIN = "roundRobin";

    /**
     * 随机
     */
    String RANDOM = "random";

    /**
     * 一致性Hash
     */
    String CONSISTENT_HASH = "consistentHash";

}
