package com.itranzition.alex.rabbitmq;

import com.itranzition.alex.model.dto.RabbitConsumerMessageDto;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Producer {
    private final RabbitTemplate template;
    @Value("${rabbit.routing}")
    private String ROUTING_KEY;

    @Autowired
    public Producer(RabbitTemplate template) {
        this.template = template;
    }

    public void send(RabbitConsumerMessageDto messageDto) {
        template.convertAndSend(ROUTING_KEY, messageDto);
    }
}
