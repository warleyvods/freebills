package com.freebills.domain;

import com.freebills.gateways.entities.enums.Type;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class FileReference {

    private UUID id;
    private String fileName;
    private String contentType;
    private Long contentLength;
    private Boolean temp;
    private Type type;
    private LocalDateTime createdAt;

    private URL url;
    private String publicUrl;

    public String getPath() {
        return this.id + "/" + this.fileName;
    }

    public boolean isPublicAccessible() {
        return type.isPublicAccessible();
    }

    public FileReference withUrl(final URL url, final String publicUrl) {
        return this.toBuilder()
                .publicUrl(publicUrl)
                .url(url)
                .build();
    }
}
