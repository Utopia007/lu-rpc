package org.example;

import org.example.common.service.UserService;
import org.example.lurpc.RpcApplication;
import org.example.lurpc.registry.LocalRegistry;
import org.example.lurpc.server.HttpServer;
import org.example.lurpc.server.VertxHttpServer;
import org.example.provider.UserServiceImpl;

/**
 * Hello world!
 */
public class EasyProviderApplication {
    public static void main(String[] args) {

        // RPC 框架初始化
        RpcApplication.init();

        // 注册服务
        LocalRegistry.register(UserService.class.getName(), UserServiceImpl.class);

        HttpServer server = new VertxHttpServer();
//        server.doStart(8080);
        server.doStart(RpcApplication.getRpcConfig().getServerPort());

        System.out.println("Hello World!"+RpcApplication.getRpcConfig().getServerPort());
    }
}
