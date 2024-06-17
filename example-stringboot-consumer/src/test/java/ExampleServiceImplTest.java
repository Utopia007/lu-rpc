import org.example.ExampleSpringbootConsumerApplication;
import org.example.service.serviceImpl.ExampleServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

/**
 * @Author: 鹿又笑
 * @Create: 2024/6/17 9:46
 * @description:
 */
@SpringBootTest(classes = ExampleSpringbootConsumerApplication.class)
class ExampleServiceImplTest {

    @Resource
    private ExampleServiceImpl exampleService;

    @Test
    void test1() {
        exampleService.test();
    }
}