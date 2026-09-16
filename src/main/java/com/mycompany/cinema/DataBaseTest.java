package com.mycompany.cinema;

import javax.sql.DataSource;
import java.sql.Connection;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import com.mycompany.movie.DirectorRepository;
import com.mycompany.movie.Director;

@Component
public class DataBaseTest implements CommandLineRunner {

    private final DataSource dataSource;
    private final DirectorRepository directorRepository;

    public DataBaseTest(DataSource dataSource, DirectorRepository directorRepository) {
        this.dataSource = dataSource;
        this.directorRepository = directorRepository;
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
    }
}