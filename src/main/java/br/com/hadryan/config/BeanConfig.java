package br.com.hadryan.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import test.outside.Connection;

@Configuration
public class BeanConfig {

    @Value("${database.url}")
    private String urlMySql;

    @Value("${database.username}")
    private String usernameMySql;

    @Value("${database.password}")
    private String passwordMySql;

    @Bean
    public Connection connection() {
        return new Connection(urlMySql, usernameMySql, passwordMySql);
    }

    @Bean(name = "mongoDB")
    public Connection connectionMongoDb() {
        return new Connection("localhost", "mongodb", "xxxx");
    }

}
