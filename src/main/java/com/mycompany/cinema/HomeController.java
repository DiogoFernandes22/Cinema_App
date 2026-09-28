package com.mycompany.cinema;

import com.mycompany.movie.Movie;
import com.mycompany.movie.Room;
import com.mycompany.movie.Session;
import com.mycompany.movie.Reservation;
import com.mycompany.movie.Seat;
import java.time.LocalTime;
import java.util.ArrayList;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import jakarta.servlet.http.HttpSession;
import com.mycompany.movie.Director;
import com.mycompany.movie.Studio;
import com.mycompany.movie.MovieRepository;
import java.util.List;
import com.mycompany.movie.SessionRepository;
import com.mycompany.movie.ReservationRepository;
import java.util.LinkedHashMap;
import java.util.Map;
import com.mycompany.movie.TicketRepository;


@Controller
public class HomeController {

    private final MovieRepository movieRepository;
    private final SessionRepository sessionRepository;
    private final ReservationRepository reservationRepository;
    private final TicketRepository ticketRepository;

    public HomeController(
        MovieRepository movieRepository,
        SessionRepository sessionRepository,
        ReservationRepository reservationRepository,
        TicketRepository ticketRepository) {

    this.movieRepository = movieRepository;
    this.sessionRepository = sessionRepository;
    this.reservationRepository = reservationRepository;
    this.ticketRepository = ticketRepository;
}

    @GetMapping("/")
    public String home(Model model) {

    List<Session> sessions = sessionRepository.findAll();

    List<Movie> movies = movieRepository.findAll();

    // Filme que aparece inicialmente na página
    Movie movie = movies.get(0);

    // Sessão selecionada inicialmente: 21:00
    Session movieSession = sessions.get(1);

    // Agrupar as sessões por filme
    Map<Movie, List<Session>> sessionsByMovie = new LinkedHashMap<>();

    for (Session session : sessions) {

        Movie sessionMovie = session.getMovie();

        sessionsByMovie
                .computeIfAbsent(sessionMovie, key -> new ArrayList<>())
                .add(session);
    }

    model.addAttribute("movies", movies);
    model.addAttribute("movie", movie);
    model.addAttribute("movieSession", movieSession);
    model.addAttribute("sessions", sessions);
    model.addAttribute("sessionsByMovie", sessionsByMovie);

    return "index";
}

    @GetMapping("/session")
    public String selectSession(
        @RequestParam("id") int id,
        Model model) {

    System.out.println("Sessão escolhida: " + id);

    Session selectedSession = sessionRepository.findById(id).orElse(null);
    
    System.out.println("Sala: " + selectedSession.getRoom().getName());
    System.out.println("Lugares: " + selectedSession.getRoom().GetSeats().length);
    
    System.out.println("Primeiro lugar: "
        + selectedSession.getRoom().GetSeats()[0][0]);

    System.out.println("Último lugar: "
        + selectedSession.getRoom().GetSeats()[9][11]);

    if (selectedSession == null) {
        return "redirect:/";
    }

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

        Session selectedSession =
            sessionRepository.findById(sessionId).orElse(null);

        if (selectedSession == null) {
            return "redirect:/";
        }
        
        //Criar a reserva
        Reservation reservation = new Reservation(selectedSession, null);
        
        //Criar lista dos tipos de bilhete
        ArrayList<String>ticketTypes = new ArrayList<>();
        
        for (int i = 0; i < normal; i++){
            ticketTypes.add("NORMAL");
        }
        
        for (int i = 0; i < estudante; i++){
            ticketTypes.add("ESTUDANTE");
        }
        
        for (int i = 0; i < crianca; i++){
            ticketTypes.add("CRIANÇA");
        }
        
        //Separar os lugares
        String[] selectedSeats = seats.split(",");
        
        //Associar cada lugar ao respetivo bilhete
        for(int i = 0; i < selectedSeats.length; i++){
            
            String seatName = selectedSeats[i];
            
            for(Seat[] row : selectedSession.getRoom().GetSeats()){
                
                for(Seat seat : row){
                    
                    if (seat.toString().equals(seatName)) {

                        boolean occupied = ticketRepository.existsBySessionIdAndSeatId(selectedSession.getId(),seat.getId());
                        if (occupied) {
                            System.out.println("Lugar " + seatName + " já está ocupado.");
                            continue;
                        }

    String ticketType = ticketTypes.get(i);

    reservation.addTicket(ticketType, seat);
}
                }
            }
        }
        
        
        //Guardar a reserva na base de dados
        reservationRepository.save(reservation);
        if (reservation.getMyTickets().isEmpty()) {
            return "redirect:/";
        }
        //Guardar a reserva na base de dados
        reservationRepository.save(reservation);

        //Enviar a Reservation para o confirmation.html
        model.addAttribute("reservation", reservation);

        //Guardar temporariamente a reserva para o /payment
        httpSession.setAttribute("reservation", reservation);

        return "confirmation";
    }
    
    @GetMapping("/payment")
    public String payment(HttpSession httpSession, Model model){
        
        Reservation reservation =
                (Reservation) httpSession.getAttribute("reservation");
        
        if(reservation == null){
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

    // Alterar os dados da reserva
    reservation.setPaymentMethod(paymentMethod);
    reservation.setPaid(true);
    reservation.setConfirmed(true);

    // Guardar as alterações na base de dados
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
                new Session(movie, LocalTime.of(18, 0), room, 8.0);

        Session session2 =
                new Session(movie, LocalTime.of(21, 0), room, 8.0);

        Session session3 =
                new Session(movie, LocalTime.of(23, 30), room, 8.0);

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
}