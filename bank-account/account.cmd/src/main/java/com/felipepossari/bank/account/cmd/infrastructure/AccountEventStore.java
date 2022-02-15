package com.felipepossari.bank.account.cmd.infrastructure;

import com.felipepossari.bank.account.cmd.domain.AccountAggregate;
import com.felipepossari.bank.account.cmd.domain.EventStoreRepository;
import com.felipepossari.bank.cqrs.core.events.BaseEvent;
import com.felipepossari.bank.cqrs.core.events.EventModel;
import com.felipepossari.bank.cqrs.core.exceptions.AggregateNotFoundException;
import com.felipepossari.bank.cqrs.core.exceptions.ConcurrencyException;
import com.felipepossari.bank.cqrs.core.infrastructure.EventStore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountEventStore implements EventStore {

    private final EventStoreRepository eventStoreRepository;

    @Override
    public void saveEvents(String aggregateId, Iterable<BaseEvent> events, int expectedVersion) {
        var eventStream = eventStoreRepository.findByAggregateIdentifier(aggregateId);
        if (expectedVersion != -1 && eventStream.get(eventStream.size() - 1).getVersion() != expectedVersion) {
            throw new ConcurrencyException();
        }
        var version = expectedVersion;
        for (var event : events) {
            version++;
            event.setVersion(version);
            var eventModel = EventModel.builder()
                    .timeStamp(new Date())
                    .aggregateIdentifier(aggregateId)
                    .aggregateType(AccountAggregate.class.getTypeName())
                    .version(version)
                    .eventType(event.getClass().getTypeName())
                    .eventData(event)
                    .build();
            var persistedEvent = eventStoreRepository.save(eventModel);
            if (persistedEvent != null) {

            }
        }
    }

    @Override
    public List<BaseEvent> getEvents(String aggregateId) {
        var eventStream = eventStoreRepository.findByAggregateIdentifier(aggregateId);
        if (CollectionUtils.isEmpty(eventStream)) {
            throw new AggregateNotFoundException("Aggregate not found");
        }
        return eventStream.stream().map(EventModel::getEventData).collect(Collectors.toList());
    }
}
