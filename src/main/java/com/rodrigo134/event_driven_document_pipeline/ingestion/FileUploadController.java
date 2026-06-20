package com.rodrigo134.event_driven_document_pipeline.ingestion;


import com.rodrigo134.event_driven_document_pipeline.document.Document;
import com.rodrigo134.event_driven_document_pipeline.document.DocumentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/files")
public class FileUploadController {

    private final DocumentService documentService;

    public FileUploadController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @PostMapping("/upload")
    public ResponseEntity<Document> uploadFile(@RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(documentService.upload(file));
    }
}




