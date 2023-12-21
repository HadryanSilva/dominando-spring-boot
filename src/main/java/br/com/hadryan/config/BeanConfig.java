package br.com.hadryan.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import test.outside.Connection;

@Configuration
public class BeanConfig {

    @Bean
    public Connection connection() {
        return new Connection("localhost", "user", "xxx");
    }

}
