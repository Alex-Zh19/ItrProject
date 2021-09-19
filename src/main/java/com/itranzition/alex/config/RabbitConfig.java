package com.itranzition.alex.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableRabbit
@Configuration
public class RabbitConfig {
    @Value("${rabbit.queue}")
    private String QUEUE_NAME;
    @Value("${rabbit.exchange}")
    private String EXCHANGE;
    @Value("${rabbit.routing}")
    private String ROUTING_KEY;

    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory =
                new CachingConnectionFactory();
        System.out.println("connectionFactory created");
        return connectionFactory;
    }

    @Bean
    public RabbitTemplate rabbitTemplate() {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory());
        rabbitTemplate.setExchange(EXCHANGE);
        System.out.println("rabbitTemplate created");
        return rabbitTemplate;
    }

    @Bean
    public Queue myQueue1() {
        System.out.println("Queue created");
        return new Queue(QUEUE_NAME);
    }

    @Bean
    public DirectExchange directExchange() {System.out.println("exchange created");
        return new DirectExchange(EXCHANGE);
    }

    @Bean
    public Binding errorBinding1() {
        System.out.println("binding created");
        return BindingBuilder.bind(myQueue1()).to(directExchange()).with(ROUTING_KEY);
    }
}
