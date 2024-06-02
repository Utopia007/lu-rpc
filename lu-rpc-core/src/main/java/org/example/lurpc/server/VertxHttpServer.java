package org.example.lurpc.server;

import io.vertx.core.Vertx;

/**
 * @auther : LuYouxiao
 * @date 2024/3/31   -16:22
 * @Description
 */
public class VertxHttpServer implements HttpServer{
    @Override
    public void doStart(int port) {
        // 创建vert.x实例
        Vertx vertx = Vertx.vertx();

        // 创建Http服务器
        io.vertx.core.http.HttpServer httpServer = vertx.createHttpServer();

        //监听端口并处理请求
//        httpServer.requestHandler(request -> {
//            // 处理http请求
//            System.out.println("request = " + request.method() + " " + request.uri());
//            // 发送http响应
//            request.response()
//                    .putHeader("content-type", "text/plain")
//                    .end("hello vertx wahahahhahahah 鹿又笑");
//        });
        httpServer.requestHandler(new HttpServerHandler());

        // 启动http服务器监听指定端口
        httpServer.listen(port, result -> {
            if (result.succeeded()) {
                System.out.println("成功监听！！！port = " + port);
            } else {
                System.out.println("失败@@@ = " + result.cause());
            }
        });
    }

}
