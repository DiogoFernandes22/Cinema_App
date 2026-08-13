/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.movie;

/**
 *
 * @author DIOGOFERNANDES
 */
public class Ticket {
    private String typeTicket;
    private Seat seat;
    
    
    public Ticket(String typeTicket, Seat seat){
        this.typeTicket = typeTicket;
        // Assim quando criamos um bilhete sabemos qual lugar está associado ao mesmo
        this.seat = seat;
      
    }
    
    public double ticketPrice(){
        if(typeTicket.equals("NORMAL")){
            return 8;
        }
        else if(typeTicket.equals("CRIANÇA")){
             return  4.50;
        }
        else if(typeTicket.equals("ESTUDANTE")){
            return 6;
        }
        else if(typeTicket.equals("SENIOR")){
            return 5;
        }
        else{
            System.out.println("Tipo de bilhete inválido");
            return 0;
        }
    }
    
    public String getTypeTicket(){
        return typeTicket;
    }
    
    public Seat getSeat(){
        return seat;
    }
    
    
}
