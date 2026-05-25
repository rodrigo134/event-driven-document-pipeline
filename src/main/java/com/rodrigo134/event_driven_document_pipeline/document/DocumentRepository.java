package com.rodrigo134.event_driven_document_pipeline.document;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DocumentRepository extends JpaRepository<Document, String> {
    Optional<Document> findByHash(String hash);

}
