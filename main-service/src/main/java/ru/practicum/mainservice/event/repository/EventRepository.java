package ru.practicum.mainservice.event.repository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.mainservice.event.model.Event;

import java.util.Collection;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    Collection<Event> findAllByInitiator_Id(Long unitiatorId, PageRequest pageRequest);
}
