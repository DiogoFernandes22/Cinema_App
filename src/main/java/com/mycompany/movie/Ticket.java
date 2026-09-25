/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.movie;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;


/**
 *
 * @author DIOGOFERNANDES
 */
@Entity
@Table(name = "tickets")
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    @Column(name = "type_ticket")
    private String typeTicket;
    
    @ManyToOne
    @JoinColumn(name = "seat_id")
    private Seat seat;
    
    @ManyToOne
    @JoinColumn(name = "session_id")
    private Session session;
    
    @Column(name = "price_paid")
    private double pricePaid;
    
    @ManyToOne
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;
    
    public Ticket(){
        
    }
    public Ticket(String typeTicket, Seat seat, Session session) {
        this.typeTicket = typeTicket;
        this.seat = seat;
        this.session = session;
        this.pricePaid = ticketPrice();
    }

    public String getTypeTicket() {
        return typeTicket;
    }

    public Seat getSeat() {
        return seat;
    }

    public Session getSession() {
        return session;
    }
    
    public void setReservation(Reservation reservation) {
    this.reservation = reservation;
}
    
    public double ticketPrice() {
    double basePrice = session.getTicketPrice();

    if (typeTicket.equals("NORMAL")) {
        return basePrice;
    }
    else if (typeTicket.equals("ESTUDANTE")) {
        return basePrice * 0.75;
    }
    else if (typeTicket.equals("SENIOR")) {
        return basePrice * 0.625;
    }
    else if (typeTicket.equals("CRIANÇA")) {
        return basePrice * 0.5625;
    }
    else {
        System.out.println("Tipo de bilhete inválido");
        return 0;
    }
  }
    public double getPricePaid() {
    return pricePaid;
  }
}
