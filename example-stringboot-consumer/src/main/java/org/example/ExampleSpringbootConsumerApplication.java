package org.example;

import org.example.lurpc.annotation.EnableRpc;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Author: 鹿又笑
 * @Create: 2024/6/17 9:44
 * @description:
 */
@SpringBootApplication
@EnableRpc
public class ExampleSpringbootConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExampleSpringbootConsumerApplication.class, args);
    }

}