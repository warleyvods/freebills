package com.freebills.domains;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "`users`")
@EntityListeners(AuditingEntityListener.class)
public class User extends AbstractEntity {

    @NotBlank
    private String name;

    @Basic(optional = false)
    @Column(unique = true)
    private String login;

    @NotBlank
    @Column(unique = true)
    private String email;

    @NotBlank
    private String password;

    private boolean admin = false;
    private boolean active = true;

    @CreatedDate
    private LocalDate createdAt;

}
