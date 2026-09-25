/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.movie;
import com.mycompany.cinema.TmdbService;
import org.springframework.stereotype.Service;
import java.util.List;
/**
 *
 * @author diogo
 */
@Service
public class MovieImportService {

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

    movie.setName(tmdbMovie.getTitle());
    movie.setDuration(tmdbMovie.getDuration());
    movie.setRate(tmdbMovie.getRate());
    
    if (tmdbMovie.getDirector() != null) {

    Director director =
            directorRepository.findByNameIgnoreCase(
                    tmdbMovie.getDirector()
            );

    if (director == null) {
        director = new Director();
        director.setName(tmdbMovie.getDirector());

        director = directorRepository.save(director);
    }

    movie.setDirector(director);
}
    
    if (tmdbMovie.getGenres() != null) {
    List<Genre> genres =
            genreService.findOrCreateAll(tmdbMovie.getGenres());

    movie.setGenres(genres);
}
    
    if (tmdbMovie.getStudio() != null) {

    Studio studio =
            studioRepository.findByNameIgnoreCase(
                    tmdbMovie.getStudio()
            );

    if (studio == null) {
        studio = new Studio();
        studio.setName(tmdbMovie.getStudio());

        studio = studioRepository.save(studio);
    }

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
    return movieRepository.save(movie);
}
}
