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
                .uri(URI.create(
                        "http://localhost:8080/api/admin/sessions/rooms"
                ))
                .GET()
                .build();

        HttpResponse<String> response =
                client.send(
                        request,
                        HttpResponse.BodyHandlers.ofString()
                );

        return response.body();
    }

    private static String getMoviesFromBackend() throws Exception {

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(
                        "http://localhost:8080/api/admin/sessions/movies"
                ))
                .GET()
                .build();

        HttpResponse<String> response =
                client.send(
                        request,
                        HttpResponse.BodyHandlers.ofString()
                );

        return response.body();
    }

    private static String getSessionsFromBackend() throws Exception {

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(
                        "http://localhost:8080/api/admin/sessions"
                ))
                .GET()
                .build();

        HttpResponse<String> response =
                client.send(
                        request,
                        HttpResponse.BodyHandlers.ofString()
                );

        return response.body();
    }

    public static void main(String[] args) {

        JFrame frame =
                new JFrame("CineHub - Área de Funcionários");

        frame.setSize(500, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        JPanel panel =
                new JPanel(new GridLayout(4, 2, 10, 10));

        JLabel movieLabel =
                new JLabel("Filme:");

        JComboBox<MovieItem> movieBox =
                new JComboBox<>();

        try {

            String moviesJson =
                    getMoviesFromBackend();

            ObjectMapper mapper =
                    new ObjectMapper();

            JsonNode movies =
                    mapper.readTree(moviesJson);

            for (JsonNode movie : movies) {

                int id =
                        movie.get("id").asInt();

                String name =
                        movie.get("name").asText();

                movieBox.addItem(
                        new MovieItem(id, name)
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        JLabel roomLabel =
                new JLabel("Sala:");

        JComboBox<RoomItem> roomBox =
                new JComboBox<>();

        try {

            String roomsJson =
                    getRoomsFromBackend();

            ObjectMapper mapper =
                    new ObjectMapper();

            JsonNode rooms =
                    mapper.readTree(roomsJson);

            for (JsonNode room : rooms) {

                int id =
                        room.get("id").asInt();

                String name =
                        room.get("name").asText();

                String type =
                        room.get("type").asText();

                roomBox.addItem(
                        new RoomItem(
                                id,
                                name,
                                type
                        )
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        
        JLabel dateLabel =
        new JLabel("Data:");

        JTextField dateField =
        new JTextField("2026-10-10");

        JLabel timeLabel =
                new JLabel("Hora:");

        JTextField timeField =
                new JTextField("20:00");

        JButton createButton =
                new JButton("Criar sessão");

        createButton.addActionListener(e -> {

            MovieItem selectedMovie =
                    (MovieItem) movieBox.getSelectedItem();

            RoomItem selectedRoom =
                    (RoomItem) roomBox.getSelectedItem();

            String time =
                    timeField.getText();
            
            String date =
                    dateField.getText();

            if (selectedMovie == null
                    || selectedRoom == null) {

                return;
            }

            try {

                HttpClient client =
                        HttpClient.newHttpClient();

                String url =
                        "http://localhost:8080/api/admin/sessions"
                        + "?movieId="
                        + selectedMovie.getId()
                        + "&roomId="
                        + selectedRoom.getId()
                        + "&date="
                        + date
                        + "&time="
                        + time;

                HttpRequest request =
                        HttpRequest.newBuilder()
                                .uri(URI.create(url))
                                .POST(
                                    HttpRequest.BodyPublishers
                                            .noBody()
                                )
                                .build();

                HttpResponse<String> response =
                        client.send(
                                request,
                                HttpResponse.BodyHandlers
                                        .ofString()
                        );

                System.out.println(
                        "Resposta do backend:"
                );

                System.out.println(
                        response.statusCode()
                );

                System.out.println(
                        response.body()
                );

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        panel.add(movieLabel);
        panel.add(movieBox);

        panel.add(roomLabel);
        panel.add(roomBox);

        panel.add(dateLabel);
        panel.add(dateField);

        panel.add(timeLabel);
        panel.add(timeField);

        panel.add(new JLabel());
        panel.add(createButton);

        frame.add(panel);

        frame.setVisible(true);
    }
}