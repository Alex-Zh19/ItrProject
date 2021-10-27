package com.itranzition.alex.rabbitmq.testconfig;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itranzition.alex.properties.RabbitConfigurationProperties;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ActiveProfiles;

@TestConfiguration
@ActiveProfiles("test")
@EnableConfigurationProperties({RabbitConfigurationProperties.class})
public class RabbitTestConfig {
    private final RabbitConfigurationProperties properties;

    @Autowired
    public RabbitTestConfig(RabbitConfigurationProperties properties) {
        this.properties = properties;
    }

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

    @Bean
    public ObjectMapper mapper() {
        return new ObjectMapper();
    }

    @Bean
    public MessageConverter jsonToMapMessageConverter(ObjectMapper objectMapper) {
        return new Jackson2JsonMessageConverter(objectMapper);
    }
}