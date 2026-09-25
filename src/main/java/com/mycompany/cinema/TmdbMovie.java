package com.mycompany.cinema;
import java.util.List;

public class TmdbMovie {

    private int id;
    private String title;
    private int duration;
    private String releaseDate;
    private double rate;
    private List<String> genres;
    private String director;
    private String studio;

    public TmdbMovie() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }
    
    public List<String> getGenres() {
    return genres;
    }

    public void setGenres(List<String> genres) {
    this.genres = genres;
    }
    
    public String getDirector(){
        return director;
    }
    
    public void setDirector(String director){
        this.director = director;
    }
    public String getStudio() {
    return studio;
}

    public void setStudio(String studio) {
    this.studio = studio;
}
    
}