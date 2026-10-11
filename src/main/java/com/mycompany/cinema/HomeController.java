
package com.mycompany.cinema;

import com.mycompany.movie.Movie;
import com.mycompany.movie.Room;
import com.mycompany.movie.Session;
import com.mycompany.movie.Reservation;
import com.mycompany.movie.Seat;
import com.mycompany.movie.Ticket;
import com.mycompany.movie.PriceType;
import com.mycompany.movie.Director;
import com.mycompany.movie.Studio;
import com.mycompany.movie.MovieRepository;
import com.mycompany.movie.SessionRepository;
import com.mycompany.movie.ReservationRepository;
import com.mycompany.movie.TicketRepository;
import com.mycompany.movie.RoomRepository;
import com.mycompany.movie.User;
import com.mycompany.movie.UserRepository;

import java.time.LocalTime;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Comparator;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;

@Controller
public class HomeController {

    private final MovieRepository movieRepository;
    private final SessionRepository sessionRepository;
    private final ReservationRepository reservationRepository;
    private final TicketRepository ticketRepository;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;

    public HomeController(
            MovieRepository movieRepository,
            SessionRepository sessionRepository,
            ReservationRepository reservationRepository,
            TicketRepository ticketRepository,
            RoomRepository roomRepository,
            UserRepository userRepository) {

        this.movieRepository = movieRepository;
        this.sessionRepository = sessionRepository;
        this.reservationRepository = reservationRepository;
        this.ticketRepository = ticketRepository;
        this.roomRepository = roomRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/")
    public String home(
            @RequestParam(name = "date", required = false) String dateParam,
            Model model,
            @AuthenticationPrincipal UserDetails userDetails) {

        model.addAttribute("loggedIn", userDetails != null);

        if (userDetails != null) {
            model.addAttribute("username", userDetails.getUsername());
        }

        List<Session> sessions = sessionRepository.findAll();

        sessions.sort(
                Comparator
                        .comparing(
                                (Session s) -> s.getMovie().getName()
                        )
                        .thenComparing(
                                Session::getDate,
                                Comparator.nullsLast(Comparator.naturalOrder())
                        )
                        .thenComparing(Session::getTime)
        );

        LocalDate today = LocalDate.now();

        List<LocalDate> availableDates = new ArrayList<>();

        for (int i = 0; i < 7; i++) {
            availableDates.add(today.plusDays(i));
        }

        LocalDate selectedDate = null;

        if (dateParam != null && !dateParam.isBlank()) {
            try {
                LocalDate requestedDate = LocalDate.parse(dateParam);

                if (availableDates.contains(requestedDate)) {
                    selectedDate = requestedDate;
                }
            } catch (Exception e) {
                selectedDate = null;
            }
        }

        if (selectedDate == null) {
            selectedDate = availableDates.stream()
                    .filter(date -> sessions.stream().anyMatch(
                            session -> date.equals(session.getDate())
                    ))
                    .findFirst()
                    .orElse(today);
        }

        LocalDate finalSelectedDate = selectedDate;

        List<Session> sessionsForSelectedDate =
                sessions.stream()
                        .filter(session ->
                                session.getDate() != null
                                && finalSelectedDate.equals(session.getDate())
                        )
                        .sorted(Comparator.comparing(Session::getTime))
                        .toList();

        Map<String, Map<String, List<Session>>> selectedDateSchedule =
                new LinkedHashMap<>();

        for (Session session : sessionsForSelectedDate) {

            String movieName = session.getMovie().getName();
            String roomName = session.getRoom().getName();

            selectedDateSchedule
                    .computeIfAbsent(
                            movieName,
                            key -> new LinkedHashMap<>()
                    )
                    .computeIfAbsent(
                            roomName,
                            key -> new ArrayList<>()
                    )
                    .add(session);
        }

        Map<LocalDate, Boolean> datesWithSessions = new LinkedHashMap<>();

        for (LocalDate date : availableDates) {
            boolean hasSessions = sessions.stream().anyMatch(
                    session -> date.equals(session.getDate())
            );

            datesWithSessions.put(date, hasSessions);
        }

        List<Movie> movies = movieRepository.findAll();

        Movie movie = movies.isEmpty() ? null : movies.get(0);

        Session movieSession =
                sessions.size() > 1 ? sessions.get(1)
                : sessions.isEmpty() ? null
                : sessions.get(0);

        Map<Movie, List<Session>> sessionsByMovie =
                new LinkedHashMap<>();

        for (Session session : sessions) {

            Movie sessionMovie = session.getMovie();

            sessionsByMovie
                    .computeIfAbsent(
                            sessionMovie,
                            key -> new ArrayList<>()
                    )
                    .add(session);
        }

        Map<String, Map<LocalDate, Map<String, List<Session>>>>
                sessionsSchedule = new LinkedHashMap<>();

        for (Session session : sessions) {

            String movieName = session.getMovie().getName();
            LocalDate date = session.getDate();
            String roomName = session.getRoom().getName();

            sessionsSchedule
                    .computeIfAbsent(
                            movieName,
                            key -> new LinkedHashMap<>()
                    )
                    .computeIfAbsent(
                            date,
                            key -> new LinkedHashMap<>()
                    )
                    .computeIfAbsent(
                            roomName,
                            key -> new ArrayList<>()
                    )
                    .add(session);
        }

        model.addAttribute("movies", movies);
        model.addAttribute("movie", movie);
        model.addAttribute("movieSession", movieSession);
        model.addAttribute("sessions", sessions);
        model.addAttribute("sessionsByMovie", sessionsByMovie);
        model.addAttribute("sessionsSchedule", sessionsSchedule);

        model.addAttribute("today", today);
        model.addAttribute("availableDates", availableDates);
        model.addAttribute("datesWithSessions", datesWithSessions);
        model.addAttribute("selectedDate", selectedDate);
        model.addAttribute(
                "sessionsForSelectedDate",
                sessionsForSelectedDate
        );
        model.addAttribute(
                "selectedDateSchedule",
                selectedDateSchedule
        );

        return "index";
    }

    @GetMapping("/session")
    public String selectSession(
            @RequestParam("id") int id,
            Model model) {

        System.out.println("Sessão escolhida: " + id);

        Session selectedSession =
                sessionRepository.findById(id).orElse(null);

        if (selectedSession == null) {
            return "redirect:/";
        }

        List<Seat> occupiedSeats = new ArrayList<>();

        List<Ticket> tickets =
                ticketRepository.findBySessionId(selectedSession.getId());

        for (Ticket ticket : tickets) {
            if (ticket.getSeat() != null) {
                occupiedSeats.add(ticket.getSeat());
            }
        }

        model.addAttribute("movieSession", selectedSession);
        model.addAttribute("occupiedSeats", occupiedSeats);

        return "booking";
    }

    @GetMapping("/reserve")
    public String reserve(
            @RequestParam("sessionId") int sessionId,
            @RequestParam("normal") int normal,
            @RequestParam("estudante") int estudante,
            @RequestParam("crianca") int crianca,
            @RequestParam("seats") String seats,
            Model model,
            HttpSession httpSession,
            RedirectAttributes redirectAttributes,
            @AuthenticationPrincipal UserDetails userDetails) {

        Session selectedSession =
                sessionRepository.findById(sessionId).orElse(null);

        if (selectedSession == null) {
            return "redirect:/";
        }

        if (normal < 0 || estudante < 0 || crianca < 0) {
            redirectAttributes.addFlashAttribute(
                    "bookingError",
                    "As quantidades de bilhetes não são válidas."
            );

            return "redirect:/session?id=" + sessionId;
        }

        int totalTickets = normal + estudante + crianca;

        if (totalTickets <= 0 || seats == null || seats.isBlank()) {
            redirectAttributes.addFlashAttribute(
                    "bookingError",
                    "Escolha os bilhetes e os respetivos lugares."
            );

            return "redirect:/session?id=" + sessionId;
        }

        List<String> ticketTypes = new ArrayList<>();

        for (int i = 0; i < normal; i++) {
            ticketTypes.add("NORMAL");
        }

        for (int i = 0; i < estudante; i++) {
            ticketTypes.add("ESTUDANTE");
        }

        for (int i = 0; i < crianca; i++) {
            ticketTypes.add("CRIANÇA");
        }

        String[] selectedSeatNames = seats.split(",", -1);

        if (selectedSeatNames.length != totalTickets) {
            redirectAttributes.addFlashAttribute(
                    "bookingError",
                    "O número de lugares deve corresponder ao número de bilhetes."
            );

            return "redirect:/session?id=" + sessionId;
        }

        List<Seat> seatsToReserve = new ArrayList<>();

        for (String seatName : selectedSeatNames) {

            seatName = seatName.trim();

            if (seatName.isEmpty()) {
                redirectAttributes.addFlashAttribute(
                        "bookingError",
                        "A seleção de lugares não é válida."
                );

                return "redirect:/session?id=" + sessionId;
            }

            for (Seat selectedSeat : seatsToReserve) {
                if (selectedSeat.toString().equals(seatName)) {
                    redirectAttributes.addFlashAttribute(
                            "bookingError",
                            "Não pode selecionar o mesmo lugar mais do que uma vez."
                    );

                    return "redirect:/session?id=" + sessionId;
                }
            }

            Seat matchingSeat = null;

            for (Seat[] row : selectedSession.getRoom().GetSeats()) {
                for (Seat seat : row) {

                    if (seat != null && seat.toString().equals(seatName)) {
                        matchingSeat = seat;
                        break;
                    }
                }

                if (matchingSeat != null) {
                    break;
                }
            }

            if (matchingSeat == null) {
                redirectAttributes.addFlashAttribute(
                        "bookingError",
                        "Um dos lugares selecionados não existe nesta sala."
                );

                return "redirect:/session?id=" + sessionId;
            }

            boolean occupied =
                    ticketRepository.existsBySessionIdAndSeatId(
                            selectedSession.getId(),
                            matchingSeat.getId()
                    );

            if (occupied) {
                redirectAttributes.addFlashAttribute(
                        "bookingError",
                        "O lugar " + seatName
                        + " já foi reservado. Escolha outros lugares."
                );

                return "redirect:/session?id=" + sessionId;
            }

            seatsToReserve.add(matchingSeat);
        }

        // Procurar o utilizador autenticado.
        // Se não houver sessão iniciada, a reserva permanece anónima.
        User user = null;

        if (userDetails != null) {
            user = userRepository
                    .findByUsername(userDetails.getUsername())
                    .orElse(null);
        }

        // Criar a reserva associada ao utilizador, quando existe.
        Reservation reservation =
                new Reservation(selectedSession, user);

        for (int i = 0; i < seatsToReserve.size(); i++) {
            reservation.addTicket(
                    ticketTypes.get(i),
                    seatsToReserve.get(i)
            );
        }

        reservationRepository.save(reservation);

        model.addAttribute("reservation", reservation);

        httpSession.setAttribute("reservation", reservation);

        return "confirmation";
    }

    @GetMapping("/payment")
    public String payment(
            HttpSession httpSession,
            Model model) {

        Reservation reservation =
                (Reservation) httpSession.getAttribute("reservation");

        if (reservation == null) {
            return "redirect:/";
        }

        model.addAttribute("reservation", reservation);

        return "payment";
    }

    @GetMapping("/pay")
    public String pay(
            @RequestParam("paymentMethod") String paymentMethod,
            HttpSession httpSession,
            Model model) {

        Reservation reservation =
                (Reservation) httpSession.getAttribute("reservation");

        if (reservation == null) {
            return "redirect:/";
        }

        reservation.setPaymentMethod(paymentMethod);
        reservation.setPaid(true);
        reservation.setConfirmed(true);

        reservationRepository.save(reservation);

        model.addAttribute("reservation", reservation);

        return "confirmed";
    }

    private ArrayList<Session> createSessions() {

        ArrayList<String> cast = new ArrayList<>();

        cast.add("Sam Worthington");
        cast.add("Zoe Saldana");

        Director director = new Director();
        director.setName("James Cameron");

        Studio studio = new Studio();
        studio.setName("20th Century Studios");

        Movie movie = new Movie();

        movie.setName("Avatar");
        movie.setDuration(162);
        movie.setReleaseDate(2009);
        movie.setRate(7.8);
        movie.setDirector(director);
        movie.setStudio(studio);

        Room room = new Room("NORMAL");

        Session session1 =
                new Session(
                        movie,
                        LocalDate.of(2026, 10, 10),
                        LocalTime.of(18, 0),
                        room,
                        PriceType.NORMAL
                );

        Session session2 =
                new Session(
                        movie,
                        LocalDate.of(2026, 10, 10),
                        LocalTime.of(21, 0),
                        room,
                        PriceType.NORMAL
                );

        Session session3 =
                new Session(
                        movie,
                        LocalDate.of(2026, 10, 10),
                        LocalTime.of(23, 30),
                        room,
                        PriceType.NORMAL
                );

        ArrayList<Session> sessions = new ArrayList<>();

        sessions.add(session1);
        sessions.add(session2);
        sessions.add(session3);

        return sessions;
    }

    private ArrayList<Movie> createMovies() {

        ArrayList<String> cast1 = new ArrayList<>();

        cast1.add("Sam Worthington");
        cast1.add("Zoe Saldana");

        Director director1 = new Director();
        director1.setName("James Cameron");

        Studio studio1 = new Studio();
        studio1.setName("20th Century Studios");

        Movie movie1 = new Movie();

        movie1.setName("Avatar");
        movie1.setDuration(162);
        movie1.setReleaseDate(2009);
        movie1.setRate(7.8);
        movie1.setDirector(director1);
        movie1.setStudio(studio1);

        ArrayList<String> cast2 = new ArrayList<>();

        cast2.add("Matthew McConaughey");
        cast2.add("Jessica Chastain");

        Director director2 = new Director();
        director2.setName("Christopher Nolan");

        Studio studio2 = new Studio();
        studio2.setName("Warner Bros.");

        Movie movie2 = new Movie();

        movie2.setName("Interstellar");
        movie2.setDuration(169);
        movie2.setReleaseDate(2014);
        movie2.setRate(8.7);
        movie2.setDirector(director2);
        movie2.setStudio(studio2);

        ArrayList<Movie> movies = new ArrayList<>();

        movies.add(movie1);
        movies.add(movie2);

        return movies;
    }

    @GetMapping("/admin/sessions/create")
    public String createSessionPage(Model model) {

        model.addAttribute(
                "movies",
                movieRepository.findAll()
        );

        model.addAttribute(
                "rooms",
                roomRepository.findAll()
        );

        return "admin-session-create";
    }

    @PostMapping("/admin/sessions/create")
    public String createSession(
            @RequestParam("movieId") int movieId,
            @RequestParam("roomId") int roomId,
            @RequestParam("time") LocalTime time,
            @RequestParam("priceType") String priceType) {

        Movie movie =
                movieRepository.findById(movieId).orElse(null);

        Room room =
                roomRepository.findById(roomId).orElse(null);

        if (movie == null || room == null) {
            return "redirect:/admin/sessions/create";
        }

        PriceType sessionPriceType;

        if (room.getType().equalsIgnoreCase("IMAX")) {
            sessionPriceType = PriceType.IMAX;
        } else {
            sessionPriceType = PriceType.NORMAL;
        }

        Session session = new Session(
                movie,
                LocalDate.of(2026, 10, 10),
                time,
                room,
                sessionPriceType
        );

        sessionRepository.save(session);

        return "redirect:/admin/sessions/create";
    }
}
