package org.example.lurpc.config;

import lombok.Data;
import org.example.lurpc.fault.tolerant.TolerantStrategyKeys;
import org.example.lurpc.loadbalancer.LoadBalancerKeys;
import org.example.lurpc.fault.retry.RetryStrategyKeys;
import org.example.lurpc.serializer.SerializerKeys;

/**
 * @auther : LuYouxiao
 * @date 2024/4/1   -11:49
 * @Description Rpc框架配置
 */
@Data
public class RpcConfig {

    /**
     * 名称
     */
    private String name = "lu-rpc";

    /**
     * 版本号
     */
    private String version = "1.0";

    /**
     * 服务器主机名
     */
    private String serverHost = "localhost";

    /**
     * 服务器端口号
     */
    private Integer serverPort = 8888;

    /**
     * 模拟调用开启mock
     */
    private boolean mock = false;

    /**
     * 序列化器
     */
    private String serializer = SerializerKeys.JDK;

    /**
     * 注册中心配置
     */
    private RegistryConfig registryConfig = new RegistryConfig();

    /**
     * 负载均衡器
     */
    private String loadBalancer = LoadBalancerKeys.ROUND_ROBIN;

    /**
     * 重试器
     */
    private String retryStrategy = RetryStrategyKeys.NO;

    /**
     * 容错机制
     */
    private String tolerantStrategy = TolerantStrategyKeys.FAIL_FAST;

}
