
package com.mycompany.movie;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation, Integer> {

    List<Reservation> findByUserUsernameOrderByIdDesc(String username);
}
