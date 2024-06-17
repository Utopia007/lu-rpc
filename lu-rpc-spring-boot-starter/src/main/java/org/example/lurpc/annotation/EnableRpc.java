package org.example.lurpc.annotation;

import org.example.lurpc.bootstrap.RpcConsumerBootstrap;
import org.example.lurpc.bootstrap.RpcInitBootstrap;
import org.example.lurpc.bootstrap.RpcProviderBootstrap;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author: 鹿又笑
 * @Create: 2024/6/16 16:35
 * @description:
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Import({RpcInitBootstrap.class, RpcProviderBootstrap.class, RpcConsumerBootstrap.class})
public @interface EnableRpc {

    /**
     * 需要启动server
     *
     * @return
     */
    boolean needService() default true;

}
