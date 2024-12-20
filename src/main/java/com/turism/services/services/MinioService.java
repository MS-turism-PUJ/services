package com.turism.services.services;

import io.minio.*;
import io.minio.errors.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Slf4j
@Service
public class MinioService {
    private final MinioClient minioClient;

    @Value("${minio.bucket.name}")
    private String bucketName;

    public MinioService(MinioClient minioClient) {
        this.minioClient = minioClient;
    }

    private void createBucket() {
        try {
            if (!minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build())) {
                log.info("Creating bucket");
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            }
        } catch (Exception e) {
            log.error("Error creating bucket", e);
        }
    }

    public InputStream getObject(String filename) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        log.info("Getting object");
        createBucket();

        return minioClient.getObject(GetObjectArgs.builder()
        .bucket(bucketName)
        .object("contents/" + filename)
        .build());
    }

    public void uploadFile(String filename, MultipartFile file) throws IOException, ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        log.info("Uploading file");
        String contentType = file.getContentType();

        createBucket();
        minioClient.putObject(PutObjectArgs.builder()
                .bucket(bucketName)
                .object("contents/" + filename)
                .stream(file.getInputStream(), file.getSize(), -1)
                .contentType(contentType)
                .build());
    }
}
