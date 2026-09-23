package com.mycompany.cinema;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;


public class TmdbTest {

    public static void main(String[] args) throws Exception {

    String token = "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiIyMGU1ZGJkZWFhZjlkYmU2ZDdmMmI5NTNlMWNjNTUwNyIsIm5iZiI6MTc5MDEwODI2OC42NTI5OTk5LCJzdWIiOiI2YWIyZTI2YzFjNGVkZGUwY2VlMWYxM2UiLCJzY29wZXMiOlsiYXBpX3JlYWQiXSwidmVyc2lvbiI6MX0.Vjth8G5Ii0qSAd8d1s69EueCzzkFwhKDjmzM8r_JOMg";

    String movieId = "19995";

    String url = "https://api.themoviedb.org/3/movie/" + movieId;

    HttpClient client = HttpClient.newHttpClient();

    HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(url))
            .header("Authorization", "Bearer " + token)
            .header("accept", "application/json")
            .GET()
            .build();

    HttpResponse<String> response =
            client.send(request, HttpResponse.BodyHandlers.ofString());

    System.out.println("Status: " + response.statusCode());

    ObjectMapper mapper = new ObjectMapper();

    JsonNode json = mapper.readTree(response.body());

    System.out.println("ID do filme: " + json.get("id").asInt());
    System.out.println("Título: " + json.get("title").asText());
    System.out.println("Duração: " + json.get("runtime").asInt());
    System.out.println("Nota: " + json.get("vote_average").asDouble());
    System.out.println("Data de lançamento: " + json.get("release_date").asText());
   
   System.out.println("GÉNEROS:");

    for (JsonNode genero : json.get("genres")) {

        String nome = genero.get("name").asText();

        System.out.println("- " + nome);
        }
    
    System.out.println("ESTÚDIOS:");

        for (JsonNode estudio : json.get("production_companies")) {

            String nome = estudio.get("name").asText();

            System.out.println("- " + nome);
        }
        
    String creditsUrl = "https://api.themoviedb.org/3/movie/"
        + movieId + "/credits";

    HttpRequest creditsRequest = HttpRequest.newBuilder()
        .uri(URI.create(creditsUrl))
        .header("Authorization", "Bearer " + token)
        .header("accept", "application/json")
        .GET()
        .build();

    HttpResponse<String> creditsResponse =
        client.send(creditsRequest, HttpResponse.BodyHandlers.ofString());

    JsonNode creditsJson = mapper.readTree(creditsResponse.body());

    System.out.println("REALIZADOR:");

    for (JsonNode pessoa : creditsJson.get("crew")) {

        String trabalho = pessoa.get("job").asText();

            if (trabalho.equals("Director")) {

                System.out.println("- " + pessoa.get("name").asText());
            }
        }
}
}

/* eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiIyMGU1ZGJkZWFhZjlkYmU2ZDdmMmI5NTNlMWNjNTUwNyIsIm5iZiI6MTc5MDEwODI2OC42NTI5OTk5LCJzdWIiOiI2YWIyZTI2YzFjNGVkZGUwY2VlMWYxM2UiLCJzY29wZXMiOlsiYXBpX3JlYWQiXSwidmVyc2lvbiI6MX0.Vjth8G5Ii0qSAd8d1s69EueCzzkFwhKDjmzM8r_JOMg
*/