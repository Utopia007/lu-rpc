package org.example.common.service;

import org.example.common.model.User;

/**
 * @auther : LuYouxiao
 * @date 2024/3/31   -15:45
 * @Description
 */
public interface UserService {

    User getUser(User user);

    /**
     * 新方法 - 获取数字
     */
    default short getNumber() {
        return 1;
    }

}
