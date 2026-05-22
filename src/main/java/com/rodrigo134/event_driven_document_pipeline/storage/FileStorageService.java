package com.rodrigo134.event_driven_document_pipeline.storage;


import io.minio.MinioClient;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import io.minio.PutObjectArgs;

@Service
public class FileStorageService {

    private final MinioClient minioClient;

    public FileStorageService(MinioClient minioClient) {
        this.minioClient = minioClient;
    }

    public void salvarArquivo(String bucket, String nome, MultipartFile arquivo) {

        try {

            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucket)
                            .object(nome)
                            .stream(
                                    arquivo.getInputStream(),
                                    arquivo.getSize(),
                                    -1
                            )
                            .contentType(arquivo.getContentType())
                            .build()
            );

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }



    }
