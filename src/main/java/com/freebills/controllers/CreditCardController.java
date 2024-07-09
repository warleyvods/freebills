package com.freebills.controllers;

import com.freebills.controllers.dtos.requests.CreditCardPostRequestDTO;
import com.freebills.controllers.dtos.requests.CreditCardPutRequestDTO;
import com.freebills.controllers.dtos.responses.CreditCardResponseDTO;
import com.freebills.controllers.mappers.CreditCardMapper;
import com.freebills.usecases.CreateCreditCard;
import com.freebills.usecases.DeleteCreditCard;
import com.freebills.usecases.FindCreditCard;
import com.freebills.usecases.UpdateCreditCard;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

@Tag(name = "Credit Card Controller")
@RestController
@RequiredArgsConstructor
@RequestMapping("v1/credit-card")
public class CreditCardController {

    private final FindCreditCard findCreditCard;
    private final UpdateCreditCard updateCreditCard;
    private final CreateCreditCard createCreditCart;
    private final DeleteCreditCard deleteCard;
    private final CreditCardMapper creditCardMapper;

    @ResponseStatus(CREATED)
    @PostMapping
    public CreditCardResponseDTO save(@RequestBody @Valid final CreditCardPostRequestDTO request, Principal principal) {
        final var creditCard = creditCardMapper.toDomain(request);
        return creditCardMapper.fromDomain(createCreditCart.execute(creditCard, principal.getName()));
    }

    @ResponseStatus(OK)
    @GetMapping
    public List<CreditCardResponseDTO> findAll(final Principal principal) {
        return creditCardMapper.fromDomainList(findCreditCard.findAll(principal.getName()));
    }

    @ResponseStatus(OK)
    @GetMapping("{id}")
    public CreditCardResponseDTO findById(@PathVariable final Long id, final Principal principal) {
        return creditCardMapper.fromDomain(findCreditCard.byId(id, principal.getName()));
    }

    @ResponseStatus(OK)
    @PutMapping
    public CreditCardResponseDTO update(@RequestBody @Valid final CreditCardPutRequestDTO request, Principal principal) {
        final var creditCardFinded = findCreditCard.byId(request.id(), principal.getName());
        final var toJson = updateCreditCard.execute(creditCardMapper.toDomain(request, creditCardFinded), principal.getName());
        return creditCardMapper.fromDomain(toJson);
    }

    @ResponseStatus(NO_CONTENT)
    @DeleteMapping("{id}")
    public void delete(@PathVariable final Long id, final Principal principal) {
        deleteCard.execute(id, principal.getName());
    }
}
