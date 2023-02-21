package ru.practicum.statserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.statserver.mapper.ViewStat;
import ru.practicum.statserver.model.Hit;

import java.time.LocalDateTime;
import java.util.Collection;

@Repository
public interface StatRepository extends JpaRepository<Hit, Long> {

    @Query("SELECT h.app AS app, h.uri AS uri, COUNT(h.ip) AS hits " +
            "FROM Hit AS h " +
            "WHERE h.created BETWEEN :start AND :end " +
            "GROUP BY app, uri " +
            "ORDER BY hits DESC")
    Collection<ViewStat> getStat(LocalDateTime start, LocalDateTime end);

    @Query("SELECT h.app AS app, h.uri AS uri, COUNT(DISTINCT h.ip) AS hits " +
            "FROM Hit AS h " +
            "WHERE h.created BETWEEN :start AND :end " +
            "GROUP BY app, uri " +
            "ORDER BY hits DESC")
    Collection<ViewStat> getStatUniqueIp(LocalDateTime start, LocalDateTime end);

    @Query("SELECT h.app AS app, h.uri AS uri, COUNT(h.ip) AS hits " +
            "FROM Hit AS h " +
            "WHERE h.created BETWEEN :start AND :end AND uri IN (:uris) " +
            "GROUP BY app, uri " +
            "ORDER BY hits DESC")
    Collection<ViewStat> getStatByUri(LocalDateTime start, LocalDateTime end, String[] uris);

    @Query("SELECT h.app AS app, h.uri AS uri, COUNT(DISTINCT h.ip) AS hits " +
            "FROM Hit AS h " +
            "WHERE h.created BETWEEN :start AND :end AND uri IN (:uris) " +
            "GROUP BY app, uri " +
            "ORDER BY hits DESC")
    Collection<ViewStat> getStatUniqueIpByUri(LocalDateTime start, LocalDateTime end, String[] uris);

}
