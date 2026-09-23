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
import com.mycompany.cinema.TmdbService;

@Component
public class DataBaseTest implements CommandLineRunner {

    private final DataSource dataSource;
    private final DirectorRepository directorRepository;
    private final StudioRepository studioRepository;
    private final MovieRepository movieRepository;
    private final TmdbService tmdbService;

public DataBaseTest(
        DataSource dataSource,
        DirectorRepository directorRepository,
        StudioRepository studioRepository,
        MovieRepository movieRepository,
        TmdbService tmdbService) {

    this.dataSource = dataSource;
    this.directorRepository = directorRepository;
    this.studioRepository = studioRepository;
    this.movieRepository = movieRepository;
    this.tmdbService = tmdbService;
}

    @Override
    public void run(String... args) throws Exception {

        try (Connection connection = dataSource.getConnection()) {

            System.out.println("=================================");
            System.out.println("LIGAÇÃO AO MYSQL: OK");
            System.out.println("Base de dados: " 
                    + connection.getCatalog());
            System.out.println("=================================");
            System.out.println("=================================");
            System.out.println("DIRETORES NA BD:");

            for (Director director : directorRepository.findAll()) {
                System.out.println(director.getName());
            }

            System.out.println("=================================");
        }
        System.out.println("FILMES NA BD:");

        for (Movie movie : movieRepository.findAll()) {
            System.out.println(movie.getName());
        }

        System.out.println("=================================");
        
        System.out.println("=================================");
        System.out.println("TESTE TMDB:");

        TmdbMovie resultado = tmdbService.searchMovie("Avatar");

        System.out.println("ID: " + resultado.getId());
        System.out.println("Título: " + resultado.getTitle());
        System.out.println("Data de lançamento: " + resultado.getReleaseDate());
        System.out.println("Nota: " + resultado.getRate());
        System.out.println("=================================");
        }
}