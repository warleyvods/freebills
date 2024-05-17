package com.freebills.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static java.time.LocalDateTime.now;

@Getter
@Setter
@EqualsAndHashCode
public class User {

    private Long id;
    private String name;
    private String login;
    private String email;
    private String password;
    private Boolean admin;
    private Boolean active;
    private LocalDate createdAt;
    private LocalDateTime lastAccess;

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
