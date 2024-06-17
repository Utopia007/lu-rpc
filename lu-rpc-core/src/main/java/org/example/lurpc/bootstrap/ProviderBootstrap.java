package org.example.lurpc.bootstrap;

import org.example.lurpc.RpcApplication;
import org.example.lurpc.config.RegistryConfig;
import org.example.lurpc.config.RpcConfig;
import org.example.lurpc.model.ServiceMetaInfo;
import org.example.lurpc.model.ServiceRegisterInfo;
import org.example.lurpc.registry.LocalRegistry;
import org.example.lurpc.registry.Registry;
import org.example.lurpc.registry.RegistryFactory;
import org.example.lurpc.server.VertxHttpServer;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * @Author: 鹿又笑
 * @Create: 2024/6/16 14:56
 * @description:
 */
public class ProviderBootstrap {

    public static void init(List<ServiceRegisterInfo<?>> serviceRegisterInfoList) {
        // rpc框架初始化
        RpcApplication.init();

        // 全局配置
        final RpcConfig rpcConfig = RpcApplication.getRpcConfig();

        // 注册服务
        for (ServiceRegisterInfo<?> serviceRegisterInfo : serviceRegisterInfoList) {
            String serviceName = serviceRegisterInfo.getServiceName();
            // 本地注册
            LocalRegistry.register(serviceName, serviceRegisterInfo.getImplClass());
            // 注册服务到注册中心
            RegistryConfig registryConfig = rpcConfig.getRegistryConfig();
            Registry registry = RegistryFactory.getInstance(registryConfig.getRegistry());
            ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
            serviceMetaInfo.setServiceName(serviceName);
            serviceMetaInfo.setServiceHost(rpcConfig.getServerHost());
            serviceMetaInfo.setServicePort(rpcConfig.getServerPort());
            serviceMetaInfo.setServiceAddress(rpcConfig.getServerHost() + ":" + rpcConfig.getServerPort());
            try {
                registry.registerService(serviceMetaInfo);
            } catch (ExecutionException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        // 启动web服务
        VertxHttpServer vertxHttpServer = new VertxHttpServer();
        vertxHttpServer.doStart(RpcApplication.getRpcConfig().getServerPort());

    }

}
