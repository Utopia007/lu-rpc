package org.example.service.serviceImpl;

import org.example.common.model.User;
import org.example.common.service.UserService;
import org.example.lurpc.annotation.RpcReference;
import org.springframework.stereotype.Service;

/**
 * @Author: 鹿又笑
 * @Create: 2024/6/17 9:45
 * @description:
 */
@Service
public class ExampleServiceImpl {

    @RpcReference
    private UserService userService;

    public void test() {
        User user = new User();
        user.setName("鹿又笑");
        User resultUser = userService.getUser(user);
        System.out.println(resultUser.getName());
    }

}