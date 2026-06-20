package com.rodrigo134.event_driven_document_pipeline.document;

import com.rodrigo134.event_driven_document_pipeline.exception.DocumentNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/documents")
public class DocumentController {

    private final DocumentRepository documentRepository;

    public DocumentController(DocumentRepository documentRepository) {
        this.documentRepository = documentRepository;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Document> findById(@PathVariable String id) {
        Document document = documentRepository.findById(id)
                .orElseThrow(() -> new DocumentNotFoundException("Document not found"));

        return ResponseEntity.ok(document);
    }
}