package com.rodrigo134.event_driven_document_pipeline.exception;


public class DuplicateDocumentException extends RuntimeException {
    public DuplicateDocumentException(String message) {
        super(message);
    }
}