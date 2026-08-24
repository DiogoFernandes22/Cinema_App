package com.mycompany.cinema;

import com.mycompany.movie.Movie;
import com.mycompany.movie.Room;
import com.mycompany.movie.Session;
import java.time.LocalTime;
import java.util.ArrayList;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

    @GetMapping("/tickets")
    public String tickets(
            @RequestParam("time") String time,
            @RequestParam("normal") int normal,
            @RequestParam("estudante") int estudante,
            @RequestParam("crianca") int crianca,
            Model model) {

        // Transformar a hora recebida pelo formulário num LocalTime
        LocalTime selectedTime = LocalTime.parse(time);

        // Criar novamente as sessões
        ArrayList<Session> sessions = createSessions();

        // Procurar a sessão escolhida
        Session selectedSession = null;

        for (Session session : sessions) {

            if (session.getTime().equals(selectedTime)) {
                selectedSession = session;
                break;
            }
        }

        // Se não encontrou a sessão, volta à página inicial
        if (selectedSession == null) {
            return "redirect:/";
        }

        // Enviar a sessão escolhida para a próxima página
        model.addAttribute("movieSession", selectedSession);

        // Enviar as quantidades de bilhetes
        model.addAttribute("normal", normal);
        model.addAttribute("estudante", estudante);
        model.addAttribute("crianca", crianca);

        // Ir para a página de escolha de lugares
        return "seats";
    }

    @GetMapping("/reserve")
    public String reserve(
            @RequestParam("time") String time,
            @RequestParam("seats") String seats,
            Model model) {

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

        model.addAttribute("movieSession", selectedSession);
        model.addAttribute("seats", seats);

        return "confirmation";
    }

    private ArrayList<Session> createSessions() {

        ArrayList<String> cast = new ArrayList<>();
        cast.add("Sam Worthington");
        cast.add("Zoe Saldana");

        Movie movie = new Movie(
                "Avatar",
                "Ficção Científica",
                162,
                2009,
                "James Cameron",
                cast,
                "20th Century Studios",
                7.8
        );

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

        Movie movie1 = new Movie(
                "Avatar",
                "Ficção Científica",
                162,
                2009,
                "James Cameron",
                cast1,
                "20th Century Studios",
                7.8
        );

        ArrayList<String> cast2 = new ArrayList<>();
        cast2.add("Matthew McConaughey");
        cast2.add("Jessica Chastain");

        Movie movie2 = new Movie(
                "Interstellar",
                "Ficção Científica",
                169,
                2014,
                "Christopher Nolan",
                cast2,
                "Warner Bros.",
                8.7
        );

        ArrayList<Movie> movies = new ArrayList<>();

        movies.add(movie1);
        movies.add(movie2);

        return movies;
    }
}