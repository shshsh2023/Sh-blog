package com.sh.config;

import com.sh.handle.PoetryRuntimeException;
import lombok.SneakyThrows;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

@Order(Ordered.LOWEST_PRECEDENCE)
public class CustomEnvironmentPostProcessor implements EnvironmentPostProcessor {

    private static final String SOURCE_NAME = "sys_config";

    private static final String SOURCE_SQL = "select * from shblog.sys_config";

    private static final String DATABASE = "shblog";

    private static final String sqlPath = "classpath:sql/*.sql";


    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        try {
            Map<String, Object> map = new HashMap<>();

            String username = environment.getProperty("spring.datasource.username");
            String password = environment.getProperty("spring.datasource.password");
            String url = environment.getProperty("spring.datasource.url").replace("/target", "/ShBlog");
            String driver = environment.getProperty("spring.datasource.driver-class-name");
            Class.forName(driver);
            try (Connection connection = DriverManager.getConnection(url, username, password)) {
                //初始化数据库
                initDb(connection);
                //加载配置文件
                try (Statement statement = connection.createStatement()) {
                    try (ResultSet resultSet = statement.executeQuery(SOURCE_SQL)) {
                        while (resultSet.next()) {
                            map.put(resultSet.getString("config_key"), resultSet.getString("config_value"));
                        }
                    }
                }
            }

            MutablePropertySources propertySources = environment.getPropertySources();
            PropertySource<?> source = new MapPropertySource(SOURCE_NAME, map);
            propertySources.addFirst(source);
        } catch (Exception e) {
            throw new PoetryRuntimeException(e);
        }
    }

    @SneakyThrows
    private void initDb(Connection connection) {
        try (Statement statement = connection.createStatement()) {
            try (ResultSet resultSet = statement.executeQuery("SHOW DATABASES LIKE '" + DATABASE + "'")) {
                if (!resultSet.next()) {
                    ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
                    ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
                    populator.addScripts(resolver.getResources(sqlPath));
                    populator.populate(connection);
                }
            }
        }
    }

}
