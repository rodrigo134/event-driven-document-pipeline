package com.rodrigo134.event_driven_document_pipeline.processing;

import com.rodrigo134.event_driven_document_pipeline.document.Document;
import com.rodrigo134.event_driven_document_pipeline.document.DocumentRepository;
import com.rodrigo134.event_driven_document_pipeline.document.DocumentStatus;
import com.rodrigo134.event_driven_document_pipeline.exception.DocumentNotFoundException;
import com.rodrigo134.event_driven_document_pipeline.queue.DocumentUploadedEvent;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class DocumentProcessingService {

    private final DocumentRepository documentRepository;

    public DocumentProcessingService(DocumentRepository documentRepository) {
        this.documentRepository = documentRepository;
    }

    public void process(DocumentUploadedEvent event) {
        Document document = documentRepository.findById(event.documentId())
                .orElseThrow(() -> new DocumentNotFoundException("Document not found"));

        try {
            document.setStatus(DocumentStatus.PROCESSING);
            documentRepository.save(document);

            // Simula processamento do documento

            document.setStatus(DocumentStatus.PROCESSED);
            document.setProcessedAt(LocalDateTime.now());
            document.setFailureReason(null);
            documentRepository.save(document);

        } catch (Exception e) {
            document.setStatus(DocumentStatus.FAILED);
            document.setFailureReason(e.getMessage());
            documentRepository.save(document);

            throw e;
        }
    }
}