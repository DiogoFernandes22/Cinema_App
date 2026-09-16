/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.cinema;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.boot.persistence.autoconfigure.EntityScan;

/**
 *
 * @author diogo
 */
//procurar Controllers, Repositories, Entities, Components, etc. dentro de com.mycompany e dos seus subpacotes
@SpringBootApplication(scanBasePackages = "com.mycompany")
@EnableJpaRepositories(basePackages = "com.mycompany.movie")
@EntityScan(basePackages = "com.mycompany.movie")
public class CinemaApplication {
    
     public static void main(String[] args) {
        SpringApplication.run(CinemaApplication.class, args);
    }
    
}
