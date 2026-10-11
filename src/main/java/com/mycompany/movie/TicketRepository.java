package com.mycompany.movie;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<Ticket, Integer> {

    boolean existsBySessionIdAndSeatId(int sessionId, int seatId);

    List<Ticket> findBySessionId(int sessionId);
}