package org.example.lurpc.fault.tolerant;

import org.example.lurpc.model.RpcRequest;
import org.example.lurpc.model.RpcResponse;

import java.util.Map;

/**
 * @Author: 鹿又笑
 * @Create: 2024/6/16 9:18
 * @description:
 */
public interface TolerantStrategy {

    /**
     * 容错
     *
     * @param context 用于上下文传递数据
     * @param e 异常
     * @return
     */
    RpcResponse doTolerant(Map<String, Object> context, Exception e);

}
