package com.freebills.controllers;

import com.freebills.controllers.dtos.requests.CreditCardPostRequestDTO;
import com.freebills.controllers.dtos.requests.CreditCardPutRequestDTO;
import com.freebills.controllers.dtos.responses.CreditCardResponseDTO;
import com.freebills.controllers.mappers.CreditCardMapper;
import com.freebills.usecases.CreateCreditCart;
import com.freebills.usecases.FindCreditCard;
import com.freebills.usecases.UpdateCreditCard;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@Tag(name = "CreditCard Controller")
@RestController
@RequiredArgsConstructor
@RequestMapping("v1/cards")
public class CreditCardController {

    private final CreditCardMapper mapper;
    private final FindCreditCard findCreditCard;
    private final UpdateCreditCard updateCreditCard;
    private final CreateCreditCart createCreditCart;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public CreditCardResponseDTO save(@RequestBody @Valid final CreditCardPostRequestDTO creditCardPostRequestDTO) {
        final var creditCard = mapper.toDomain(creditCardPostRequestDTO);
        return mapper.fromDomain(createCreditCart.save(creditCard));
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public List<CreditCardResponseDTO> findAll(Principal principal) {
        return mapper.fromDomainList(findCreditCard.findByLogin(principal.getName()));
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping
    public CreditCardResponseDTO update(@RequestBody @Valid final CreditCardPutRequestDTO creditCardPutRequestDTO) {
        final var creditCardFinded = findCreditCard.byId(creditCardPutRequestDTO.id());
        final var toJson = updateCreditCard.update(mapper.updateCreditCardFromDto(creditCardPutRequestDTO, creditCardFinded));
        return mapper.fromDomain(toJson);
    }
}
