package org.example.lurpc.fault.tolerant;

import org.example.lurpc.model.RpcResponse;

import java.util.Map;

/**
 * 容错策略  -故障转移
 *
 * @Author: 鹿又笑
 * @Create: 2024/6/16 9:29
 * @description:
 */
public class FailOverTolerantStrategy implements TolerantStrategy{

    /**
     *
     * @param context 用于上下文传递数据
     * @param e 异常
     * @return
     */
    @Override
    public RpcResponse doTolerant(Map<String, Object> context, Exception e) {
        // todo 获取其他服务节点并调用

        return null;
    }
}
