package ru.practicum.statserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.statserver.model.Hit;

@Repository
public interface StatRepository extends JpaRepository<Hit, Long> {
}
