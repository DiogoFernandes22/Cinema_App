/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.movie;
// pacote de ferramentas disponibilizadas pelo JPA para fazer mapeamento enrte java e a base de dados. Sem ele o java não sabe o que Entity significa
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
/**
 *
 * @author diogo
 */
// Entity é uma annotation. Estamos a dizer ao JPA: A classe director representa uma entidade que vai ser persistida na base de dados 
@Entity
// Está a dizer que a classe Director está associada à tabela directors 
@Table(name = "directors")
public class Director {
    // este atributo é o identificador único desta entidade
    @Id
    // O atributo id do Java corresponde à coluna director_id do MySQL
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "director_id")
    private int id;
    
    private String name;
    
    public int getId(){
    return id;
}
    
    public void setId(int id){
        this.id = id;
    }
    
    public String getName(){
        return name;
    }
    
    public void setName(String name){
        this.name = name;
    }
    
public Director(){
    
}
    
}





