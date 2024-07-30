package com.freebills.gateways;

import com.freebills.domain.Transfer;
import com.freebills.exceptions.TransferNotFoundException;
import com.freebills.gateways.mapper.TransferGatewayMapper;
import com.freebills.repositories.TransferRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransferGateway {

    private final TransferRepository transferRepository;
    private final TransferGatewayMapper transferGatewayMapper;

    public Transfer save(final Transfer transfer) {
        return transferGatewayMapper.toDomain(transferRepository.save(transferGatewayMapper.toEntity(transfer)));
    }

    public Page<Transfer> findAll(final String username, final Integer year, final Integer month,final Pageable pageable) {
        return transferRepository.findAllByUsername(username, year, month, pageable).map(transferGatewayMapper::toDomain);
    }

    public Transfer findById(final Long id, final String username) {
        return transferGatewayMapper.toDomain(transferRepository.findByTransferIdAndUser(id, username)
                .orElseThrow(() -> new TransferNotFoundException("Transfer id: " + id + " not found!")));
    }

    public Transfer update(final Transfer transfer) {
        return transferGatewayMapper.toDomain(transferRepository.save(transferGatewayMapper.toEntity(transfer)));
    }

    public void deleteById(final Long id, final String username) {
        transferRepository.deleteByIdAndUsername(id, username);
    }
}
