package org.example.lurpc.fault.tolerant;

import org.example.lurpc.model.RpcResponse;

import java.util.Map;

/**
 * 容错策略  -降级到其他服务
 *
 * @Author: 鹿又笑
 * @Create: 2024/6/16 9:27
 * @description:
 */
public class FailBackTolerantStrategy implements TolerantStrategy{
    @Override
    public RpcResponse doTolerant(Map<String, Object> context, Exception e) {
        // todo 降级 （获取降级的服务并调用）

        return null;
    }
}
