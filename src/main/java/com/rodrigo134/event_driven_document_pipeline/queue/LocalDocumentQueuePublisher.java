package com.rodrigo134.event_driven_document_pipeline.queue;

import org.springframework.stereotype.Service;

@Service
public class LocalDocumentQueuePublisher implements DocumentQueuePublisher {

    @Override
    public void publish(DocumentUploadedEvent event) {
        System.out.println("Document uploaded event: " + event);
    }
}