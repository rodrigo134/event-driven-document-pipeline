package com.rodrigo134.event_driven_document_pipeline.queue;

public record DocumentUploadedEvent(
        String documentId,
        String objectKey,
        String hash
) {
}