/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.movie;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.Iterator;

/**
 *
 * @author diogo
 */
public class Reservation {
    
    private Session session; //Serve para a reservation guardar a session à qual pertence
    private User user; //Para a reserva ficar associada a uma pessoa
    private ArrayList<Seat> mySeats; //Lista de lugares que o utilizador escolheu para esta reserva
    private ArrayList<Ticket>myTickets;//Uma reserva pode ter vários lugares, logo pode ter vários bilhetes
    private boolean confirmed;
    private boolean paid;
    private String paymentMethod;
    
    public Reservation(Session session, User user){
        //Assim a reserva fica associada à sessão
        this.session = session;
        //A reserva fica associada ao user
        this.user = user;
        this.mySeats = new ArrayList<>();
        this.myTickets = new ArrayList<>();
        this.confirmed = false;
        this.paid = false;
        this.paymentMethod = null;
    }
    
    public void confirmReservation(){
    Scanner scanner = new Scanner(System.in);
    // confirmacao começa vazia para o while começar e vai guardar a resposta do utilizador
    String confirmacao = "";
    //Controla quando o processo de confirmação termina
    boolean finished = false;
    // O while continua enquanto a reserva não estiver terminada
    while (!finished) {
        for (Ticket ticket : myTickets) {
        System.out.println(
        ticket.getSeat() + " - " +
        ticket.getTypeTicket() + " - " +
        ticket.ticketPrice() + "€"
    );
}
        System.out.println("Preço total: " + totalPrice() + "€");
        System.out.println("Confirma a reserva? (sim/nao/alterar)");
        confirmacao = scanner.nextLine().toLowerCase(); // Converte para minúsculas para evitar erros
        
        if (confirmacao.equals("sim")) {
            confirmed = true;
            finished = true;
            System.out.println("Reserva confirmada com sucesso.");
        } 
        else if (confirmacao.equals("nao")) {
            confirmed = false;
            
            for(Seat seat : mySeats){
                session.cancelSeat(seat);
            }
            //Limpar os lugares guardados no mySeats
            mySeats.clear();
            
            finished = true;
            
            System.out.println("Reserva cancelada.");
            
        } 
        
        else if (confirmacao.equals("alterar")){
            // Assim o utilizador vai para o método do cancelamento, depois de cancelar volta para o confirmReservation porque ainda está dentro do while
            cancelSeat();
            chooseSeat();
            return;
            
        }
        else {
            System.out.println("Resposta inválida. Por favor, escreva 'sim', 'nao' ou 'alterar'.");
        }
    }
}
    
    public void chooseSeat(){
    // Scanner para ler o que o utilizador escreve
    Scanner scanner = new Scanner(System.in);
    
    String seatSelection = "";
    
    while (!seatSelection.equals("sair")){
        
    session.availableSeats(); //Apresentar os lugares que estão disponíveis
        
    System.out.println("Escolha um lugar: ");
    
    // Guarda o que o utilizador escreve
    seatSelection = scanner.nextLine();
    //Dar a opção ao utilizador de sair sem escolher lugar
    if(seatSelection.equals("sair")){
        break;
    }
    //Ainda não encontramos o lugar escolhido
    boolean found = false;
    // Percorrer todas as filas
    for (int i = 0; i < session.getRoom().GetSeats().length; i++){ 
        //Percorrer todos os lugares
        for(int j = 0; j < session.getRoom().GetSeats()[i].length; j++ ){
            //Obtem o seat que está na posição [i][j]
            Seat seat = session.getRoom().GetSeats()[i][j];
            //Compara o que o utilizador escreveu com o nome do lugar, se for igual envia o Seat para a Session para ser reservado
            if (seat.toString().equals(seatSelection)){
                found = true;
                //Tenta reservar o lugar na sessão, só acontece se o reserveSeat() do Session devolver true
                if(session.reserveSeat(seat)){
                mySeats.add(seat);//Adiciona o lugar à lista de lugares desta reserva
                Ticket ticket = chooseTicketType(seat);//Cria um bilhete para este lugar e guarda-o na variável ticket. Associar um tipo de bilhete ao lugar forneceido pelo chooseTicket
                myTickets.add(ticket);
                System.out.println("Lugar está reservado");
                }
            }
        }
        
    }
    if(!found && !seatSelection.equals("sair")){
        System.out.println("Lugar Inválido ou ocupado");
    }
    }
    
    confirmReservation();
}
    //O Seat é decidido pelo chooseSeat
    public Ticket chooseTicketType(Seat seat){
    
        Scanner scanner = new Scanner(System.in);
        String typeTicket = "";
        
        while(!typeTicket.equals("NORMAL")
            && !typeTicket.equals("CRIANÇA")
            && !typeTicket.equals("ESTUDANTE")
            && !typeTicket.equals("SENIOR")){

    System.out.println("Escolha o tipo de bilhete:");
    System.out.println("NORMAL");
    System.out.println("CRIANÇA");
    System.out.println("ESTUDANTE");
    System.out.println("SENIOR");
    
    typeTicket = scanner.nextLine().toUpperCase();
    
    if(!typeTicket.equals("NORMAL") && !typeTicket.equals("CRIANÇA") && !typeTicket.equals("ESTUDANTE") && !typeTicket.equals("SENIOR")){
        
        System.out.println("Tipo de bilhete inválido");
    }
        }
    //Contem o tipo escolhido pelo utilizador assim como o seat/lugar que foi passado
    return new Ticket(typeTicket, seat);
    }
    
    public void cancelSeat(){
        
        Scanner scanner = new Scanner(System.in);
        //Vai receber o input do utilizador 
        String seatSelection = "";
        // O while está sempre true, não termina por si só. Apenas quando chega ao break
        while(true){
            
        System.out.println("Escolha o lugar que quer cancelar: ");
        seatSelection = scanner.nextLine();
        //Começa como null porque ainda não temos lugar para cancelar, depois vai ser substituído pelo lugar selecionado
        Seat seatToCancel = null;
        
        //Para cada seat que existe dentro de mySeats, guarda esse lugar na variável seat, ou seja, seat = A5, seat = A6, até chegar ao fim do mySeats. Assim compara um a um
        for(Seat seat : mySeats){
            //Analisar se o lugar que estamos a analisar é o mesmo que o utilizador escreveu, se o lugar corresponder guarda-o no seatCancel
            if(seat.toString().equals(seatSelection)){
                seatToCancel = seat;
            }
        }
        //O seatTocnacel deixa de ser nulo quando encontramos o lugar escolhido 
        if(seatToCancel != null){
            //Remove o lugar ocupado na sessão 
            session.cancelSeat(seatToCancel);
            //Cria um Iterator para percorrer todos os bilhetes da lista myTickets. Iterator porque queremos percorrer myTickets e remover um Ticket ao mesmo tempo. Ao percorrer a lista enquanto a alteramos com um for podia causar um erro
            Iterator<Ticket> iterator = myTickets.iterator();
            // Continua enquanto ainda existirem bilhetes por analisar, enquanto tiver algo a seguir 
            while(iterator.hasNext()){
                //Obtém o próximo bilhete da lista
                Ticket ticket = iterator.next();
                //Verifica se o lugar deste bilhete é o mesmo que queremos cancelar
                if(ticket.getSeat().equals(seatToCancel)){
                    //Remove o bilhete correspondente da lista myTickets
                    iterator.remove();
                    //Como já encontrámos o bilhete, terminamos o ciclo
                    break;
                }
            }
            
            //Remove o lugar guardado no seatToCancel do mySeats
            mySeats.remove(seatToCancel);
            System.out.println("Lugar cancelado");
            //Lugar encontrado e cancelado, então podemos sair do ciclo 
            break;
        } 
        //Se o seatToCancel não ficar nulo ele volta ao início do ciclo
        else {
            System.out.println("Esse lugar não pertence à sua reserva");
        }
    }    
}
    public double totalPrice(){
        double total = 0;
        
        for(Ticket ticket : myTickets){
            //O tipo do bilhete fica guardado em typeTicket e depois chamo o ticket.ticketPrice que consulta o typeTicket e determina o preço
            total +=ticket.ticketPrice();
        }
        return total;
    }
    public User getUser(){
        return user;
    }
    //Poder consultar os bilhetes que pertencem àquela reserva
    public ArrayList<Ticket> getMyTickets(){
        return myTickets;
    }
    //Para podermos criar uma reserva a partir do site sem utilizar o scanner
    public void addTicket(String typeTicket, Seat seat){
        
        Ticket ticket = new Ticket(typeTicket, seat);
        
        myTickets.add(ticket);
        mySeats.add(seat);
        
    }
    
    public void setConfirmed(boolean confirmed){
        this.confirmed = confirmed;
    }
    
    public boolean isConfirmed(){
        return confirmed;
    }
    
    public boolean isPaid(){
        return paid;
    }
    
    public void setPaid(boolean paid){
        this.paid = paid;
    }
    
    public String getPaymentMethod(){
        return paymentMethod;
    }
    
    public void setPaymentMethod(String paymentMethod){
        this.paymentMethod = paymentMethod;
    }
    
    //Pode ser útil, mas pode não ser necessário
    public Session getSession(){
        return session;
    }
    // Pode não ser necessário
    public ArrayList<Seat> getMySeats(){
        return mySeats;
    }
    
    
}
