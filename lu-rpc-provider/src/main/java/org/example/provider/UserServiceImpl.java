package org.example.provider;

import org.example.common.model.User;
import org.example.common.service.UserService;

/**
 * @auther : LuYouxiao
 * @date 2024/3/31   -16:03
 * @Description
 */
public class UserServiceImpl implements UserService {

    @Override
    public User getUser(User user) {
        System.out.println("@@user = " + user);
        return user;
    }
}
