/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.movie;

import java.time.LocalTime;
import java.util.ArrayList;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Column;
import jakarta.persistence.Transient;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import java.time.LocalDate;

/**
 *
 * @author diogo
 */
@Entity
@Table(name = "sessions")
public class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "movie_id")
    private Movie movie;

    @Column(name = "session_time")
    private LocalTime time;

    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;

    @Transient
    private ArrayList<Seat> occupiedSeats;

    @Column(name = "session_date")
    private LocalDate date;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "price_type")
    private PriceType priceType;

    public Session() {
    }

    public Session(Movie movie, LocalDate date, LocalTime time, Room room, PriceType priceType) {

        this.movie = movie;
        this.date = date;
        this.time = time;
        this.room = room;
        this.priceType = priceType;

        occupiedSeats = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void availableSeats() {

        for (int i = 0; i < room.GetSeats().length; i++) {

            for (int j = 0; j < room.GetSeats()[i].length; j++) {

                Seat seat = room.GetSeats()[i][j];

                if (!occupiedSeats.contains(seat)) {

                    System.out.println(seat);
                }
            }
        }
    }

    // Verificar se o lugar está ocupado,
    // se não colocá-lo no occupiedSeats
    public boolean reserveSeat(Seat seat) {

        if (occupiedSeats.contains(seat)) {

            System.out.println(
                    "Lugar já ocupado, por favor selecione outro"
            );

            return false;

        } else {

            occupiedSeats.add(seat);

            return true;
        }
    }

    public void cancelSeat(Seat seat) {

        occupiedSeats.remove(seat);
    }

    public Room getRoom() {
        return room;
    }

    public Movie getMovie() {
        return movie;
    }

    public LocalTime getTime() {
        return time;
    }
    
    public LocalDate getDate() {
    return date;
    }

    public PriceType getPriceType() {
        return priceType;
    }
}