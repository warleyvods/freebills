package com.freebills.controllers;


import com.freebills.controllers.dtos.requests.TransactionPostRequestDTO;
import com.freebills.controllers.dtos.requests.TransactionPutRequestDTO;
import com.freebills.controllers.dtos.requests.TransactionRestorePostRequestDTO;
import com.freebills.controllers.dtos.requests.UploadImageRequestDTO;
import com.freebills.controllers.dtos.requests.TransactionMetadataRequestDTO;
import com.freebills.controllers.dtos.responses.TransactionResponseDTO;
import com.freebills.controllers.dtos.responses.TransactionRestoreResponseDTO;
import com.freebills.controllers.dtos.responses.UploadResponseDTO;
import com.freebills.controllers.dtos.responses.DownloadRequestResult;
import com.freebills.controllers.mappers.TransactionMapper;
import com.freebills.controllers.mappers.FileReferenceMapper;
import com.freebills.gateways.entities.enums.TransactionType;
import com.freebills.gateways.StorageGateway;
import com.freebills.usecases.CreateTransaction;
import com.freebills.usecases.DeleteTransaction;
import com.freebills.usecases.DuplicateTransaction;
import com.freebills.usecases.FindTransaction;
import com.freebills.usecases.UpdateTransaction;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/transactions")
public class TransactionController {

    private final TransactionMapper transactionMapper;
    private final CreateTransaction createTransaction;
    private final UpdateTransaction updateTransaction;
    private final FindTransaction findTransaction;
    private final DeleteTransaction deleteTransaction;
    private final DuplicateTransaction duplicateTransaction;
    private final StorageGateway storageGateway;
    private final FileReferenceMapper fileReferenceMapper;

    //FIX - não deixar usuário gravar se a conta não for dele.
    @ResponseStatus(CREATED)
    @PostMapping
    public TransactionResponseDTO save(@RequestBody @Valid final TransactionPostRequestDTO transactionPostRequestDTO) {
        final var transaction = transactionMapper.toDomain(transactionPostRequestDTO);
        return transactionMapper.fromDomain(createTransaction.execute(transaction));
    }

    @ResponseStatus(OK)
    @PostMapping("/restore/events")
    @PreAuthorize("hasRole('ADMIN')")
    public void restoreEvents(@RequestBody final List<TransactionPostRequestDTO> requestDTOList) {
        final var list = requestDTOList.stream().map(transactionMapper::toDomain).toList();
        list.forEach(createTransaction::execute);
    }

    @ResponseStatus(OK)
    @PostMapping("/restore/transactions")
    @PreAuthorize("hasRole('ADMIN')")
    public void restoreTransactions(@RequestBody final List<TransactionRestorePostRequestDTO> requestDTOList) {
        final var list = requestDTOList.stream().map(transactionMapper::toDomainWithCategoryName).toList();

        // 'Hack' fix: As vezes acontece do completable future não terminar esperar a thread de execução terminar
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Thread was interrupted", e);
        }

        list.forEach(createTransaction::execute);
    }

    @ResponseStatus(OK)
    @GetMapping("/filter")
    public Page<TransactionResponseDTO> findAllWithFilters(
            final Principal principal,
            @RequestParam(required = false) final Integer month,
            @RequestParam(required = false) final Integer year,
            @RequestParam(required = false) final String keyword,
            @RequestParam(required = false) final String transactionType,
            final Pageable pageable) {

        return findTransaction.findAllWithFilters(principal.getName(), month, year, pageable, keyword, transactionType != null ? TransactionType.valueOf(transactionType) : null)
                .map(transactionMapper::fromDomain);
    }

    @ResponseStatus(OK)
    @GetMapping("{id}")
    public TransactionResponseDTO findTransactionById(@PathVariable final Long id) {
        return transactionMapper.fromDomain(findTransaction.findById(id));
    }

    @ResponseStatus(OK)
    @PutMapping
    public TransactionResponseDTO update(@RequestBody @Valid final TransactionPutRequestDTO transactionPutRequestDTO) {
        final var transactionFinded = findTransaction.findById(transactionPutRequestDTO.id());
        final var update = updateTransaction.execute(transactionMapper.updateTransaction(transactionPutRequestDTO, transactionFinded));
        return transactionMapper.fromDomain(update);
    }

    @ResponseStatus(OK)
    @PutMapping(value = "/duplicate")
    public TransactionResponseDTO duplicate(@RequestParam final Long id) {
        final var execute = duplicateTransaction.execute(id);
        return transactionMapper.fromDomain(execute);
    }

    @ResponseStatus(OK)
    @GetMapping("/category")
    public Page<TransactionResponseDTO> findAllByCategory(final Principal principal,
                                                          @RequestParam(required = false) final Integer month,
                                                          @RequestParam(required = false) final Integer year,
                                                          @RequestParam(required = false) final String category,
                                                          @RequestParam(required = false) final String transactionType,
                                                          final Pageable pageable) {

        return findTransaction.findAllByCategory(
                principal.getName(), month, year, category, pageable,
                transactionType != null ? TransactionType.valueOf(transactionType.toUpperCase()) : null).map(transactionMapper::fromDomain);
    }

    @ResponseStatus(NO_CONTENT)
    @DeleteMapping("{id}")
    public void deleteById(@PathVariable final Long id) {
        deleteTransaction.delete(id);
    }

    @GetMapping("/export-transactions")
    public List<TransactionRestoreResponseDTO> exportTransaction(Principal principal) {
        final var allWithFilters = findTransaction
                .findAllWithFilters(principal.getName(), null, null, Pageable.unpaged(), null, null);
        return allWithFilters.map(transactionMapper::fromDomainWithCategoryName).toList();
    }

    @ResponseStatus(OK)
    @PostMapping("/{id}/receipt")
    public UploadResponseDTO addReceipt(@PathVariable final Long id, @RequestBody @Valid final UploadImageRequestDTO request) {
        final var transaction = findTransaction.findById(id);
        final var fileReference = fileReferenceMapper.toDomain(request);
        final var uploadResponse = storageGateway.generateUploadUrl(fileReference);
        
        transaction.setReceipt(uploadResponse);
        updateTransaction.execute(transaction);
        
        return fileReferenceMapper.fromDomain(uploadResponse);
    }

    @ResponseStatus(OK)
    @GetMapping("/{id}/receipt/download")
    public DownloadRequestResult getReceiptDownloadUrl(@PathVariable final Long id) {
        final var transaction = findTransaction.findById(id);
        
        if (transaction.getReceipt() == null) {
            throw new IllegalArgumentException("Transaction has no receipt");
        }
        
        return storageGateway.generateDownloadUrl(transaction.getReceipt());
    }

    @ResponseStatus(OK)
    @PatchMapping("/{id}/metadata")
    public TransactionResponseDTO updateMetadata(@PathVariable final Long id, @RequestBody @Valid final TransactionMetadataRequestDTO request) {
        final var transaction = findTransaction.findById(id);
        
        if (transaction.getMetadata() == null) {
            throw new IllegalArgumentException("Transaction has no metadata");
        }
        
        transaction.getMetadata().setIsRecurring(request.isRecurring());
        transaction.getMetadata().setIsFixed(request.isFixed());
        transaction.getMetadata().setIsCreditCardPayment(request.isCreditCardPayment());
        transaction.getMetadata().setIsBankSlip(request.isBankSlip());
        transaction.getMetadata().setHasPaidConfirmation(request.hasPaidConfirmation());
        transaction.getMetadata().setTags(request.tags());
        transaction.getMetadata().setIsFavorite(request.isFavorite());
        
        return transactionMapper.fromDomain(updateTransaction.execute(transaction));
    }
}
