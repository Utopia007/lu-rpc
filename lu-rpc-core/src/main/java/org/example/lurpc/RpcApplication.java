package org.example.lurpc;

import org.example.lurpc.config.RegistryConfig;
import org.example.lurpc.config.RpcConfig;
import org.example.lurpc.constant.RpcConstant;
import org.example.lurpc.registry.EtcdRegistry;
import org.example.lurpc.registry.Registry;
import org.example.lurpc.registry.RegistryFactory;
import org.example.lurpc.util.ConfigUtils;

/**
 * @auther : LuYouxiao
 * @date 2024/4/2   -20:09
 * @Description 相当于 holder，存放了项目全局用到的变量
 */
//@Slf4j
public class RpcApplication {

    // 加 volatile 关键字主要用来避免重排序问题导致其他的线程看到了一个已经分配内存和地址但没有初始化的对象，
    // 也就是说这个对象还不是处于可用状态，就被其他线程引用了
    public static volatile RpcConfig rpcConfig;

    /**
     * 初始化
     */
    public static void init() {
        RpcConfig newRpcConfig;
        try {
            newRpcConfig = ConfigUtils.loadConfig(RpcConfig.class, RpcConstant.DEFAULT_CONFIG_PREFIX);
        } catch (Exception e) {
            // 配置加载失败，使用默认值
            newRpcConfig = new RpcConfig();
        }
        init(newRpcConfig);
    }

    /**
     * 框架初始化，支持传入自定义配置
     * @param newRpcConfig
     */
    private static void init(RpcConfig newRpcConfig) {
        rpcConfig = newRpcConfig;
        System.out.println("rpc init, config = {}" + newRpcConfig.toString());
        // 初始化注册中心
        RegistryConfig registryConfig = rpcConfig.getRegistryConfig();
        Registry registry = RegistryFactory.getInstance(registryConfig.getRegistry());
        registry.init(registryConfig);
        Runtime.getRuntime().addShutdownHook(new Thread(registry::destroy));
    }

    /**
     * 双检锁单例模式实现获取配置
     * @return
     */
    public static RpcConfig getRpcConfig() {
        if (rpcConfig == null) {
            synchronized (RpcApplication.class) {
                if (rpcConfig == null){
                    init();
                }
            }
        }
        return rpcConfig;
    }


}
