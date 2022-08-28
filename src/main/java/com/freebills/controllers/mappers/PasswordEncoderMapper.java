package com.freebills.controllers.mappers;

import com.freebills.annotations.EncodedMapping;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
public record PasswordEncoderMapper(PasswordEncoder passwordEncoder) {

    @EncodedMapping
    public String encode(String password) {
        return passwordEncoder.encode(password);
    }
}
