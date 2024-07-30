package com.freebills.domain;

import com.freebills.gateways.entities.Source;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

import static java.time.LocalDateTime.now;

@Getter
@Setter
@EqualsAndHashCode
public class User implements Serializable {

    private Long id;
    private String name;
    private String login;
    private String email;
    private String password;
    private Boolean admin;
    private Boolean active;
    private Source source;
    private LocalDateTime createdAt;
    private LocalDateTime lastAccess;
    private LocalDateTime updatedAt;

    private String lastLogin;

    public void setLastAccess() {
        if (lastAccess == null) {
            this.lastAccess = now();
        }
    }

    public String getLastLogin() {
        return (lastLogin == null) ? this.login : this.lastLogin;
    }
}
