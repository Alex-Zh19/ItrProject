package com.itranzition.alex.rabbitmq;

import com.itranzition.alex.model.dto.RabbitConsumerMessageDto;
import com.itranzition.alex.properties.RabbitConfigurationProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Producer {
    private final RabbitTemplate template;
    private RabbitConfigurationProperties properties;

    @Autowired
    public Producer(RabbitConfigurationProperties properties,
                    RabbitTemplate template) {
        this.properties = properties;
        this.template = template;
    }

    public void send(RabbitConsumerMessageDto messageDto) {
        template.convertAndSend(properties.getRouting(), messageDto);
    }
}
