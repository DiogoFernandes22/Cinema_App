/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.movie;

/**
 *
 * @author diogo
 */
public class Seat {
    //Atributos do seat, linha e nº do lugar
    private char row;
    private int seatNumber;
    // Construtor
    public Seat(char row, int seatNumber){
        this.row = row;
        this.seatNumber = seatNumber;
    }
    // Getters
    public char getRow(){
        return row;
    }
    
    public int getSeatNumber(){
        return seatNumber;
    }
    
    @Override //trasnformar o objeto seat numa representação em texto 
        public String toString() {
        return "" + row + seatNumber; //"" vazio oara não converter o char para um valor numérico 
}
}