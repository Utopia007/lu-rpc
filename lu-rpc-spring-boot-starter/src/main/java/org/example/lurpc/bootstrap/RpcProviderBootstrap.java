package org.example.lurpc.bootstrap;

import org.example.lurpc.RpcApplication;
import org.example.lurpc.annotation.RpcService;
import org.example.lurpc.config.RegistryConfig;
import org.example.lurpc.config.RpcConfig;
import org.example.lurpc.model.ServiceMetaInfo;
import org.example.lurpc.registry.LocalRegistry;
import org.example.lurpc.registry.Registry;
import org.example.lurpc.registry.RegistryFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

/**
 * RPC 服务提供者启动
 *
 * @Author: 鹿又笑
 * @Create: 2024/6/16 19:07
 * @description:
 */
public class RpcProviderBootstrap implements BeanPostProcessor {

    /**
     * 在 Bean 初始化后，通过反射获取到 Bean 的所有属性，如果属性包含 @RpcService 注解，那么就为该属性动态生成代理对象并赋值
     * @param bean
     * @param beanName
     * @return
     * @throws BeansException
     */
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class<?> beanClass = bean.getClass();
//        System.out.println("beanClass = " + beanClass);
        RpcService rpcService = beanClass.getAnnotation(RpcService.class);
//        System.out.println("rpcService = " + rpcService);
        if (rpcService != null) {
            // 需要注册服务
            // 1、获取服务基本信息
            Class<?> interfaceClass = rpcService.interfaceClass();
            System.out.println("服务端 interfaceClass = " + interfaceClass);
            // 默认值处理
            if (interfaceClass == void.class) {
                interfaceClass = beanClass.getInterfaces()[0];
            }
            // 注册服务
            String serviceName = interfaceClass.getName();
            String serviceVersion = rpcService.serviceVersion();
            // 本地注册
            LocalRegistry.register(serviceName, beanClass);
            // 全局配置
            final RpcConfig rpcConfig = RpcApplication.getRpcConfig();
            // 注册服务到注册中心
            RegistryConfig registryConfig = rpcConfig.getRegistryConfig();
            Registry registry = RegistryFactory.getInstance(registryConfig.getRegistry());
            ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
            serviceMetaInfo.setServiceName(serviceName);
            serviceMetaInfo.setServiceVersion(serviceVersion);
            serviceMetaInfo.setServiceHost(rpcConfig.getServerHost());
            serviceMetaInfo.setServicePort(rpcConfig.getServerPort());
            try {
                registry.registerService(serviceMetaInfo);
            } catch (Exception e) {
                throw new RuntimeException(serviceName + " 服务注册失败", e);
            }
        }
        return BeanPostProcessor.super.postProcessAfterInitialization(bean, beanName);
    }
}
