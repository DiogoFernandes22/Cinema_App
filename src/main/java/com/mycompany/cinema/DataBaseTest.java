package com.mycompany.cinema;

import javax.sql.DataSource;
import java.sql.Connection;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.mycompany.movie.DirectorRepository;
import com.mycompany.movie.Director;
import com.mycompany.movie.StudioRepository;
import com.mycompany.movie.MovieRepository;
import com.mycompany.movie.Movie;
import com.mycompany.movie.GenreRepository;
import com.mycompany.movie.Genre;
import com.mycompany.cinema.TmdbService;
import com.mycompany.movie.GenreService;
import org.springframework.beans.factory.annotation.Autowired;
import com.mycompany.movie.MovieImportService;

@Component
public class DataBaseTest implements CommandLineRunner {

    private final DataSource dataSource;
    private final DirectorRepository directorRepository;
    private final StudioRepository studioRepository;
    private final MovieRepository movieRepository;
    private final TmdbService tmdbService;
    private final GenreRepository genreRepository;
    private final GenreService genreService;
    @Autowired
    private MovieImportService movieImportService;

    public DataBaseTest(
            DataSource dataSource,
            DirectorRepository directorRepository,
            StudioRepository studioRepository,
            MovieRepository movieRepository,
            GenreRepository genreRepository,
            GenreService genreService,
            TmdbService tmdbService) {

        this.dataSource = dataSource;
        this.directorRepository = directorRepository;
        this.studioRepository = studioRepository;
        this.movieRepository = movieRepository;
        this.genreRepository = genreRepository;
        this.tmdbService = tmdbService;
        this.genreService = genreService;
    }

    @Override
    public void run(String... args) throws Exception {

        try (Connection connection = dataSource.getConnection()) {

            System.out.println("=================================");
            System.out.println("LIGAÇÃO AO MYSQL: OK");
            System.out.println("Base de dados: "
                    + connection.getCatalog());
            System.out.println("=================================");

            System.out.println("DIRETORES NA BD:");

            for (Director director : directorRepository.findAll()) {
                System.out.println(director.getName());
            }

            System.out.println("=================================");
        }

        System.out.println("FILMES NA BD:");

        for (Movie movie : movieRepository.findAll()) {
            System.out.println("Filme: " + movie.getName());

            if (movie.getStudio() != null) {
                System.out.println("Estúdio: "
                        + movie.getStudio().getName());
            } else {
                System.out.println("Estúdio: N/A");
            }

            if (movie.getDirector() != null) {
                System.out.println("Realizador: "
                        + movie.getDirector().getName());
            } else {
                System.out.println("Realizador: N/A");
            }
        }

        System.out.println("=================================");

        System.out.println("TESTE DE GÉNERO:");
        

        Genre genre = genreRepository.findByNameIgnoreCase("terror");

        if (genre == null) {
            System.out.println("Género não encontrado.");
        } else {
            System.out.println("Género encontrado: " + genre.getName());
        }
        
        System.out.println("TESTE DE CRIAÇÃO/REUTILIZAÇÃO:");

        Genre testGenre = genreService.findOrCreate("SCIENCE FICTION");

        System.out.println("Género devolvido: " + testGenre.getName());
        
        Genre newTestGenre = genreService.findOrCreate("Action");

        System.out.println("Novo género: " + newTestGenre.getName());

        System.out.println("=================================");
        
        System.out.println("=================================");
        System.out.println("TESTE DE IMPORTAÇÃO DO TMDB:");

        Movie movieFromTmdb =
            movieImportService.searchMovieFromTmdb("Avatar");
        Movie savedMovie =
        movieImportService.saveMovie(movieFromTmdb);

        System.out.println("FILME GUARDADO:");
        System.out.println("ID: " + savedMovie.getId());
        System.out.println("Nome: " + savedMovie.getName());

        System.out.println("Filme: " + movieFromTmdb.getName());
        System.out.println("Duração: " + movieFromTmdb.getDuration());
        System.out.println("Ano: " + movieFromTmdb.getReleaseDate());
        System.out.println("Rating: " + movieFromTmdb.getRate());

        System.out.println("Realizador: "
            + movieFromTmdb.getDirector().getName());

        System.out.println("Estúdio: "
            + movieFromTmdb.getStudio().getName());

        System.out.println("Géneros:");

        for (Genre movieGenre : movieFromTmdb.getGenres()) {
            System.out.println("- " + movieGenre.getName());
        }
    }
}