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
    private ArrayList<Seat> occupiedSeats ;
    private double ticketPrice;
    
    public Session() {
    }
    
    public Session(Movie movie, LocalTime time, Room room, double ticketPrice){
        
        this.movie = movie;
        this.time = time;
        this.room = room;
        this.ticketPrice = ticketPrice;
        
        occupiedSeats = new ArrayList<>();
        
    }
    
    public void availableSeats(){
        for (int i = 0; i < room.GetSeats().length; i++){ //Aqui só estou a percorrer as filas Preciso de percorrer os seats da sala e verificar aqueles que não estão no occupiedSeats. Não dá para comparar um int com um array, por isso tem de ser room.GetSeats().length 
            
            for(int j = 0; j < room.GetSeats()[i].length; j++){ //Aqui estou a percorrer cada lugar da fila, [i] para percorrer todas as filas, por exemplo, se fosse [0] percorria apenas a fila A 
                
                //Igualar a escolha do utilizador ao lugar ocupado no Array
                Seat seat = room.GetSeats()[i][j];
           
                if (!occupiedSeats.contains(seat)){
                    
                    System.out.println(seat);
                
            }
        }
    }
}
    //Verificar se o lugar está ocupado, se não colocá-lo no occupiedSeats
    public boolean reserveSeat(Seat seat){
        if(occupiedSeats.contains(seat)){
            System.out.println("Lugar já ocupado, por favor selecione outro");
            //Falso não se pode reservar lugar
            return false;
        }
        else{
            occupiedSeats.add(seat);
            return true;
        }
    }
    
    public void cancelSeat(Seat seat){
        
        occupiedSeats.remove(seat);
    }    
    public Room getRoom(){
        return room;
    }
    
    public Movie getMovie(){
        return movie;
    }
    
    public LocalTime getTime(){
        return time;
    }
    
    public double getTicketPrice(){
    return ticketPrice;
}
}
