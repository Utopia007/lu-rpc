package org.example.lurpc.fault.tolerant;

import org.example.lurpc.model.RpcResponse;

import java.util.Map;

/**
 * 容错策略  -快速失败
 *
 * @Author: 鹿又笑
 * @Create: 2024/6/16 9:19
 * @description:
 */
public class FailFastTolerantStrategy implements TolerantStrategy{

    @Override
    public RpcResponse doTolerant(Map<String, Object> context, Exception e) {
        System.out.println("+++++++++++++++++++++++++___________________________________");
        throw new RuntimeException("fastfail 服务报错", e);
    }
}
