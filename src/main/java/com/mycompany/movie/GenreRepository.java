/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.movie;
import org.springframework.data.jpa.repository.JpaRepository;
/**
 *
 * @author diogo
 */
public interface GenreRepository extends JpaRepository<Genre, Integer> {
    
    Genre findByNameIgnoreCase(String name);
}
