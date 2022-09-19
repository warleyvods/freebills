package com.freebills.controllers.dtos.requests;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public record AccountReajustDTO(@NotNull Long accountId, @NotNull BigDecimal amount, @NotNull String type) {
}
