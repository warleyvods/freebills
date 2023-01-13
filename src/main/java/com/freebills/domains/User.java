package com.freebills.domains;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "`user`",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "login"),
                @UniqueConstraint(columnNames = "email")
        })
@EntityListeners(AuditingEntityListener.class)
public class User extends AbstractEntity {

    @NotBlank
    @Column(nullable = false)
    private String name;

    @Size(max = 50)
    @Basic(optional = false)
    @Column(nullable = false)
    private String login;

    @Size(max = 50)
    @NotBlank
    @Column(nullable = false)
    private String email;

    @Size(max = 100)
    @NotBlank
    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private boolean admin = false;

    @Column(nullable = false)
    private boolean active = true;

    @CreatedDate
    private LocalDate createdAt;

    @OneToMany(mappedBy = "user")
    private List<Account> accounts;

    private LocalDateTime lastAccess;

    public void lastAccess() {
        this.lastAccess = LocalDateTime.now();
    }
}
