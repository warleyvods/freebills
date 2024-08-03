package com.freebills.gateways;

import com.freebills.configs.StorageProperties;
import com.freebills.domain.FileReference;
import com.freebills.exceptions.StorageCloudException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.awscore.AwsRequestOverrideConfiguration;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CopyObjectRequest;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.NoSuchKeyException;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.net.URL;
import java.time.Duration;

@Slf4j
@Component
@AllArgsConstructor
public class S3CloudStorageGateway {

    private final S3Client s3Client;
    private final S3Presigner s3Presigner;
    private final StorageProperties storageProperties;

    public URL generatePresignedUploadUrl(final FileReference fileReference) {
        var builder = AwsRequestOverrideConfiguration.builder();

        if (fileReference.isPublicAccessible()) {
            builder.putRawQueryParameter("x-amz-acl", "public-read");
        }

        PutObjectRequest objectRequest = PutObjectRequest.builder()
                .bucket(getBucket())
                .key(fileReference.getPath())
                .contentType(fileReference.getContentType())
                .contentLength(fileReference.getContentLength())
                .acl(fileReference.isPublicAccessible() ? "public-read" : null)
                .overrideConfiguration(builder.build())
                .build();

        PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(30))
                .putObjectRequest(objectRequest)
                .build();

        return s3Presigner.presignPutObject(presignRequest).url();
    }

    public URL generatePresignedDownloadUrl(final FileReference fileReference) {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(getBucket())
                .key(fileReference.getPath())
                .build();

        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(30))
                .getObjectRequest(getObjectRequest)
                .build();

        return s3Presigner.presignGetObject(presignRequest).url();
    }

    public boolean fileExists(final String filePath) {
        if (filePath == null || filePath.isEmpty()) {
            return false;
        }

        GetObjectRequest request = GetObjectRequest.builder()
                .bucket(getBucket())
                .key(filePath)
                .build();

        try {
            s3Client.getObject(request);
            return true;
        } catch (NoSuchKeyException e) {
            log.warn(String.format("Arquivo não encontrado na nuvem %s", filePath));
            return false;
        }
    }

    public void moveFile(final String fromPath, final String toPath) {
        CopyObjectRequest copyObjectRequest = CopyObjectRequest.builder()
                .sourceKey(fromPath)
                .destinationKey(toPath)
                .sourceBucket(getBucket())
                .destinationBucket(getBucket())
                .build();

        try {
            s3Client.copyObject(copyObjectRequest);
        } catch (S3Exception e) {
            log.error(String.format("Erro ao copiar o arquivo %s para %s", fromPath, toPath), e);
            throw new StorageCloudException(String.format("Erro ao copiar o arquivo %s para %s", fromPath, toPath));
        }

        removeFile(fromPath);
    }

    public void removeFile(final String filePath) {
        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                .bucket(getBucket())
                .key(filePath)
                .build();

        try {
            s3Client.deleteObject(deleteObjectRequest);
        } catch (S3Exception e) {
            log.error(String.format("Erro ao remover arquivo %s", filePath), e);
            throw new StorageCloudException(String.format("Erro ao remover arquivo %s", filePath));
        }
    }

    private String getBucket() {
        return storageProperties.getS3().getBucket();
    }
}
