package com.freebills.strategy;

import com.freebills.domain.Event;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public interface BalanceUpdateStrategy {

    BigDecimal updateBalance(BigDecimal currentBalance, Event event);
}