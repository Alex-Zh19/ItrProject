package com.itranzition.alex;

import com.itranzition.alex.properties.JwtConfigurationProperties;
import com.itranzition.alex.properties.RabbitConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@EnableConfigurationProperties({JwtConfigurationProperties.class, RabbitConfigurationProperties.class})
public abstract class ItransitionApplicationTests {
}
