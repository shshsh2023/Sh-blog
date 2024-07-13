package com.sh.config;



import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.ConfigurableEnvironment;

/**
  * @author song
  * @version 0.0.1
  * @date 2024/7/12 16:27
  *
  */
@SpringBootTest
class CustomEnvironmentPostProcessorTest {


    @Test
    public void test1(ConfigurableEnvironment environment) throws ClassNotFoundException {
        String username = environment.getProperty("spring.datasource.username");
        String password = environment.getProperty("spring.datasource.password");
        String url = environment.getProperty("spring.datasource.url").replace("/target", "ShBlog");
        String driver = environment.getProperty("spring.datasource.driver-class-name");
        Class.forName(driver);

        System.out.println(username);
        System.out.println(password);
        System.out.println(url);

    }

}