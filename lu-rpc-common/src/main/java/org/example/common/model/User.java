package org.example.common.model;

import java.io.Serializable;

/**
 * @auther : LuYouxiao
 * @date 2024/3/31   -15:45
 * @Description
 */
public class User implements Serializable {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
