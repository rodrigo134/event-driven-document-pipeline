package com.rodrigo134.event_driven_document_pipeline.queue;

import com.rodrigo134.event_driven_document_pipeline.config.RabbitMQConfig;
import com.rodrigo134.event_driven_document_pipeline.processing.DocumentProcessingService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class DocumentUploadConsumer {

    private final DocumentProcessingService documentProcessingService;

    public DocumentUploadConsumer(DocumentProcessingService documentProcessingService) {
        this.documentProcessingService = documentProcessingService;
    }

    @RabbitListener(queues = RabbitMQConfig.DOCUMENT_UPLOADED_QUEUE)
    public void consume(DocumentUploadedEvent event) {
        System.out.println("Consuming event from RabbitMQ: " + event);
        documentProcessingService.process(event);
    }
}