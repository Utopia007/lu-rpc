package org.example.lurpc.bootstrap;

import org.example.lurpc.RpcApplication;

/**
 * @Author: 鹿又笑
 * @Create: 2024/6/16 15:30
 * @description:
 */
public class ConsumerBootstrap {

    /**
     * 初始化
     */
    public static void init() {
        // RPC 框架初始化（配置和注册中心）
        RpcApplication.init();
    }


}
