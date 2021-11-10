package com.itranzition.alex.rabbitmq;

import com.itranzition.alex.model.dto.RabbitConsumerMessageDto;
import com.itranzition.alex.properties.RabbitConfigurationProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Producer {
    private final RabbitTemplate template;
    private final RabbitConfigurationProperties properties;

    public void send(RabbitConsumerMessageDto messageDto) {
        template.convertAndSend(properties.getRouting(), messageDto);
    }
}
