/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.movie;
import java.time.LocalTime;
import java.util.ArrayList;
/**
 *
 * @author diogo
 */
public class Session {
    
    private Movie movie;
    private LocalTime time;
    private Room room;
    private ArrayList<Seat> occupiedSeats ;
    
    public Session(Movie movie, LocalTime time, Room room){
        
        this.movie = movie;
        this.time = time;
        this.room = room;
        
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
}
