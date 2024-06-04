package org.example.lurpc.loadbalancer;

import org.example.lurpc.model.ServiceMetaInfo;

import java.util.List;
import java.util.Map;

/**
 * @Author: 鹿又笑
 * @Create: 2024/6/4 16:31
 * @description: 负载均衡器 （消费端使用）
 */
public interface LoadBalancer {

    /**
     * 选择服务调用
     *
     * @param requestParams
     * @param serviceMetaInfoList
     * @return
     */
    ServiceMetaInfo select(Map<String, Object> requestParams, List<ServiceMetaInfo> serviceMetaInfoList);


}
