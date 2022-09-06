package com.freebills.usecases;

import com.freebills.controllers.dtos.responses.DashboardResponseDTO;
import com.freebills.domains.Account;
import com.freebills.gateways.AccountGateway;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public record Dashboard(AccountGateway accountGateway) {

    public DashboardResponseDTO totalBalanceById(final Long id) {
        final var totalValue = accountGateway.findByUserId(id)
                .stream()
                .filter(Account::isDashboard)
                .map(Account::getAmount)
                .reduce(BigDecimal::add)
                .orElse(new BigDecimal(0));

        return new DashboardResponseDTO(totalValue, new BigDecimal(0), new BigDecimal(0),  new BigDecimal(0));
    }
}
