package org.example.lurpc.bootstrap;

import org.example.lurpc.RpcApplication;
import org.example.lurpc.annotation.EnableRpc;
import org.example.lurpc.config.RpcConfig;
import org.example.lurpc.server.tcp.VertxTCPServer;
import org.example.lurpc.server.tcp.VertxTcpClient;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

/**
 * @Author: 鹿又笑
 * @Create: 2024/6/16 18:23
 * @description:
 */
public class RpcInitBootstrap implements ImportBeanDefinitionRegistrar {

    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        // 获取 EnableRpc 注解的属性值
        boolean needService = (boolean) importingClassMetadata.getAnnotationAttributes(EnableRpc.class.getName()).get("needService");
        // RPC框架初始化
        RpcApplication.init();
        // 全局配置
        final RpcConfig rpcConfig = RpcApplication.getRpcConfig();
        if (needService) {
            VertxTCPServer vertxTCPServer = new VertxTCPServer();
            vertxTCPServer.doStart(rpcConfig.getServerPort());
        } else {
            System.out.println("不启动 service");
        }

    }

}
