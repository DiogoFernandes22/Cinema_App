package com.mycompany.cinema;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.springframework.beans.factory.annotation.Value;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.stereotype.Service;

@Service
public class TmdbService {
    
    @Value("${tmdb.api.token}")
    private String token;

    public TmdbMovie searchMovie(String query) throws Exception {

    String url = "https://api.themoviedb.org/3/search/movie?query="
            + URI.create(query).toString();

    HttpClient client = HttpClient.newHttpClient();

    HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(url))
            .header("Authorization", "Bearer " + token)
            .header("accept", "application/json")
            .GET()
            .build();

    HttpResponse<String> response =
            client.send(request, HttpResponse.BodyHandlers.ofString());

    ObjectMapper mapper = new ObjectMapper();

    JsonNode json = mapper.readTree(response.body());

    JsonNode primeiroResultado = json.get("results").get(0);

    TmdbMovie movie = new TmdbMovie();

    movie.setId(primeiroResultado.get("id").asInt());
    movie.setTitle(primeiroResultado.get("title").asText());
    movie.setReleaseDate(primeiroResultado.get("release_date").asText());
    movie.setRate(primeiroResultado.get("vote_average").asDouble());

    return movie;
}
}