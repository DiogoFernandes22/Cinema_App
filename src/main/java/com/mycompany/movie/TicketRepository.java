package com.mycompany.movie;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<Ticket, Integer> {

    boolean existsBySessionIdAndSeatId(int sessionId, int seatId);
}