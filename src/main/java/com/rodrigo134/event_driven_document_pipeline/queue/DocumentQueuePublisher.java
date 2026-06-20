package com.rodrigo134.event_driven_document_pipeline.queue;

public interface DocumentQueuePublisher {
    void publish(DocumentUploadedEvent event);
}