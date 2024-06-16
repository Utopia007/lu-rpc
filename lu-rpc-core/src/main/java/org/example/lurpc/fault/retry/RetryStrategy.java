package org.example.lurpc.fault.retry;

import org.example.lurpc.model.RpcResponse;

import java.util.concurrent.Callable;

/**
 * @Author: 鹿又笑
 * @Create: 2024/6/15 16:01
 * @description:
 */
public interface RetryStrategy {

    /**
     * 重试
     *
     * @param callable
     * @return
     * @throws Exception
     */
    RpcResponse doRetry(Callable<RpcResponse> callable) throws Exception;

}
