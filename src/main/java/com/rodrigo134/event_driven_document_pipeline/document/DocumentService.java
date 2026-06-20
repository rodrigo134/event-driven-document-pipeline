package com.rodrigo134.event_driven_document_pipeline.document;

import com.rodrigo134.event_driven_document_pipeline.queue.DocumentQueuePublisher;
import com.rodrigo134.event_driven_document_pipeline.queue.DocumentUploadedEvent;
import com.rodrigo134.event_driven_document_pipeline.storage.FileStorageService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class DocumentService {

    private final DocumentQueuePublisher documentQueuePublisher;
    private final FileStorageService fileStorageService;
    private final DocumentRepository documentRepository;
    private final FileHashService fileHashService;

    public DocumentService(
            FileStorageService fileStorageService,
            DocumentRepository documentRepository,
            FileHashService fileHashService,
            DocumentQueuePublisher documentQueuePublisher
    ) {
        this.fileStorageService = fileStorageService;
        this.documentRepository = documentRepository;
        this.fileHashService = fileHashService;
        this.documentQueuePublisher = documentQueuePublisher;
    }

    public Document upload(MultipartFile file) {
        if (file.isEmpty()) {
            throw new RuntimeException("File is empty");
        }
        // 1. hash do arquivo
        String hash = fileHashService.calculateHash(file);

        // 2. deduplicação
        documentRepository.findByHash(hash).ifPresent(doc -> {
            throw new RuntimeException("Duplicate file");
        });

        // 3. gera chave no MinIO
        String objectKey = UUID.randomUUID() + "-" + file.getOriginalFilename();

        // 4. salva no MinIO
        fileStorageService.saveFile("documents", objectKey, file);

        // 5. cria registro no banco
        Document document = new Document();
        document.setOriginalFilename(file.getOriginalFilename());
        document.setObjectKey(objectKey);
        document.setHash(hash);
        document.setSize(file.getSize());
        document.setStatus(DocumentStatus.UPLOADED);
        document.setUploadedAt(LocalDateTime.now());



        Document savedDocument = documentRepository.save(document);

        documentQueuePublisher.publish(
                new DocumentUploadedEvent(
                        savedDocument.getId(),
                        savedDocument.getObjectKey(),
                        savedDocument.getHash()
                )
        );

        return savedDocument;


    }
}