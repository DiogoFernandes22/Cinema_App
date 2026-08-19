/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.cinema;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
//Assim o HomeController passa a conhecer a classe Movie, isto porque estão em packages diferentes 
import com.mycompany.movie.Movie;
import com.mycompany.movie.Room;
import com.mycompany.movie.Session;
import java.time.LocalTime;
import java.util.ArrayList;
import org.springframework.ui.Model;

/**
 *
 * @author diogo
 */
@Controller
public class HomeController {
    
    @GetMapping("/")
    //O model é o maio de transporte entre o Java e o Thymeleaf. O model vai entregar à página html o que foi preparado no java
    public String home(Model model) {
        
        ArrayList<String> cast = new ArrayList<String>();
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
        
        Session session1 = new Session(movie, LocalTime.of(18, 0), room);
        Session session2 = new Session(movie, LocalTime.of(21, 0), room);
        Session session3 = new Session(movie, LocalTime.of(23, 30), room);
        
        ArrayList<Session> sessions = new ArrayList<>();
        sessions.add(session1);
        sessions.add(session2);
        sessions.add(session3);
        
        ArrayList<String> cast2 = new ArrayList<String>();
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
        movies.add(movie);
        movies.add(movie2);
        model.addAttribute("movies", movies);
        //O primeiro movie é o nome que vamos usar no html e o segundo é o objeto criado em java. addAttribute é a forma de passar dados do java para a página HTML
        model.addAttribute("movie", movie);
        //return "index" = templates/index.html  Browser -> GET -> HomeController -> index -> templates/index.html
        model.addAttribute("movieSession", session2);
        model.addAttribute("sessions", sessions);
        return "index";
       
        
    }
    
}
