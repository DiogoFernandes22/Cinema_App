/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.movie;
import java.util.ArrayList; //para poder utilizar o ArrayList<String>
/**
 *
 * @author diogo
 */
public class Movie {
    // Declarar todos os atributos necessários para identificar um filme
    private String name;
    private String genre;
    private int duration;
    private int releaseDate;
    private String director;
    private ArrayList<String> cast; //lista dinâmica de objetos string, ou seja, o cast vai guardar vários objetos String, pode crescer ou diminuir de forma dinâmica
    private String studio;
    private double rate;
    //Construtor
    public Movie(String name, String genre, int duration, int releaseDate, String director, ArrayList<String> cast, String studio, double rate){
        
        this.name = name;
        this.genre = genre;
        this.duration = duration;
        this.releaseDate = releaseDate;
        this.director = director;
        this.cast = cast;
        this.studio = studio;
        this.rate = rate;
        }
    //Getters
    public String getName(){
        return name;
    }
    
    public String getGenre(){
        return genre;
    }
    
    public int getDuration(){
        return duration;
    }
    
    public int getReleaseDate(){
        return releaseDate;
    }
    
    public String getDirector(){
        return director;
    }
    
    public ArrayList<String> getCast(){
        return cast;
    }
    
    public String getStudio(){
        return studio;
    }
    
    public double getRate(){
        return rate;
    }
    
    // Adicionar ator ao elenco, fazer correções ao cast no sentido de adicionar atores 
    public void addActor(String actor) {
        cast.add(actor);
    }
    //Apresentação da informação
    @Override
    public String toString() {
    return "Movie: " + name +
           "\nGenre: " + genre +
           "\nDuration: " + duration + "min" +
           "\nRelease Year: " + releaseDate +
           "\nDirector: " + director +
           "\nCast: " + cast +
           "\nStudio: " + studio +
           "\nRate: " + rate;       
}
   
}
