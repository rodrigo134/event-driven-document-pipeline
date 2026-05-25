package com.rodrigo134.event_driven_document_pipeline.storage;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/files")
public class FileUploadController {

    private final  FileStorageService fileStorageService;

    public FileUploadController(FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        fileStorageService.saveFile(
                "documents",
                UUID.randomUUID().toString() + "-"+ file.getOriginalFilename(),
                file);


        return ResponseEntity.ok("File uploaded successfully");

    }



}
