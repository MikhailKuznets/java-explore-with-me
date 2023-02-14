package ru.practicum.mainservice.event.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.mainservice.event.model.Event;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
}
