package com.mycompany.cinema;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.GridLayout;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class StaffApplication {
    
    private static String getRoomsFromBackend() throws Exception {

    HttpClient client = HttpClient.newHttpClient();

    HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create("http://localhost:8080/api/admin/sessions/rooms"))
            .GET()
            .build();

    HttpResponse<String> response =
            client.send(request, HttpResponse.BodyHandlers.ofString());

    return response.body();
}
    
    private static String getMoviesFromBackend() throws Exception {

    HttpClient client = HttpClient.newHttpClient();

    HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create("http://localhost:8080/api/admin/sessions/movies"))
            .GET()
            .build();

    HttpResponse<String> response =
            client.send(request, HttpResponse.BodyHandlers.ofString());

    return response.body();
}

    public static void main(String[] args) {
        
        try {
            String rooms = getRoomsFromBackend();
            System.out.println("Salas recebidas do backend:");
            System.out.println(rooms);
            } catch (Exception e) {
                e.printStackTrace();
            }

        JFrame frame = new JFrame("CineHub - Área de Funcionários");

        frame.setSize(500, 350);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));

        JLabel movieLabel = new JLabel("Filme:");

        JComboBox<String> movieBox = new JComboBox<>();

        try {
            String moviesJson = getMoviesFromBackend();

            ObjectMapper mapper = new ObjectMapper();
            JsonNode movies = mapper.readTree(moviesJson);

            for (JsonNode movie : movies) {
                String name = movie.get("name").asText();
                movieBox.addItem(name);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        JLabel roomLabel = new JLabel("Sala:");

        JComboBox<String> roomBox = new JComboBox<>();

        try {
            String roomsJson = getRoomsFromBackend();

            ObjectMapper mapper = new ObjectMapper();
            JsonNode rooms = mapper.readTree(roomsJson);

            for (JsonNode room : rooms) {
                String name = room.get("name").asText();
                roomBox.addItem(name);
            }

            } catch (Exception e) {
                e.printStackTrace();
            }

        JLabel timeLabel = new JLabel("Hora:");
        JTextField timeField = new JTextField("20:00");

        JLabel priceLabel = new JLabel("Tipo de preço:");
        JComboBox<String> priceBox =
                new JComboBox<>(new String[]{"NORMAL", "IMAX"});

        JButton createButton = new JButton("Criar sessão");

        panel.add(movieLabel);
        panel.add(movieBox);

        panel.add(roomLabel);
        panel.add(roomBox);

        panel.add(timeLabel);
        panel.add(timeField);

        panel.add(priceLabel);
        panel.add(priceBox);

        panel.add(new JLabel());
        panel.add(createButton);

        frame.add(panel);

        frame.setVisible(true);
    }
}