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

    String movieId = primeiroResultado.get("id").asText();

    String detailsUrl = "https://api.themoviedb.org/3/movie/" + movieId;

    HttpRequest detailsRequest = HttpRequest.newBuilder()
        .uri(URI.create(detailsUrl))
        .header("Authorization", "Bearer " + token)
        .header("accept", "application/json")
        .GET()
        .build();

    HttpResponse<String> detailsResponse =
        client.send(detailsRequest, HttpResponse.BodyHandlers.ofString());

    JsonNode detailsJson = mapper.readTree(detailsResponse.body());

    movie.setDuration(detailsJson.get("runtime").asInt());
    
    StringBuilder genres = new StringBuilder();

    for (JsonNode genre : detailsJson.get("genres")) {
    if (genres.length() > 0) {
        genres.append(", ");
    }
    genres.append(genre.get("name").asText());
    }

    movie.setGenres(genres.toString());
    
    String creditsUrl = "https://api.themoviedb.org/3/movie/" + movieId + "/credits";

    HttpRequest creditsRequest = HttpRequest.newBuilder()
        .uri(URI.create(creditsUrl))
        .header("Authorization", "Bearer " + token)
        .header("accept", "application/json")
        .GET()
        .build();

    HttpResponse<String> creditsResponse =
        client.send(creditsRequest, HttpResponse.BodyHandlers.ofString());

    JsonNode creditsJson = mapper.readTree(creditsResponse.body());

    for (JsonNode crewMember : creditsJson.get("crew")) {
        if ("Director".equals(crewMember.get("job").asText())) {
        movie.setDirector(crewMember.get("name").asText());
        break;
        }
    }

    return movie;
}
}