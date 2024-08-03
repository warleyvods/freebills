package com.freebills.usecases;

import com.freebills.domain.FileReference;
import com.freebills.domain.User;
import com.freebills.gateways.StorageGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreateImageProfile {

    private final UpdateUser updateUser;
    private final StorageGateway storageGateway;

    public FileReference execute(final User user, FileReference fileReference) {
        final var file = storageGateway.generateUploadUrl(fileReference);

        user.setImgUrl(file.getPublicUrl());
        updateUser.execute(user);

        return file;
    }
}
