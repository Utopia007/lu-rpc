package org.example.service.serviceImpl;

import org.example.common.model.User;
import org.example.common.service.UserService;
import org.example.lurpc.annotation.RpcService;
import org.springframework.stereotype.Service;

/**
 * @Author: 鹿又笑
 * @Create: 2024/6/17 9:40
 * @description:
 */
@Service
@RpcService
public class UserServiceImpl implements UserService {

    public User getUser(User user) {
        System.out.println("用户名：" + user.getName());
        return user;
    }
}