/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.movie;
import com.mycompany.cinema.TmdbService;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.ArrayList;
/**
 *
 * @author diogo
 */
@Service
public class MovieImportService {

    private boolean movieAlreadyExists;
    private final MovieRepository movieRepository;
    private final DirectorRepository directorRepository;
    private final StudioRepository studioRepository;
    private final GenreService genreService;
    private final TmdbService tmdbService;

    public MovieImportService(
            MovieRepository movieRepository,
            DirectorRepository directorRepository,
            StudioRepository studioRepository,
            GenreService genreService,
            TmdbService tmdbService) {

        this.movieRepository = movieRepository;
        this.directorRepository = directorRepository;
        this.studioRepository = studioRepository;
        this.genreService = genreService;
        this.tmdbService = tmdbService;
    }
    
    public Movie searchMovieFromTmdb(String title) throws Exception {

    var tmdbMovie = tmdbService.searchMovie(title);

    if (tmdbMovie == null) {
        return null;
    }

    Movie movie = new Movie();

    movie.setTmdbId(tmdbMovie.getId());
    movie.setName(tmdbMovie.getTitle());
    movie.setDuration(tmdbMovie.getDuration());
    movie.setRate(tmdbMovie.getRate());
    
    if (tmdbMovie.getDirector() != null) {

    Director director = new Director();
    director.setName(tmdbMovie.getDirector());

    movie.setDirector(director);
}
    
    if (tmdbMovie.getGenres() != null) {

    List<Genre> genres = new ArrayList<>();

    for (String genreName : tmdbMovie.getGenres()) {
        Genre genre = new Genre();
        genre.setName(genreName);
        genres.add(genre);
    }

    movie.setGenres(genres);
}
    
    if (tmdbMovie.getStudio() != null) {

    Studio studio = new Studio();
    studio.setName(tmdbMovie.getStudio());

    movie.setStudio(studio);
}

    String releaseDate = tmdbMovie.getReleaseDate();

    if (releaseDate != null && releaseDate.length() >= 4) {
        movie.setReleaseDate(
                Integer.parseInt(releaseDate.substring(0, 4))
        );
    }

    return movie;
}
    
    public Movie saveMovie(Movie movie) {
        
        movieAlreadyExists = false;
        
        if (movie.getTmdbId() != null) {

            Movie existingMovie =
            movieRepository.findByTmdbId(movie.getTmdbId());

        if (existingMovie != null) {
            movieAlreadyExists = true;
            return existingMovie;
        }
    }

    if (movie.getDirector() != null) {

        Director director =
                directorRepository.findByNameIgnoreCase(
                        movie.getDirector().getName()
                );

        if (director == null) {
            director = directorRepository.save(movie.getDirector());
        }

        movie.setDirector(director);
    }

    if (movie.getStudio() != null) {

        Studio studio =
                studioRepository.findByNameIgnoreCase(
                        movie.getStudio().getName()
                );

        if (studio == null) {
            studio = studioRepository.save(movie.getStudio());
        }

        movie.setStudio(studio);
    }
    
    if (movie.getGenres() != null) {

    List<String> genreNames = new ArrayList<>();

    for (Genre genre : movie.getGenres()) {
        genreNames.add(genre.getName());
    }

    List<Genre> genres =
            genreService.findOrCreateAll(genreNames);

    movie.setGenres(genres);
}

    return movieRepository.save(movie);
}
    public boolean isMovieAlreadyExists() {
    return movieAlreadyExists;
}
}
