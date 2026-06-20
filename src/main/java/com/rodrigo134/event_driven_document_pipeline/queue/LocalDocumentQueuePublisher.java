package com.rodrigo134.event_driven_document_pipeline.queue;

import com.rodrigo134.event_driven_document_pipeline.processing.DocumentProcessingService;
import org.springframework.stereotype.Service;

@Service
public class LocalDocumentQueuePublisher implements DocumentQueuePublisher {

    private final DocumentProcessingService documentProcessingService;

    public LocalDocumentQueuePublisher(DocumentProcessingService documentProcessingService) {
        this.documentProcessingService = documentProcessingService;
    }


    @Override
    public void publish(DocumentUploadedEvent event) {
        System.out.println("Document uploaded event: " + event);

        documentProcessingService.process(event);
    }
}