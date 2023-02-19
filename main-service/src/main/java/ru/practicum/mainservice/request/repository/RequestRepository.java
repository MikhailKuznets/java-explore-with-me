package ru.practicum.mainservice.request.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.mainservice.request.model.ParticipationRequest;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface RequestRepository extends JpaRepository<ParticipationRequest, Long> {
    Optional<ParticipationRequest> findByRequester_IdAndEvent_Id(Long requesterId, Long eventId);

    Collection<ParticipationRequest> findAllByRequester_Id(Long requesterId);

    Collection<ParticipationRequest> findAllByEvent_Id(Long eventId);

    Collection<ParticipationRequest> findByIdIn(List<Long> requestIds);
}