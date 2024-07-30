package com.freebills.usecases;

import com.freebills.domain.CCTransaction;
import com.freebills.gateways.CCTransactionGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreateCCTransaction {

    private final CCTransactionGateway ccTransactionGateway;

    public CCTransaction execute(final CCTransaction ccTransaction) {
        return ccTransactionGateway.save(ccTransaction);
    }
}
