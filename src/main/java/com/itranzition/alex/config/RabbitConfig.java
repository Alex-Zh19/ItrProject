package com.itranzition.alex.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itranzition.alex.properties.RabbitConfigurationProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableRabbit
@Configuration
@RequiredArgsConstructor
public class RabbitConfig {
    private final RabbitConfigurationProperties properties;

    @Bean
    public ConnectionFactory connectionFactory() {
        return new CachingConnectionFactory(properties.getHost());
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, MessageConverter messageConverter) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setExchange(properties.getExchange());
        rabbitTemplate.setMessageConverter(messageConverter);
        return rabbitTemplate;
    }

    @Bean
    public Queue myQueue() {
        return new Queue(properties.getQueue());
    }

    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange(properties.getExchange());
    }

    @Bean
    public Binding binding() {
        return BindingBuilder.bind(myQueue()).to(directExchange()).with(properties.getRouting());
    }

    @Bean
    public MessageConverter messageConverter(ObjectMapper mapper) {
        return new Jackson2JsonMessageConverter(mapper);
    }
}
