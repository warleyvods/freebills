package com.freebills.usecases;

import com.freebills.domain.Transfer;
import com.freebills.gateways.TransferGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FindTransfer {

    private final TransferGateway transferGateway;

    public Transfer byId(final Long id, final String username) {
        return transferGateway.findById(id, username);
    }

    public Page<Transfer> allByUsername(final String username, final Integer year, final Integer month, final Pageable pageable) {
        return transferGateway.findAll(username, year, month, pageable);
    }
}
