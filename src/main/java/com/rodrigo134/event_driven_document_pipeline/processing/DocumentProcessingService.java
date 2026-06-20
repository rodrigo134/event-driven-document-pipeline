package com.rodrigo134.event_driven_document_pipeline.processing;

import com.rodrigo134.event_driven_document_pipeline.document.Document;
import com.rodrigo134.event_driven_document_pipeline.document.DocumentRepository;
import com.rodrigo134.event_driven_document_pipeline.document.DocumentStatus;
import com.rodrigo134.event_driven_document_pipeline.exception.DocumentNotFoundException;
import com.rodrigo134.event_driven_document_pipeline.queue.DocumentUploadedEvent;
import com.rodrigo134.event_driven_document_pipeline.storage.FileStorageService;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.time.LocalDateTime;

@Service
public class DocumentProcessingService {

    private final DocumentRepository documentRepository;
    private final FileStorageService fileStorageService;

    public DocumentProcessingService(
            DocumentRepository documentRepository,
            FileStorageService fileStorageService
    ) {
        this.documentRepository = documentRepository;
        this.fileStorageService = fileStorageService;
    }

    public void process(DocumentUploadedEvent event) {
        Document document = documentRepository.findById(event.documentId())
                .orElseThrow(() -> new DocumentNotFoundException("Document not found"));

        try {
            document.setStatus(DocumentStatus.PROCESSING);
            documentRepository.save(document);

            try (InputStream inputStream = fileStorageService.getFile("documents", document.getObjectKey())) {
                inputStream.read(); // valida que o worker conseguiu acessar e ler o arquivo
            }

            document.setStatus(DocumentStatus.PROCESSED);
            document.setProcessedAt(LocalDateTime.now());
            document.setFailureReason(null);
            documentRepository.save(document);

        } catch (Exception e) {
            document.setStatus(DocumentStatus.FAILED);
            document.setFailureReason(e.getMessage());
            documentRepository.save(document);

            throw new RuntimeException(e);
        }
    }
}