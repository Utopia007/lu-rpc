package org.example.lurpc.registry;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ConcurrentHashSet;
import cn.hutool.cron.CronUtil;
import cn.hutool.cron.task.Task;
import cn.hutool.json.JSONUtil;
import io.etcd.jetcd.*;
import io.etcd.jetcd.options.GetOption;
import io.etcd.jetcd.options.PutOption;
import io.etcd.jetcd.watch.WatchEvent;
import org.example.lurpc.config.RegistryConfig;
import org.example.lurpc.model.ServiceMetaInfo;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

/**
 * @auther : LuYouxiao
 * @date 2024/4/15   -11:04
 * @Description
 */
public class EtcdRegistry implements Registry {

    /**
     * 本机注册节点的key集合（用于维护续期）
     */
    private static final Set<String> localRegisterNodeKeySet = new ConcurrentHashSet<>();

    /**
     * 注册中心缓存服务
     */
    private final RegistryServiceCache registryServiceCache = new RegistryServiceCache();

    /**
     * 正在监听的 key 集合
     */
    private final Set<String> watchingKeySet = new ConcurrentHashSet<>();

    private Client client;

    private KV kvClient;

    /**
     * 根节点
     */
    private static final String ETCD_ROOT_PATH = "/rpc/";

    @Override
    public void init(RegistryConfig registryConfig) {
        client = Client.builder().endpoints(registryConfig.getAddress()).connectTimeout(Duration.ofMillis(registryConfig.getTimeout())).build();
        kvClient = client.getKVClient();
        // 开启心跳检测机制
        heartBeat();
    }

    /**
     * 服务注册，创建 key 并设置过期时间，value 为服务注册信息的 JSON 序列化
     * @param serviceMetaInfo
     */
    @Override
    public void registerService(ServiceMetaInfo serviceMetaInfo) throws ExecutionException, InterruptedException {
        // 创建租约 30秒
        long id = client.getLeaseClient().grant(30).get().getID();
        // 设置存储的键值对
        String serverKey = ETCD_ROOT_PATH + serviceMetaInfo.getServiceNodeKey();
        ByteSequence key = ByteSequence.from(serverKey, StandardCharsets.UTF_8);
        ByteSequence value = ByteSequence.from(JSONUtil.toJsonStr(serviceMetaInfo), StandardCharsets.UTF_8);
        // 将键值对与租约关联起来，并设置过期时间
        PutOption putOption = PutOption.builder().withLeaseId(id).build();
        kvClient.put(key, value, putOption).get();
        // 添加节点信息到本地缓存
        localRegisterNodeKeySet.add(serverKey);
    }

    @Override
    public void unRegister(ServiceMetaInfo serviceMetaInfo) {
        String serverNodeKey = ETCD_ROOT_PATH + serviceMetaInfo.getServiceNodeKey();
        kvClient.delete(ByteSequence.from(serverNodeKey, StandardCharsets.UTF_8));
        // 从本地缓存移除
        localRegisterNodeKeySet.remove(serverNodeKey);
    }

    @Override
    public List<ServiceMetaInfo> serviceDiscovery(String serviceKey) {
        // 优先从缓存获取服务
        List<ServiceMetaInfo> serviceMetaInfoList = registryServiceCache.readCache();
        if (serviceMetaInfoList != null) {
            return serviceMetaInfoList;
        }
        String searchPrefix = ETCD_ROOT_PATH + serviceKey + "/";
        try {
            // 前缀查询
            GetOption getOption = GetOption.builder().isPrefix(true).build();
            List<KeyValue> kvs = kvClient.get(ByteSequence.from(searchPrefix, StandardCharsets.UTF_8), getOption).get().getKvs();
            // 解析服务信息
            List<ServiceMetaInfo> serviceMetaInfos = kvs.stream().map(keyValue -> {
                String key = keyValue.getKey().toString(StandardCharsets.UTF_8);
                // 监听key的变化
                watch(key);
                String value = keyValue.getValue().toString(StandardCharsets.UTF_8);
                return JSONUtil.toBean(value, ServiceMetaInfo.class);
            }).collect(Collectors.toList());
            // 写入服务缓存
            registryServiceCache.writeCache(serviceMetaInfos);
            return serviceMetaInfos;
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void destroy() {
        System.out.println("当前节点下线");
        for (String key : localRegisterNodeKeySet) {
            try {
                kvClient.delete(ByteSequence.from(key, StandardCharsets.UTF_8)).get();
            } catch (Exception e) {
                throw new RuntimeException("节点下线失败，" + e);
            }
        }
        // 释放资源
        if (kvClient != null) {
            kvClient.close();
        }
        if (client != null) {
            client.close();
        }
    }

    @Override
    public void heartBeat() {
        // 10 秒续签一次
        CronUtil.schedule("*/10 * * * * *", new Task() {
            @Override
            public void execute() {
                // 遍历本节点所有的 key
                for (String key : localRegisterNodeKeySet) {
                    try {
                        List<KeyValue> keyValues = kvClient.get(ByteSequence.from(key, StandardCharsets.UTF_8))
                                .get()
                                .getKvs();
                        // 该节点已过期（需要重启节点才能重新注册）
                        if (CollUtil.isEmpty(keyValues)) {
                            continue;
                        }
                        // todo 有问题 节点未过期，重新注册（相当于续签）
                        KeyValue keyValue = keyValues.get(0);
                        String value = keyValue.getValue().toString(StandardCharsets.UTF_8);
                        ServiceMetaInfo serviceMetaInfo = JSONUtil.toBean(value, ServiceMetaInfo.class);
                        registerService(serviceMetaInfo);
                    } catch (Exception e) {
                        throw new RuntimeException(key + "续签失败", e);
                    }
                }
            }
        });

        // 支持秒级别定时任务
        CronUtil.setMatchSecond(true);
        CronUtil.start();
    }

    @Override
    public void watch(String serviceNodeKey) {
        System.out.println("执行watch============");
        Watch watchClient = client.getWatchClient();
        // 之前未被监听，开启监听
        boolean add = watchingKeySet.add(serviceNodeKey);
        if (add) {
            watchClient.watch(ByteSequence.from(serviceNodeKey, StandardCharsets.UTF_8), watchResponse -> {
                for (WatchEvent responseEvent : watchResponse.getEvents()) {
                    switch (responseEvent.getEventType()) {
                        case DELETE:
                            registryServiceCache.clearCache();
                            break;
                        default:
                            break;
                    }
                }
            });
        }

    }

}
