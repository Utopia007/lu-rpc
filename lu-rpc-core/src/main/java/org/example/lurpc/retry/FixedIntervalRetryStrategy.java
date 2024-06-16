package org.example.lurpc.retry;

import com.github.rholder.retry.*;
import lombok.extern.slf4j.Slf4j;
import org.example.lurpc.model.RpcResponse;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * 重试策略  -固定时间间隔重试
 *
 * @Author: 鹿又笑
 * @Create: 2024/6/15 16:27
 * @description:
 */
@Slf4j
public class FixedIntervalRetryStrategy implements RetryStrategy{

    @Override
    public RpcResponse doRetry(Callable<RpcResponse> callable) throws Exception {
        Retryer<RpcResponse> retryer = RetryerBuilder.<RpcResponse>newBuilder()
                .retryIfExceptionOfType(Exception.class)
                .withWaitStrategy(WaitStrategies.fixedWait(3L, TimeUnit.SECONDS))
                .withStopStrategy(StopStrategies.stopAfterAttempt(3))
                .withRetryListener(new RetryListener() {
                    @Override
                    public <V> void onRetry(Attempt<V> attempt) {
//                        log.info("log 重试次数： {}", attempt.getAttemptNumber());
                        System.out.println("控制台输出重试：" + attempt.getAttemptNumber());
                    }
                }).build();
        return retryer.call(callable);
    }
}
