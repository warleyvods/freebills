package com.freebills.controllers.dtos.requests;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

public record LoginRequestDTO(@NotBlank String login, @NotBlank String password) {
}
