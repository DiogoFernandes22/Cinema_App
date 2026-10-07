package com.mycompany.cinema;
import com.mycompany.movie.PriceType;
import com.mycompany.movie.Movie;
import com.mycompany.movie.Room;
import com.mycompany.movie.Session;
import com.mycompany.movie.MovieRepository;
import com.mycompany.movie.SessionRepository;
import java.time.LocalTime;
import java.util.List;
import com.mycompany.movie.RoomRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/sessions")
public class AdminSessionController {

    private final MovieRepository movieRepository;
    private final SessionRepository sessionRepository;
    private final RoomRepository roomRepository;

    public AdminSessionController(
        MovieRepository movieRepository,
        SessionRepository sessionRepository,
        RoomRepository roomRepository) {

    this.movieRepository = movieRepository;
    this.sessionRepository = sessionRepository;
    this.roomRepository = roomRepository;
}

    @GetMapping("/movies")
    public List<Movie> getMovies() {
        return movieRepository.findAll();
    }

    @PostMapping
public ResponseEntity<?> createSession(
        @RequestParam int movieId,
        @RequestParam int roomId,
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
}
