package com.freebills.gateways;

import com.freebills.domain.Event;
import com.freebills.gateways.mapper.EventGatewayMapper;
import com.freebills.repositories.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EventGateway {

    private final EventRepository eventRepository;
    private final EventGatewayMapper eventGatewayMapper;

    public Event save(final Event event) {
        return eventGatewayMapper.toDomainWithJson(eventRepository.save(eventGatewayMapper.toEntityWithJson(event)));
    }

    public List<Event> getEventsByAggregateId(final Long aggregateId) {
        return eventRepository.findByAggregateId(aggregateId).stream()
                .map(eventGatewayMapper::toDomainWithJson)
                .toList();
    }

    public void saveAll(final List<Event> events) {
        eventRepository.saveAll(events.stream()
                .map(eventGatewayMapper::toEntityWithJson)
                .toList());
    }
}
