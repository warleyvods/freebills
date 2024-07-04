package com.freebills.usecases;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Slf4j
@Component
@RequiredArgsConstructor
public class ReajustAccount {

    private static final String DESCRIPTION = "Reajuste";

    private final FindAccount findAccount;
    private final UpdateAccount updateAccount;
    private final CreateTransaction createTransaction;
    private final ApplicationEventPublisher eventPublisher;

    public void execute(final Long id, final BigDecimal value, final String type) {
        final var account = findAccount.byId(id);
    }
}