package ru.practicum.mainservice.request.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.mainservice.request.model.Request;

import java.util.Optional;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {
    Optional<Request> findByRequester_IdAndEvent_Id(Long requesterId, Long eventId);
}
