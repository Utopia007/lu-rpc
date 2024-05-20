package org.example.lurpc.server;

/**
 * @auther : LuYouxiao
 * @date 2024/3/31   -16:19
 * @Description Http服务接口
 */
public interface HttpServer {

    /**
     * 启动服务器
     * @param port
     */
    void doStart(int port);

}
