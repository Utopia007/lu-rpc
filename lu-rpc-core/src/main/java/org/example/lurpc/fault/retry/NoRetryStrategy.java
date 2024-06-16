package org.example.lurpc.fault.retry;

import org.example.lurpc.model.RpcResponse;

import java.util.concurrent.Callable;

/**
 * 重试策略  -不重试
 *
 * @Author: 鹿又笑
 * @Create: 2024/6/15 16:26
 * @description:
 */
public class NoRetryStrategy implements RetryStrategy{

    @Override
    public RpcResponse doRetry(Callable<RpcResponse> callable) throws Exception {
        return callable.call();
    }
}
