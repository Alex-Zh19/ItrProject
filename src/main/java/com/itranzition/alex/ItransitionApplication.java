package com.itranzition.alex;

import com.itranzition.alex.properties.JwtConfigurationProperties;
import com.itranzition.alex.properties.RabbitConfigurationProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({JwtConfigurationProperties.class, RabbitConfigurationProperties.class})
public class ItransitionApplication {
    public static void main(String[] args) {
        SpringApplication.run(ItransitionApplication.class, args);
    }
}
