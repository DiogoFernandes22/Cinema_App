/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.movie;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author diogo
 */
// herdar do JpaRepository que já tem métodos preparados como findAll(), findById(), save(), delete()
public interface DirectorRepository extends JpaRepository <Director, Integer> {
    
}
