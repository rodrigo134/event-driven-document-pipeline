package com.rodrigo134.event_driven_document_pipeline.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String DOCUMENT_UPLOADED_QUEUE = "document.uploaded.queue";

    @Bean
    public Queue documentUploadedQueue() {
        return new Queue(DOCUMENT_UPLOADED_QUEUE, true);
    }

    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}