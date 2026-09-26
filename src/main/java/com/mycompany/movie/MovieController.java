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

    public MovieController(MovieImportService movieImportService) {
        this.movieImportService = movieImportService;
    }

    @GetMapping("/admin/movies/search")
    public String searchMovie(
            @RequestParam String title,
            Model model) throws Exception {

        Movie movie = movieImportService.searchMovieFromTmdb(title);

        model.addAttribute("movie", movie);

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
}
