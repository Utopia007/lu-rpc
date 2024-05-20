package org.example;

import org.example.common.model.User;
import org.example.common.service.UserService;
import org.example.lurpc.proxy.ServiceProxyFactory;

/**
 * 服务消费者
 */
public class EasyConsumerApplication {
    public static void main(String[] args) {

        long startTime = System.currentTimeMillis();

        // todo 需要获取 UserService 的实现类对象
        UserService userService = ServiceProxyFactory.getProxy(UserService.class);
        User user = new User();
        user.setName("luyouxiao");
        System.out.println("userService.getUser(user) = " + userService.getUser(user));
        // 调用
        User newUser =userService.getUser(user);
        if (newUser != null) {
            System.out.println("123456" + newUser.getName());
        } else {
            System.out.println("user == null");
        }
//        short number = userService.getNumber();
//        System.out.println("number = " + number); //为0证明mock调用成功

        long endTime = System.currentTimeMillis();

        System.out.println("耗时-----" + (endTime - startTime));

    }
}
