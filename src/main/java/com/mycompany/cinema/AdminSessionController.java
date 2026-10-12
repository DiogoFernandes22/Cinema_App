package com.mycompany.cinema;
import com.mycompany.movie.PriceType;
import com.mycompany.movie.Movie;
import com.mycompany.movie.Room;
import com.mycompany.movie.Session;
import com.mycompany.movie.MovieRepository;
import com.mycompany.movie.SessionRepository;
import java.time.LocalTime;
import java.time.LocalDate;
import java.util.List;
import com.mycompany.movie.RoomRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.mycompany.movie.TicketRepository;
import java.util.ArrayList;
import java.util.Map;
import java.util.LinkedHashMap;

@RestController
@RequestMapping("/api/admin/sessions")
public class AdminSessionController {

    private final MovieRepository movieRepository;
    private final SessionRepository sessionRepository;
    private final RoomRepository roomRepository;
    private final TicketRepository ticketRepository;

    public AdminSessionController(
        MovieRepository movieRepository,
        SessionRepository sessionRepository,
        RoomRepository roomRepository,
         TicketRepository ticketRepository) {

    this.movieRepository = movieRepository;
    this.sessionRepository = sessionRepository;
    this.roomRepository = roomRepository;
    this.ticketRepository = ticketRepository;
}

    @GetMapping("/movies")
    public List<Movie> getMovies() {
        return movieRepository.findAll();
    }

    @PostMapping
public ResponseEntity<?> createSession(
        @RequestParam int movieId,
        @RequestParam int roomId,
        @RequestParam String date,
        @RequestParam String time) {

    Movie movie = movieRepository.findById(movieId).orElse(null);

    if (movie == null) {
        return ResponseEntity.badRequest()
                .body("Filme não encontrado.");
    }

    Room room = roomRepository.findById(roomId).orElse(null);

    if (room == null) {
        return ResponseEntity.badRequest()
                .body("Sala não encontrada.");
    }

    PriceType priceType;

    if (room.getType().equalsIgnoreCase("IMAX")) {
        priceType = PriceType.IMAX;
    } else {
        priceType = PriceType.NORMAL;
    }

    Session session = new Session(
            movie,
            LocalDate.parse(date),
            LocalTime.parse(time),
            room,
            priceType
    );

    Session savedSession = sessionRepository.save(session);

    return ResponseEntity.ok(savedSession);
}
    @GetMapping("/rooms")
    public List<Room> getRooms() {
        return roomRepository.findAll();
}
    @GetMapping
    public List<Session> getSessions() {
        return sessionRepository.findAll();
}
    @GetMapping("/lotacao")
public List<Map<String, Object>> getSessionsWithOccupancy() {
    List<Session> sessions = sessionRepository.findAll();
    List<Map<String, Object>> result = new ArrayList<>();

    for (Session session : sessions) {
        int capacity = session.getRoom().getCapacity();
        int registeredTickets =
                ticketRepository.findBySessionId(session.getId()).size();

        Map<String, Object> item = new LinkedHashMap<>();
        item.put("id", session.getId());
        item.put("movie", session.getMovie().getName());
        item.put("date", session.getDate());
        item.put("time", session.getTime());
        item.put("room", session.getRoom().getName());
        item.put("capacity", capacity);
        item.put("registeredTickets", registeredTickets);
        item.put("availableSeats",
                Math.max(0, capacity - registeredTickets));

        result.add(item);
    }

    return result;
}
}
