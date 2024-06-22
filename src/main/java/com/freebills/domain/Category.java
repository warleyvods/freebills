package com.freebills.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder(toBuilder = true)
public class Category {

    private Long id;
    private String name;
    private String color;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @JsonIgnore
    private User user;

}
