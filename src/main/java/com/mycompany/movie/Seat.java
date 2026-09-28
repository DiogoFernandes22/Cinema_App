/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.movie;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;

/**
 *
 * @author diogo
 */
@Entity
@Table(name = "seats")
public class Seat {

    @Id
    @Column(name = "id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;

    @Column(name = "row_letter")
    private char row;

    @Column(name = "seat_number")
    private int seatNumber;

    public Seat() {
    }

    public Seat(char row, int seatNumber) {
        this.row = row;
        this.seatNumber = seatNumber;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Room getRoom() {
    return room;
}

    public void setRoom(Room room) {
    this.room = room;
}

    public char getRow() {
        return row;
    }

    public int getSeatNumber() {
        return seatNumber;
    }

    @Override
    public String toString() {
        return "" + row + seatNumber;
    }
}