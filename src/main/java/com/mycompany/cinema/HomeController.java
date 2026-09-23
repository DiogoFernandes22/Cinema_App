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


@Controller
public class HomeController {

    @GetMapping("/")
    public String home(Model model) {

        ArrayList<Session> sessions = createSessions();

        ArrayList<Movie> movies = createMovies();

        // Filme que aparece inicialmente na página
        Movie movie = sessions.get(0).getMovie();

        // Sessão selecionada inicialmente: 21:00
        Session movieSession = sessions.get(1);

        model.addAttribute("movies", movies);
        model.addAttribute("movie", movie);
        model.addAttribute("movieSession", movieSession);
        model.addAttribute("sessions", sessions);

        return "index";
    }

    @GetMapping("/session")
    public String selectSession(
            @RequestParam("time") String time,
            Model model) {

        System.out.println("Sessão escolhida: " + time);

        ArrayList<Session> sessions = createSessions();

        LocalTime selectedTime = LocalTime.parse(time);

        Session selectedSession = null;

        for (Session session : sessions) {

            if (session.getTime().equals(selectedTime)) {
                selectedSession = session;
                break;
            }
        }

        if (selectedSession == null) {
            return "redirect:/";
        }

        model.addAttribute("movieSession", selectedSession);

        return "booking";
    }

    @GetMapping("/reserve")
    public String reserve(
            @RequestParam("time") String time,
            @RequestParam("normal") int normal,
            @RequestParam("estudante") int estudante,
            @RequestParam("crianca") int crianca,
            @RequestParam("seats") String seats,
            Model model, 
            HttpSession httpSession) {

        System.out.println("Sessão: " + time);
        System.out.println("Lugares escolhidos: " + seats);

        LocalTime selectedTime = LocalTime.parse(time);

        ArrayList<Session> sessions = createSessions();

        Session selectedSession = null;

        for (Session session : sessions) {

            if (session.getTime().equals(selectedTime)) {
                selectedSession = session;
                break;
            }
        }

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
                    
                    if (seat.toString().equals(seatName)){
                        
                        String ticketType = ticketTypes.get(i);
                        
                        reservation.addTicket(ticketType, seat);
                    }
                }
            }
        }
        
        //Enviar a Reservation para o confirmation.html
        model.addAttribute("reservation", reservation);
         //guarda temporariamente a reserva para o /confirm
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
    public String pay(@RequestParam("paymentMethod") String paymentMethod, HttpSession httpSession, Model model){
        
        Reservation reservation = (Reservation) httpSession.getAttribute("reservation");
        
        if(reservation == null){
            return "redirect:/";
        }
        
        //Guardar o método de pagamento escolhido
        reservation.setPaymentMethod(paymentMethod);
        
        //Simular o pagamento
        reservation.setPaid(true);
        
        //A reserva fica confirmada
        reservation.setConfirmed(true);
        
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
                new Session(movie, LocalTime.of(18, 0), room);

        Session session2 =
                new Session(movie, LocalTime.of(21, 0), room);

        Session session3 =
                new Session(movie, LocalTime.of(23, 30), room);

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