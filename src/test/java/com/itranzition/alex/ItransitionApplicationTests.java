package com.itranzition.alex;

import org.junit.jupiter.api.Test;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.NestedTestConfiguration;

@SpringBootTest
@ActiveProfiles(profiles = "test")
class ItransitionApplicationTests {

    @Test
    void contextLoads() {
    }
}
