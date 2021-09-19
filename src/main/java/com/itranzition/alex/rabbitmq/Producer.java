package com.itranzition.alex.rabbitmq;

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

    public void send(String message) {
        System.out.println("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF");
        template.convertAndSend(ROUTING_KEY,message);
    }
}
