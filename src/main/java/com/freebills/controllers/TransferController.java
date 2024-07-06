package com.freebills.controllers;

import com.freebills.controllers.dtos.requests.TransferPostRequestDTO;
import com.freebills.controllers.dtos.requests.TransferPutRequestDTO;
import com.freebills.controllers.dtos.responses.TransferResponseDTO;
import com.freebills.controllers.mappers.TransferMapper;
import com.freebills.domain.Transfer;
import com.freebills.usecases.CreateTransfer;
import com.freebills.usecases.DeleteTransfer;
import com.freebills.usecases.FindTransfer;
import com.freebills.usecases.UpdateTransfer;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "Transfer Controller")
@RequestMapping("v1/transfer")
public class TransferController {

    private final TransferMapper transferMapper;
    private final CreateTransfer createTransfer;
    private final FindTransfer findTransfer;
    private final DeleteTransfer deleteTransfer;
    private final UpdateTransfer updateTransfer;

    @ResponseStatus(CREATED)
    @PostMapping
    public TransferResponseDTO save(@RequestBody @Valid TransferPostRequestDTO request) {
        return transferMapper.toDTO(createTransfer.execute(transferMapper.toDomain(request)));
    }

    @ResponseStatus(OK)
    @GetMapping("{id}")
    public TransferResponseDTO findById(@PathVariable final Long id, final Principal principal) {
        return transferMapper.toDTO(findTransfer.byId(id, principal.getName()));
    }

    @ResponseStatus(OK)
    @PutMapping
    public TransferResponseDTO update(@RequestBody @Valid TransferPutRequestDTO request, Principal principal) {
        final Transfer transfer = findTransfer.byId(request.id(), principal.getName());
        return transferMapper.toDTO(updateTransfer.execute(transferMapper.toDomain(request, transfer), principal.getName()));
    }

    @ResponseStatus(OK)
    @GetMapping
    public Page<TransferResponseDTO> findAll(@RequestParam(required = false) final Integer year,
                                             @RequestParam(required = false) final Integer month,
                                             final Principal principal,
                                             final Pageable pageable) {
        return findTransfer.allByUsername(principal.getName(), year, month, pageable).map(transferMapper::toDTO);
    }

    @ResponseStatus(NO_CONTENT)
    @DeleteMapping("{id}")
    public void deleteByIdAndUsername(@PathVariable final Long id, Principal principal) {
        deleteTransfer.execute(id, principal.getName());
    }


}
