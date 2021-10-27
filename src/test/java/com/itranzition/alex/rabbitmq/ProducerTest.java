package com.itranzition.alex.rabbitmq;

import com.itranzition.alex.model.dto.RabbitConsumerMessageDto;
import com.itranzition.alex.model.dto.RegisteredUserDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ActiveProfiles("test")
//@ContextConfiguration(classes = RabbitTestConfig.class)
class ProducerTest {
    private final String TEST_EMAIL = "testEmail";
    private final String TEST_NAME = "testName";

    @Autowired
    private Producer producer;

    @BeforeEach
    void setUp() {
        send();
    }

    @Test
    @RabbitListener
    public void worker(RabbitConsumerMessageDto messageDto) {
        assertNotNull(messageDto);
    }

    private void send() {
        RegisteredUserDto registeredUserDto = new RegisteredUserDto(TEST_EMAIL, TEST_NAME);
        RabbitConsumerMessageDto rabbitConsumerMessageDto =
                new RabbitConsumerMessageDto(registeredUserDto, LocalDateTime.now());
        producer.send(rabbitConsumerMessageDto);
    }
}
