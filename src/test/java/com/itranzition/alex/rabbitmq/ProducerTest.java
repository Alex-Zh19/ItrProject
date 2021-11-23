package com.itranzition.alex.rabbitmq;

import com.itranzition.alex.ItransitionApplicationTests;
import com.itranzition.alex.model.dto.RabbitConsumerMessageDto;
import com.itranzition.alex.model.dto.RegisteredUserDto;
import com.itranzition.alex.properties.RabbitConfigurationProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class ProducerTest extends ItransitionApplicationTests {
    private static final String TEST_EMAIL = "testEmail";
    private static final String TEST_NAME = "testName";

    @Autowired
    private Producer producer;
    @Autowired
    private RabbitConfigurationProperties properties;
    @Autowired
    private ConnectionFactory factory;

    @BeforeEach
    void setUp() {
        send();
    }

    @Test
    public void worker() throws IOException {
        Connection connection = factory.createConnection();
        Channel channel = connection.createChannel(false);
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println(" [x] Received '" + message + "'");
        };
        String result = channel.basicConsume(properties.getQueue(), true, deliverCallback, consumerTag -> {
        });

        assertNotNull(result);
    }

    private void send() {
        RegisteredUserDto registeredUserDto = new RegisteredUserDto(TEST_EMAIL, TEST_NAME);
        RabbitConsumerMessageDto rabbitConsumerMessageDto =
                new RabbitConsumerMessageDto(registeredUserDto, LocalDateTime.now());
        producer.send(rabbitConsumerMessageDto);
    }
}
