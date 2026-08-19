/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.movie;

/**
 *
 * @author diogo
 */
public class Room {
    //Array que contem as colunas e linhas de lugares da sala
    private Seat[][] seats;
    // Tipo de sala, Normal ou IMAX
    private String type;
    
    public Room(String type){
        this.type = type;
        //Caso seja normal 
        if(type.equals("NORMAL")){
           
             seats = new Seat[10][12];
        }
        //Caso seja IMAX
        else if(type.equals("IMAX")){
            seats = new Seat[12][15];
        }
        //Se não for um tipo válido
        else{
            
             System.out.println("Tipo de sala inválido, selecione NORMAL ou IMAX");
        }
        
       //Método para criar os lugares
        createSeats();
        
    }
    
    public void createSeats(){ // A matriz está vazia e com os dois for percorre-se cada posição da matriz calculando a letra da fila e o número do lugar
    
        for (int row = 0; row < seats.length; row++){ /*Percorer todas as filas da matriz*/
            
            for(int column = 0; column < seats[row].length; column++){ /*Percorer todos os lugares/colunas de cada fila*/
                
                char rowLetter = (char) ('A' + row); /*Converte o número da fila numa letra: 0 -> A, 1 -> B, etc. O Java permite fazer contas com char, então: A + 0 = A, A + 1 = B, A + 2 = C. Transforma o número da fila (i) na letra correspondente. Mas é sempre maiúscula*/
                int number = column + 1; /*Define o número do lugar: 0 -> 1, 1 -> 2, etc.*/
                
                Seat seat = new Seat(rowLetter, number); //Cria o objeto seat
                
                seats[row][column] = seat; /*Guarda esse Seat na matriz, não guarda String A1, guarda objetos seats[0][0] -> objeto Seat ('A', 1)*/
                
            }
            
        }
}
    
    public void showSeats() {

    for (int i = 0; i < seats.length; i++) {

        for (int j = 0; j < seats[i].length; j++) {

            System.out.print(seats[i][j] + " ");
        }

        System.out.println();
    }
}
    
    public Seat[][] GetSeats(){ //Para conseguir aceder aos seats no Session
        
    return seats;
}
    
    public String getType(){
        return type;
    }
    
}
