package com.freebills.controllers;

import com.freebills.controllers.dtos.requests.TransferPostRequestDTO;
import com.freebills.controllers.dtos.responses.TransferResponseDTO;
import com.freebills.controllers.mappers.TransferMapper;
import com.freebills.usecases.CreateTransfer;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.CREATED;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "Transfer Controller")
@RequestMapping("v1/transfer")
public class TransferController {

    private final TransferMapper transferMapper;
    private final CreateTransfer createTransfer;

    @ResponseStatus(CREATED)
    @PostMapping
    public TransferResponseDTO save(@RequestBody @Valid TransferPostRequestDTO request) {
        return transferMapper.toDTO(createTransfer.execute(transferMapper.toDomain(request)));
    }
}
