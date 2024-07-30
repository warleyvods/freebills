package com.freebills.controllers;

import com.freebills.controllers.dtos.responses.TransactionRestoreResponseDTO;
import com.freebills.controllers.dtos.responses.UserResponseDTO;
import com.freebills.controllers.mappers.TransactionMapper;
import com.freebills.controllers.mappers.UserMapper;
import com.freebills.domain.Event;
import com.freebills.domain.Transaction;
import com.freebills.gateways.EventGateway;
import com.freebills.gateways.UserGateway;
import com.freebills.gateways.entities.enums.EventType;
import com.freebills.usecases.FindTransaction;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.http.HttpStatus.OK;

@Tag(name = "Me Controller")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
@RequestMapping("v1/me")
@RestController
public class MeController {

    private final UserGateway userGateway;
    private final UserMapper userMapper;
    private final EventGateway eventGateway;
    private final FindTransaction findTransaction;
    private final TransactionMapper transactionMapper;

    @ResponseStatus(OK)
    @GetMapping
    public UserResponseDTO findById(Principal principal) {
        return userMapper.fromDomain(userGateway.findByLogin(principal.getName()));
    }

    @GetMapping("{id}")
    public List<TransactionRestoreResponseDTO>  get(@PathVariable Long id) {
        final List<Event> eventsByAggregateId = eventGateway.getEventsByAggregateId(id);

        final Map<Long, Event> latestEventsMap = new HashMap<>();

        for (Event event : eventsByAggregateId) {
            Transaction transactionData = event.getTransactionData();
            if (transactionData != null) {
                Long transactionId = transactionData.getId();
                if (transactionId != null) {
                    Event existingEvent = latestEventsMap.get(transactionId);
                    if (existingEvent == null || event.getCreatedAt().isAfter(existingEvent.getCreatedAt())) {
                        latestEventsMap.put(transactionId, event);
                    }

                    if (event.getEventType().equals(EventType.TRANSACTION_DELETED)) {
                        latestEventsMap.remove(transactionId);
                    }
                }
            }
        }

        List<Event> latestEvents = new ArrayList<>(latestEventsMap.values());

        return latestEvents.stream().map(Event::getTransactionData)
                .toList()
                .stream().map(transactionMapper::fromDomainWithCategoryName)
                .toList();
    }
}
