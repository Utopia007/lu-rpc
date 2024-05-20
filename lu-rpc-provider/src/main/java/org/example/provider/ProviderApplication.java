package org.example.provider;

import org.example.common.service.UserService;
import org.example.lurpc.RpcApplication;
import org.example.lurpc.config.RegistryConfig;
import org.example.lurpc.config.RpcConfig;
import org.example.lurpc.model.ServiceMetaInfo;
import org.example.lurpc.registry.LocalRegistry;
import org.example.lurpc.registry.Registry;
import org.example.lurpc.registry.RegistryFactory;
import org.example.lurpc.server.VertxHttpServer;

import java.util.concurrent.ExecutionException;

/**
 * @auther : LuYouxiao
 * @date 2024/4/18   -17:35
 * @Description
 */
public class ProviderApplication {

    public static void main(String[] args) {

        // rpc框架初始化
        RpcApplication.init();

        // 注册服务
        String serverName = UserService.class.getName();
        LocalRegistry.register(serverName, UserServiceImpl.class);

        // 注册服务到注册中心
        RpcConfig rpcConfig = new RpcConfig();
        RegistryConfig registryConfig = rpcConfig.getRegistryConfig();
        Registry registry = RegistryFactory.getInstance(registryConfig.getRegistry());
        ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
        serviceMetaInfo.setServiceName(serverName);
        serviceMetaInfo.setServiceHost("127.0.0.1");
        serviceMetaInfo.setServicePort(8888);
        serviceMetaInfo.setServiceAddress(rpcConfig.getServerHost() + ":" + rpcConfig.getServerPort());
        System.out.println("provider serviceMetaInfo.setServiceAddress = " + serviceMetaInfo.getServiceAddress());
        try {
            registry.registerService(serviceMetaInfo);
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        // 启动web服务
        VertxHttpServer vertxHttpServer = new VertxHttpServer();
        vertxHttpServer.doStart(RpcApplication.getRpcConfig().getServerPort());

    }

}
