package org.example.lurpc.registry;

import org.example.lurpc.model.ServiceMetaInfo;

import java.util.List;

/**
 * @Author:鹿又笑
 * @Create:2024/4/29 20:55
 * @description:
 */

public class RegistryServiceCache {

    List<ServiceMetaInfo> serviceCache;

    /**
     * 读缓存
     * @return
     */
    List<ServiceMetaInfo> readCache(){
        System.out.println("读缓存");
        return serviceCache;
    }

    /**
     * 写缓存
     * @param serviceCache
     */
    void writeCache(List<ServiceMetaInfo> serviceCache){
        System.out.println("写入缓存");
        this.serviceCache = serviceCache;
    }

    /**
     * 清空缓存
     */
    void clearCache(){
        System.out.println("清空缓存");
        serviceCache.clear();
    }

}
