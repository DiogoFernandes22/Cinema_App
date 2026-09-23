package com.mycompany.movie;

import org.springframework.data.jpa.repository.JpaRepository;


public interface StudioRepository extends JpaRepository<Studio, Integer> {
    
}