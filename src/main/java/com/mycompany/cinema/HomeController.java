package com.mycompany.cinema;

import com.mycompany.movie.Movie;
import com.mycompany.movie.Room;
import com.mycompany.movie.Session;
import com.mycompany.movie.Reservation;
import com.mycompany.movie.Seat;
import com.mycompany.movie.PriceType;
import com.mycompany.movie.Director;
import com.mycompany.movie.Studio;
import com.mycompany.movie.MovieRepository;
import com.mycompany.movie.SessionRepository;
import com.mycompany.movie.ReservationRepository;
import com.mycompany.movie.TicketRepository;
import com.mycompany.movie.RoomRepository;

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

@Controller
public class HomeController {

    private final MovieRepository movieRepository;
    private final SessionRepository sessionRepository;
    private final ReservationRepository reservationRepository;
    private final TicketRepository ticketRepository;
    private final RoomRepository roomRepository;

    public HomeController(
            MovieRepository movieRepository,
            SessionRepository sessionRepository,
            ReservationRepository reservationRepository,
            TicketRepository ticketRepository,
            RoomRepository roomRepository) {

        this.movieRepository = movieRepository;
        this.sessionRepository = sessionRepository;
        this.reservationRepository = reservationRepository;
        this.ticketRepository = ticketRepository;
        this.roomRepository = roomRepository;
    }

    @GetMapping("/")
    public String home(
            @RequestParam(name = "date", required = false) String dateParam,
            Model model) {

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

        // Preparar os próximos 7 dias para a barra de seleção.
        List<LocalDate> availableDates = new ArrayList<>();

        for (int i = 0; i < 7; i++) {
            availableDates.add(today.plusDays(i));
        }

        // Por defeito, escolher hoje se houver sessões.
        // Caso contrário, escolher a próxima data com sessões.
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

        // Obter apenas as sessões da data selecionada.
        LocalDate finalSelectedDate = selectedDate;

        List<Session> sessionsForSelectedDate =
                sessions.stream()
                        .filter(session ->
                                session.getDate() != null
                                && finalSelectedDate.equals(session.getDate())
                        )
                        .sorted(Comparator.comparing(Session::getTime))
                        .toList();

        // Organizar as sessões por filme e sala.
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

        // Indicar ao HTML quais dias têm sessões.
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

        System.out.println(
                "Sala: " + selectedSession.getRoom().getName()
        );

        System.out.println(
                "Lugares: "
                + selectedSession.getRoom().GetSeats().length
        );

        System.out.println(
                "Primeiro lugar: "
                + selectedSession.getRoom().GetSeats()[0][0]
        );

        System.out.println(
                "Último lugar: "
                + selectedSession.getRoom().GetSeats()[9][11]
        );

        model.addAttribute("movieSession", selectedSession);

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
            HttpSession httpSession) {

        System.out.println("Sessão: " + sessionId);
        System.out.println("Lugares escolhidos: " + seats);
        System.out.println("Normal: " + normal);
        System.out.println("Estudante: " + estudante);
        System.out.println("Criança: " + crianca);

        Session selectedSession =
                sessionRepository.findById(sessionId).orElse(null);

        if (selectedSession == null) {
            return "redirect:/";
        }

        Reservation reservation =
                new Reservation(selectedSession, null);

        ArrayList<String> ticketTypes = new ArrayList<>();

        for (int i = 0; i < normal; i++) {
            ticketTypes.add("NORMAL");
        }

        for (int i = 0; i < estudante; i++) {
            ticketTypes.add("ESTUDANTE");
        }

        for (int i = 0; i < crianca; i++) {
            ticketTypes.add("CRIANÇA");
        }

        String[] selectedSeats = seats.split(",");

        for (int i = 0; i < selectedSeats.length; i++) {

            String seatName = selectedSeats[i];

            for (Seat[] row : selectedSession.getRoom().GetSeats()) {

                for (Seat seat : row) {

                    if (seat.toString().equals(seatName)) {

                        boolean occupied =
                                ticketRepository.existsBySessionIdAndSeatId(
                                        selectedSession.getId(),
                                        seat.getId()
                                );

                        if (occupied) {
                            System.out.println(
                                    "Lugar " + seatName + " já está ocupado."
                            );
                            continue;
                        }

                        if (i >= ticketTypes.size()) {
                            continue;
                        }

                        String ticketType = ticketTypes.get(i);

                        reservation.addTicket(ticketType, seat);
                    }
                }
            }
        }

        if (reservation.getMyTickets().isEmpty()) {
            return "redirect:/";
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