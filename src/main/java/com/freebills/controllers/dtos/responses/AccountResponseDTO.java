package com.freebills.controllers.dtos.responses;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccountResponseDTO {

    private Long userId;
    private Double amount;
    private String description;
    private String accountType;
    private boolean dashboard;
    private String bankType;
}
