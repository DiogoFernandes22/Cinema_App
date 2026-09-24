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

        // Criar sala NORMAL
        Room room = new Room("NORMAL");

        // Criar sessão
        Session session = new Session(movie, LocalTime.of(21, 0), room, 8.0);

        // Criar reserva associada ao utilizador e à sessão
        Reservation reservation = new Reservation(session, user);

        // Começar a escolher lugares
        reservation.chooseSeat();
        
        System.out.println("Reserva pertence a: " + reservation.getUser());
        
        for(Ticket ticket : reservation.getMyTickets()){
        System.out.println(
        ticket.getSeat() + " - " +
        ticket.getTypeTicket() + " - " +
        ticket.ticketPrice() + "€"
    );
        }
    }
    }
