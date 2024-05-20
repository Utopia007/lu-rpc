package org.example.lurpc.util;

import cn.hutool.core.util.StrUtil;
import cn.hutool.setting.dialect.Props;

/**
 * @auther : LuYouxiao
 * @date 2024/4/1   -21:17
 * @Description
 */
public class ConfigUtils {

    /**
     * 配置工具类
     * @param tClass
     * @param prefix
     * @return
     * @param <T>
     */
    public static <T> T loadConfig(Class<T> tClass, String prefix) {
        return loadConfig(tClass, prefix, "");
    }

    /**
     * 加载配置对象，支持区分环境
     * @param tClass
     * @param prefix
     * @param environment
     * @return
     * @param <T>
     */
    private static <T> T loadConfig(Class<T> tClass, String prefix, String environment) {

        StringBuilder builder = new StringBuilder("application");
        if (StrUtil.isNotBlank(environment)) {
            builder.append("-").append(environment);
        }
        builder.append(".properties");
        System.out.println("builder = " + builder);
        Props props = new Props(builder.toString());//properties
        // 将配置文件转换为Bean
        return props.toBean(tClass, prefix);

    }

}
