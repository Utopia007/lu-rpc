package org.example;

import org.example.common.model.User;
import org.example.common.service.UserService;
import org.example.lurpc.bootstrap.ConsumerBootstrap;
import org.example.lurpc.proxy.ServiceProxyFactory;

/**
 * @Author: 鹿又笑
 * @Create: 2024/6/16 16:05
 * @description:
 */
public class ConsumerForBootstrap {

    public static void main(String[] args) {
        // 服务提供者初始化
//        ConsumerBootstrap.init();

        // 获取代理
        UserService userService = ServiceProxyFactory.getProxy(UserService.class);
        User user = new User();
        user.setName("yupi");
        // 调用
        User newUser = userService.getUser(user);
        if (newUser != null) {
            System.out.println("结果是："+newUser.getName());
        } else {
            System.out.println("user == null");
        }

    }

}
