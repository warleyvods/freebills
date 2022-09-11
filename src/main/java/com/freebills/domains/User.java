package com.freebills.domains;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "`user`")
@EntityListeners(AuditingEntityListener.class)
public class User extends AbstractEntity {

    @NotBlank
    @Column(nullable = false)
    private String name;

    @Basic(optional = false)
    @Column(unique = true, nullable = false)
    private String login;

    @NotBlank
    @Column(unique = true, nullable = false)
    private String email;

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
