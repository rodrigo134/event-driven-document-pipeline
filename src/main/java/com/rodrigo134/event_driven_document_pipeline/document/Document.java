package com.rodrigo134.event_driven_document_pipeline.document;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "documents")
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String originalFilename;

    private String objectKey;

    private String hash;

    private Long size;

    @Enumerated(EnumType.STRING)
    private DocumentStatus status;

    private LocalDateTime uploadedAt;

    private LocalDateTime processedAt;

    private String failureReason;
}