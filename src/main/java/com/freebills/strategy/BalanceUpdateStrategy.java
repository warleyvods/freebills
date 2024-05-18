package com.freebills.strategy;

import com.freebills.domain.Event;

import java.math.BigDecimal;

public interface BalanceUpdateStrategy {

    BigDecimal updateBalance(BigDecimal currentBalance, Event event);
}