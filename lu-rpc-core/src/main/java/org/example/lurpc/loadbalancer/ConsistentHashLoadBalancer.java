package org.example.lurpc.loadbalancer;

import org.example.lurpc.model.ServiceMetaInfo;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * @Author: 鹿又笑
 * @Create: 2024/6/4 16:45
 * @description: 一致性 Hash 负载均衡器
 */
public class ConsistentHashLoadBalancer implements LoadBalancer {

    /**
     * 一致性hash环，存放虚拟节点
     */
    private final TreeMap<Integer, ServiceMetaInfo> virtualNodes = new TreeMap<>();

    public static final int VIRTUAL_NODE_COUNT = 100;

    @Override
    public ServiceMetaInfo select(Map<String, Object> requestParams, List<ServiceMetaInfo> serviceMetaInfoList) {
        if (serviceMetaInfoList == null || serviceMetaInfoList.isEmpty()) {
            return null;
        }
        // 构建虚拟节点环
        // 外层遍历服务
        for (ServiceMetaInfo serviceMetaInfo : serviceMetaInfoList) {
            // 内层循环根据VIRTUAL_NODE_NUM创建相应数量的虚拟节点，主要是为了解决一致性哈希在实际应用中可能遇到的一个问题：数据倾斜
            for (int i = 0; i < VIRTUAL_NODE_COUNT; i++) {
                int hash = getHash(serviceMetaInfo.getServiceAddress() + "#" + i);
                virtualNodes.put(hash, serviceMetaInfo);
            }
        }
        // 获取调用请求的 hash 值
        int hash = getHash(requestParams);
        // 选择最接近且大于等于调用请求 hash 值的虚拟节点
        Map.Entry<Integer, ServiceMetaInfo> entry = virtualNodes.ceilingEntry(hash);
        if (entry == null) {
            // 如果没有大于等于调用请求 hash 值的虚拟节点，则返回环首部的节点
            return virtualNodes.firstEntry().getValue();
        }
        return entry.getValue();
    }

    /**
     * Hash算法实现
     *
     * @param key
     * @return
     */
    public int getHash(Object key) {
        return key.hashCode();
    }

}
