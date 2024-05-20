package org.example;

import org.example.lurpc.config.RpcConfig;
import org.example.lurpc.util.ConfigUtils;

/**
 * @auther : LuYouxiao
 * @date 2024/4/2   -20:22
 * @Description
 */
public class ConsumerExample {

    public static void main(String[] args) {
        RpcConfig rpc = ConfigUtils.loadConfig(RpcConfig.class, "rpc");
        System.out.println("！！！初始化========" + rpc);
    }

}
