package com.rodrigo134.event_driven_document_pipeline.processing;

import com.rodrigo134.event_driven_document_pipeline.document.Document;
import com.rodrigo134.event_driven_document_pipeline.document.DocumentRepository;
import com.rodrigo134.event_driven_document_pipeline.document.DocumentStatus;
import com.rodrigo134.event_driven_document_pipeline.queue.DocumentUploadedEvent;
import org.springframework.stereotype.Service;

@Service
public class DocumentProcessingService {

    private final DocumentRepository documentRepository;

    public DocumentProcessingService(DocumentRepository documentRepository) {
        this.documentRepository = documentRepository;
    }

    public void process(DocumentUploadedEvent event) {
        Document document = documentRepository.findById(event.documentId())
                .orElseThrow(() -> new RuntimeException("Document not found"));

        document.setStatus(DocumentStatus.PROCESSING);
        documentRepository.save(document);

        // Simula processamento do documento
        document.setStatus(DocumentStatus.PROCESSED);
        documentRepository.save(document);
    }
}