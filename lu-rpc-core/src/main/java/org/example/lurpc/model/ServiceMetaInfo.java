package org.example.lurpc.model;

import cn.hutool.core.util.StrUtil;
import lombok.Data;

/**
 * @auther : LuYouxiao
 * @date 2024/4/16   -19:47
 * @Description
 */
@Data
public class ServiceMetaInfo {

    /**
     * 服务名称
     */
    private String serviceName;

    /**
     * 服务版本号
     */
    private String serviceVersion = "1.0";

    /**
     * 服务地址
     */
    private String serviceAddress;

    /**
     * 服务分组（暂未实现）
     */
    private String serviceGroup = "default";

    /**
     * 服务域名
     */
    private String serviceHost;

    /**
     * 服务端口号
     */
    private Integer servicePort;

    /**
     * 获取服务键名
     * @return
     */
    public String getServerKey(){
        return String.format("%s:%s", serviceName, serviceVersion);
    }

    /**
     * 获取服务注册节点键名
     * @return
     */
    public String getServiceNodeKey(){
        return String.format("%s/%s:%s", getServerKey(), serviceHost, servicePort);
    }

    /**
     * 获取完整服务地址
     *
     * @return
     */
    public String getServiceAddress() {
        if (!StrUtil.contains(serviceHost, "http")) {
            return String.format("http://%s:%s", serviceHost, servicePort);
        }
        return String.format("%s:%s", serviceHost, servicePort);
    }

}
