package org.example.lurpc.fault.tolerant;

import org.example.lurpc.model.RpcResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * 容错策略  -静默处理异常
 *
 * @Author: 鹿又笑
 * @Create: 2024/6/16 9:25
 * @description:
 */
public class FailSafeTolerantStrategy implements TolerantStrategy {
    private static final Logger log = LoggerFactory.getLogger(FailSafeTolerantStrategy.class);

    @Override
    public RpcResponse doTolerant(Map<String, Object> context, Exception e) {
        log.info("静默处理异常", e);
        return new RpcResponse();
    }
}
