package org.example.lurpc.proxy;

import org.example.lurpc.RpcApplication;

import java.lang.reflect.Proxy;

/**
 * @auther : LuYouxiao
 * @date 2024/3/31   -18:56
 * @Description 根据指定类创建动态代理对象
 */
public class ServiceProxyFactory {

    /**
     * 根据服务类获取代理对象
     * @param serviceClass
     * @return
     * @param <T>
     */
    public static <T> T getProxy(Class<T> serviceClass) {

        if (RpcApplication.getRpcConfig().isMock()) {
            return getMockProxy(serviceClass);
        }

        return (T) Proxy.newProxyInstance(
                serviceClass.getClassLoader(),
                new Class[]{serviceClass},
                new ServiceProxy());
    }


    public static <T> T getMockProxy(Class<T> serverClass) {
        return (T) Proxy.newProxyInstance(
                serverClass.getClassLoader(),
                new Class[]{serverClass},
                new MockServiceProxy()
        );
    }

}
