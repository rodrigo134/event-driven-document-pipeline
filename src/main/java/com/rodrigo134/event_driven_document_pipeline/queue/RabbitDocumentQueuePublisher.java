package com.rodrigo134.event_driven_document_pipeline.queue;


import com.rodrigo134.event_driven_document_pipeline.config.RabbitMQConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class RabbitDocumentQueuePublisher implements DocumentQueuePublisher {

    private final RabbitTemplate rabbitTemplate;

    public RabbitDocumentQueuePublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void publish(DocumentUploadedEvent event) {
        System.out.println("Publishing event to RabbitMQ: " + event);
        rabbitTemplate.convertAndSend(RabbitMQConfig.DOCUMENT_UPLOADED_QUEUE, event);
    }
}