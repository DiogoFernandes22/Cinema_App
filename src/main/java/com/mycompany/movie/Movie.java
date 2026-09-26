package com.mycompany.movie;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.JoinTable;


@Entity
@Table(name = "movies")
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    private int duration;

    @Column(name = "release_date")
    private int releaseDate;

    private double rate;
    
    @Column(name = "tmdb_id")
    private Integer tmdbId;

    @ManyToOne
    @JoinColumn(name = "director_id")
    private Director director;

    @ManyToOne
    @JoinColumn(name = "studio_id")
    private Studio studio;
    
    @ManyToMany
    @JoinTable(
    name = "movie_genres",
    joinColumns = @JoinColumn(name = "movie_id"),
    inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    private List<Genre> genres = new ArrayList<>();

    public Movie() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(int releaseDate) {
        this.releaseDate = releaseDate;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public Director getDirector() {
        return director;
    }
    
    public Integer getTmdbId() {
    return tmdbId;
    }

    public void setTmdbId(Integer tmdbId) {
    this.tmdbId = tmdbId;
    }

    public void setDirector(Director director) {
        this.director = director;
    }

    public Studio getStudio() {
        return studio;
    }

    public void setStudio(Studio studio) {
        this.studio = studio;
    }
    
    public List<Genre> getGenres() {
    return genres;
    }

    public void setGenres(List<Genre> genres) {
    this.genres = genres;
    }

    @Override
    public String toString() {
        return "Movie: " + name +
               "\nDuration: " + duration + "min" +
               "\nRelease Year: " + releaseDate +
               "\nDirector: " + (director != null ? director.getName() : "N/A") +
               "\nStudio: " + (studio != null ? studio.getName() : "N/A") +
               "\nRate: " + rate;
    }
}