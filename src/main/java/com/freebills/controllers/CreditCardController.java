package com.freebills.controllers;

import com.freebills.controllers.dtos.requests.CreditCardPostRequestDTO;
import com.freebills.controllers.dtos.requests.CreditCardPutRequestDTO;
import com.freebills.controllers.dtos.responses.CreditCardResponseDTO;
import com.freebills.controllers.mappers.CreditCardMapper;
import com.freebills.usecases.CreateCreditCart;
import com.freebills.usecases.DeleteCard;
import com.freebills.usecases.FindCreditCard;
import com.freebills.usecases.UpdateCreditCard;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Tag(name = "Credit Card Controller")
@RestController
@RequiredArgsConstructor
@RequestMapping("v1/cards")
public class CreditCardController {

    private final CreditCardMapper creditCardMapper;
    private final FindCreditCard findCreditCard;
    private final UpdateCreditCard updateCreditCard;
    private final CreateCreditCart createCreditCart;
    private final DeleteCard deleteCard;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public CreditCardResponseDTO save(@RequestBody @Valid final CreditCardPostRequestDTO creditCardPostRequestDTO) {
        final var creditCard = creditCardMapper.toDomain(creditCardPostRequestDTO);
        return creditCardMapper.fromDomain(createCreditCart.execute(creditCard));
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public List<CreditCardResponseDTO> findAll(Principal principal) {
        return creditCardMapper.fromDomainList(findCreditCard.execute(principal.getName()));
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("{id}")
    public CreditCardResponseDTO findAll(@PathVariable final Long id) {
        return creditCardMapper.fromDomain(findCreditCard.byId(id));
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping
    public CreditCardResponseDTO update(@RequestBody @Valid final CreditCardPutRequestDTO creditCardPutRequestDTO) {
        final var creditCardFinded = findCreditCard.byId(creditCardPutRequestDTO.id());
        final var toJson = updateCreditCard.execute(creditCardMapper.toDomain(creditCardPutRequestDTO, creditCardFinded));
        return creditCardMapper.fromDomain(toJson);
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("{id}")
    public void deleteCard(@PathVariable final Long id) {
        deleteCard.execute(id);
    }
}
