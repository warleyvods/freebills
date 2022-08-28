package com.freebills.auth.dtos;

import javax.validation.constraints.NotBlank;

public record LoginRequestDTO(@NotBlank String login, @NotBlank String password) {
}
