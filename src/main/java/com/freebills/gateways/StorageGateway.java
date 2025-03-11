package com.freebills.gateways;

import com.freebills.controllers.dtos.responses.DownloadRequestResult;
import com.freebills.domain.FileReference;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.util.Objects;


@Slf4j
@Service
@AllArgsConstructor
public class StorageGateway {

    private final S3CloudStorageGateway cloudStorageProvider;
    private final FileReferenceGateway fileReferenceGateway;

    public FileReference generateUploadUrl(final FileReference fileReference) {
        Objects.requireNonNull(fileReference);

        final var fileSaved = fileReferenceGateway.save(fileReference);
        final var presignedUploadUrl = cloudStorageProvider.generatePresignedUploadUrl(fileSaved);
        var url = presignedUploadUrl.getProtocol() + "://" + presignedUploadUrl.getHost() + presignedUploadUrl.getPath();

        return fileSaved.withUrl(presignedUploadUrl, url);
    }

    public DownloadRequestResult generateDownloadUrl(FileReference fileReference) {
        Objects.requireNonNull(fileReference);

        URL url = cloudStorageProvider.generatePresignedDownloadUrl(fileReference);
        return new DownloadRequestResult(
                url.toString(),
                fileReference.getPublicUrl(),
                fileReference.getContentType()
        );
    }

    public boolean fileExists(FileReference fileReference) {
        Objects.requireNonNull(fileReference);

        return this.cloudStorageProvider.fileExists(fileReference.getPath());
    }

    public void softDelete(FileReference fileReference) {
        this.cloudStorageProvider.moveFile(fileReference.getPath(), "deleted/" + fileReference.getPath());
    }
}
