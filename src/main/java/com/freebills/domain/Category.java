package com.freebills.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.freebills.gateways.entities.enums.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@RequiredArgsConstructor
public class Category implements Serializable {

    private Long id;

    @NonNull
    private String name;

    @NonNull
    private String icon;

    @NonNull
    private String color;

    @NonNull
    private TransactionType categoryType;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @NonNull
    private Boolean archived;

    @NonNull
    @JsonIgnore
    private User user;

}


