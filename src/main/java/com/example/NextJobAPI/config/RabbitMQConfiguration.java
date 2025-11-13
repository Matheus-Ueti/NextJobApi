package com.example.NextJobAPI.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfiguration {
    
    public static final String ANALISE_QUEUE = "nextjob.analise.queue";
    public static final String ANALISE_EXCHANGE = "nextjob.analise.exchange";
    public static final String ANALISE_ROUTING_KEY = "nextjob.analise.routing.key";
    
    public static final String PLANO_QUEUE = "plano.processamento";
    
    @Bean
    public Queue analiseQueue() {
        return QueueBuilder.durable(ANALISE_QUEUE).build();
    }
    
    @Bean
    public Queue planoQueue() {
        return QueueBuilder.durable(PLANO_QUEUE).build();
    }
    
    @Bean
    public TopicExchange analiseExchange() {
        return new TopicExchange(ANALISE_EXCHANGE);
    }
    
    @Bean
    public Binding analiseBinding(Queue analiseQueue, TopicExchange analiseExchange) {
        return BindingBuilder
                .bind(analiseQueue)
                .to(analiseExchange)
                .with(ANALISE_ROUTING_KEY);
    }
    
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
    
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jsonMessageConverter());
        return template;
    }
}
