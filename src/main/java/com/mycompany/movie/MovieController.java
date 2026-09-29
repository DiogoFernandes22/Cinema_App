/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.movie;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author diogo
 */
@Controller
public class MovieController {

    private final MovieImportService movieImportService;
    private final MovieRepository movieRepository;

    public MovieController(
        MovieImportService movieImportService,
        MovieRepository movieRepository) {

    this.movieImportService = movieImportService;
    this.movieRepository = movieRepository;
}

    @GetMapping("/admin/movies/search")
public String searchMovie(
        @RequestParam String title,
        Model model) throws Exception {

    Movie movie = movieImportService.searchMovieFromTmdb(title);

    model.addAttribute("movie", movie);

    if (movieRepository.findByTmdbId(movie.getTmdbId()) != null) {
        model.addAttribute("message",
                "Este filme já existe no CineHub.");
    }

    return "movie-search";
}
    
    @GetMapping("/admin/movies/import")
    public String importMovie(
        @RequestParam String title,
        Model model) throws Exception {

    Movie movie = movieImportService.searchMovieFromTmdb(title);

    Movie savedMovie = movieImportService.saveMovie(movie);

    model.addAttribute("movie", savedMovie);

    return "movie-search";
}
    
    @GetMapping("/movies")
    public String listMovies(Model model) {

    model.addAttribute("movies", movieRepository.findAll());

        return "movies";
    }
}
