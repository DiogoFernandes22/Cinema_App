/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.movie;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;

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

    @Column(name = "room_id")
    private int roomId;

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

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
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