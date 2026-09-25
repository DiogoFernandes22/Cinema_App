/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.movie;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.ArrayList;
/**
 *
 * @author diogo
 */
@Service
public class GenreService {

    private final GenreRepository genreRepository;

    public GenreService(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    public Genre findOrCreate(String name) {

        String normalizedName = name.trim();

        Genre existingGenre =
                genreRepository.findByNameIgnoreCase(normalizedName);

        if (existingGenre != null) {
            return existingGenre;
        }

        Genre newGenre = new Genre();
        newGenre.setName(normalizedName);

        return genreRepository.save(newGenre);
    }
    
    public List<Genre> findOrCreateAll(List<String> genreNames) {

    List<Genre> genres = new ArrayList<>();

    for (String genreName : genreNames) {
        Genre genre = findOrCreate(genreName);
        genres.add(genre);
    }

    return genres;
}
}