package com.mycompany.movie;

import java.time.LocalTime;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {

         // Criar utilizador
        User user = new User("Diogo", "1234");

        // Criar filme
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

        // Criar sala NORMAL
        Room room = new Room("NORMAL");

        // Criar sessão
        Session session = new Session(movie, LocalTime.of(21, 0), room);

        // Criar reserva associada ao utilizador e à sessão
        Reservation reservation = new Reservation(session, user);

        // Começar a escolher lugares
        reservation.chooseSeat();
    }
    }
