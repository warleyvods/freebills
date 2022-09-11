package com.freebills.domains;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    @Size(max = 20)
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

}
