package com.freebills.gateways.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;

import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.FetchType.LAZY;

@Getter
@Setter
@Entity
@Table(name = "users")
@EntityListeners(AuditingEntityListener.class)
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String name;

    @Size(max = 50)
    @Column(unique = true)
    private String login;

    @Size(max = 50)
    @NotBlank
    @Column(nullable = false, unique = true)
    private String email;

    @Size(max = 100)
    private String password;

    @Column(nullable = false)
    private Boolean admin = false;

    @Column(nullable = false)
    private Boolean active = true;

    @Enumerated(STRING)
    private Source source;

    @OneToMany(mappedBy = "user", cascade = ALL, fetch = LAZY)
    private List<AccountEntity> accounts;

    @OneToMany(mappedBy = "user", cascade = ALL, fetch = LAZY)
    private List<CategoryEntity> categories;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime lastAccess;

}
