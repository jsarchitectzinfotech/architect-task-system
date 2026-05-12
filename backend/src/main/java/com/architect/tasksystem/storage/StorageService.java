package com.architect.tasksystem.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

import java.net.URI;
import java.time.Duration;
import java.util.UUID;

@Service
@Slf4j
public class StorageService {

    @Value("${supabase.url}")
    private String supabaseUrl;

    @Value("${supabase.service-key}")
    private String serviceKey;

    @Value("${supabase.storage.bucket}")
    private String bucket;

    private S3Client getS3Client() {
        return S3Client.builder()
                .endpointOverride(URI.create(supabaseUrl + "/storage/v1/s3"))
                .region(Region.of("ap-south-1"))
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create("anykey", serviceKey)))
                .forcePathStyle(true)
                .build();
    }

    public String uploadFile(MultipartFile file, Long taskId, Long userId) {
        try {
            String ext = getExtension(file.getOriginalFilename());
            String key = "tasks/" + taskId + "/user_" + userId + "/" + UUID.randomUUID() + ext;
            S3Client s3 = getS3Client();
            s3.putObject(PutObjectRequest.builder()
                    .bucket(bucket)
                    .key(key)
                    .contentType(file.getContentType())
                    .build(),
                    RequestBody.fromInputStream(file.getInputStream(), file.getSize()));
            return key;
        } catch (Exception e) {
            log.error("File upload failed", e);
            throw new RuntimeException("Failed to upload file: " + e.getMessage());
        }
    }

    public String getPublicUrl(String storagePath) {
        return supabaseUrl + "/storage/v1/object/public/" + bucket + "/" + storagePath;
    }

    public String getSignedUrl(String storagePath) {
        try {
            S3Presigner presigner = S3Presigner.builder()
                    .endpointOverride(URI.create(supabaseUrl + "/storage/v1/s3"))
                    .region(Region.of("ap-south-1"))
                    .credentialsProvider(StaticCredentialsProvider.create(
                            AwsBasicCredentials.create("anykey", serviceKey)))
                    .build();
            GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                    .signatureDuration(Duration.ofHours(1))
                    .getObjectRequest(r -> r.bucket(bucket).key(storagePath))
                    .build();
            return presigner.presignGetObject(presignRequest).url().toString();
        } catch (Exception e) {
            return getPublicUrl(storagePath);
        }
    }

    public void deleteFile(String storagePath) {
        try {
            getS3Client().deleteObject(DeleteObjectRequest.builder().bucket(bucket).key(storagePath).build());
        } catch (Exception e) {
            log.error("File delete failed: {}", e.getMessage());
        }
    }

    private String getExtension(String filename) {
        if (filename == null || !filename.contains(".")) return "";
        return "." + filename.substring(filename.lastIndexOf('.') + 1);
    }
}
