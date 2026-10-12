
package com.mycompany.cinema;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Base64;
import java.util.Arrays;

public class StaffApplication {

    private static final Color BACKGROUND = new Color(20, 21, 25);
    private static final Color PANEL = new Color(31, 32, 38);
    private static final Color FIELD = new Color(42, 43, 50);
    private static final Color GOLD = new Color(226, 180, 86);
    private static final Color TEXT = new Color(242, 242, 245);
    private static final Color MUTED = new Color(165, 166, 175);
    private static final Color BORDER = new Color(65, 66, 75);

    private static final HttpClient CLIENT = HttpClient.newHttpClient();
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static String authorizationHeader;

    private static String getRoomsFromBackend() throws Exception {
        return getFromBackend(
                "http://localhost:8080/api/admin/sessions/rooms"
        );
    }

    private static String getMoviesFromBackend() throws Exception {
        return getFromBackend(
                "http://localhost:8080/api/admin/sessions/movies"
        );
    }

    private static String getSessionsFromBackend() throws Exception {
        return getFromBackend(
                "http://localhost:8080/api/admin/sessions"
        );
    }
    
    private static String getSessionsWithOccupancyFromBackend()
        throws Exception {
    return getFromBackend(
            "http://localhost:8080/api/admin/sessions/lotacao"
    );
}

    private static String getFromBackend(String url) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(url))
            .header("Authorization", authorizationHeader)
            .GET()
            .build();

        HttpResponse<String> response = CLIENT.send(
                request,
                HttpResponse.BodyHandlers.ofString()
        );

        if (response.statusCode() < 200
                || response.statusCode() >= 300) {
            throw new IllegalStateException(
                    "O servidor respondeu com o código "
                            + response.statusCode()
            );
        }

        return response.body();
    }

    private static JComboBox<MovieItem> createMovieBox() {
        JComboBox<MovieItem> box = new JComboBox<>();

        try {
            JsonNode movies = MAPPER.readTree(getMoviesFromBackend());

            for (JsonNode movie : movies) {
                box.addItem(new MovieItem(
                        movie.get("id").asInt(),
                        movie.get("name").asText()
                ));
            }
        } catch (Exception ex) {
            showLoadingError("filmes", ex);
        }

        styleComboBox(box);
        return box;
    }

    private static JComboBox<RoomItem> createRoomBox() {
        JComboBox<RoomItem> box = new JComboBox<>();

        try {
            JsonNode rooms = MAPPER.readTree(getRoomsFromBackend());

            for (JsonNode room : rooms) {
                box.addItem(new RoomItem(
                        room.get("id").asInt(),
                        room.get("name").asText(),
                        room.get("type").asText()
                ));
            }
        } catch (Exception ex) {
            showLoadingError("salas", ex);
        }

        styleComboBox(box);
        return box;
    }

    private static void showLoadingError(String item, Exception ex) {
        ex.printStackTrace();

        JOptionPane.showMessageDialog(
                null,
                "Não foi possível carregar " + item
                        + ". Confirma que o CineHub está em execução.",
                "Erro de ligação",
                JOptionPane.ERROR_MESSAGE
        );
    }

    
    private static void styleComboBox(JComboBox<?> box) {
        box.setFont(new Font("SansSerif", Font.PLAIN, 14));
        box.setForeground(TEXT);
        box.setBackground(FIELD);
        box.setFocusable(true);
        box.setPreferredSize(new Dimension(320, 44));

        box.setOpaque(true);
        box.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(
                    JList<?> list,
                    Object value,
                    int index,
                    boolean isSelected,
                    boolean cellHasFocus) {

                super.getListCellRendererComponent(
                        list, value, index, isSelected, cellHasFocus
                );

                setForeground(TEXT);
                setBackground(isSelected ? new Color(75, 65, 43) : FIELD);
                setFont(new Font("SansSerif", Font.PLAIN, 14));
                setBorder(new EmptyBorder(6, 8, 6, 8));

                return this;
            }
        });
    }


    private static JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("SansSerif", Font.BOLD, 13));
        label.setForeground(MUTED);
        return label;
    }

    private static JTextField createField(String value) {
        JTextField field = new JTextField(value);
        field.setFont(new Font("SansSerif", Font.PLAIN, 14));
        field.setForeground(TEXT);
        field.setBackground(FIELD);
        field.setCaretColor(GOLD);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER),
                new EmptyBorder(10, 12, 10, 12)
        ));
        field.setPreferredSize(new Dimension(320, 44));
        return field;
    }

    private static JPanel createFieldPanel(
            String labelText,
            JComponent component
    ) {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel label = createLabel(labelText);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        component.setAlignmentX(Component.LEFT_ALIGNMENT);

        panel.add(label);
        panel.add(Box.createVerticalStrut(9));
        panel.add(component);

        return panel;
    }
    
private static boolean authenticateStaff() {
    while (true) {
        JTextField usernameField = new JTextField(20);
        JPasswordField passwordField = new JPasswordField(20);

        JPanel panel = new JPanel(new GridLayout(0, 1, 5, 5));
        panel.add(new JLabel("Username"));
        panel.add(usernameField);
        panel.add(new JLabel("Password"));
        panel.add(passwordField);

        int result = JOptionPane.showConfirmDialog(
                null,
                panel,
                "CineHub | Login de funcionário",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result != JOptionPane.OK_OPTION) {
            return false;
        }

        String username = usernameField.getText().trim();
        char[] password = passwordField.getPassword();

        if (username.isEmpty() || password.length == 0) {
            Arrays.fill(password, '\0');

            JOptionPane.showMessageDialog(
                    null,
                    "Introduz o username e a password.",
                    "Dados em falta",
                    JOptionPane.WARNING_MESSAGE
            );
            continue;
        }

        try {
            String credentials = username + ":"
                    + new String(password);

            String candidateHeader = "Basic "
                    + Base64.getEncoder().encodeToString(
                            credentials.getBytes(StandardCharsets.UTF_8)
                    );

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(
                            "http://localhost:8080/api/admin/sessions"
                    ))
                    .header("Authorization", candidateHeader)
                    .GET()
                    .build();

            HttpResponse<String> response = CLIENT.send(
                    request,
                    HttpResponse.BodyHandlers.ofString()
            );

            if (response.statusCode() == 200) {
                authorizationHeader = candidateHeader;

                Arrays.fill(password, '\0');
                passwordField.setText("");

                return true;
            }

            Arrays.fill(password, '\0');
            passwordField.setText("");

            String message;

            if (response.statusCode() == 401) {
                message = "Username ou password incorretos.";
            } else if (response.statusCode() == 403) {
                message = "A tua conta não tem permissão "
                        + "para aceder à gestão de sessões.";
            } else {
                message = "O servidor respondeu com o código "
                        + response.statusCode() + ".";
            }

            JOptionPane.showMessageDialog(
                    null,
                    message,
                    "Falha no login",
                    JOptionPane.ERROR_MESSAGE
            );

        } catch (Exception ex) {
            Arrays.fill(password, '\0');
            passwordField.setText("");

            JOptionPane.showMessageDialog(
                    null,
                    "Não foi possível contactar o CineHub.\n"
                            + "Confirma que o backend está "
                            + "em execução em localhost:8080.",
                    "Erro de ligação",
                    JOptionPane.ERROR_MESSAGE
            );

            return false;
        }
    }
}


    public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
        try {
            UIManager.setLookAndFeel(
                UIManager.getCrossPlatformLookAndFeelClassName()
        );
        } catch (Exception ignored) {
            // Mantém o Look and Feel disponível.
        }

        if (authenticateStaff()) {
            createWindow();
        }
    });
}

    private static void createWindow() {
        
        
        UIManager.put("TabbedPane.background", PANEL);
        UIManager.put("TabbedPane.foreground", TEXT);
        UIManager.put("TabbedPane.selected", FIELD);
        UIManager.put("TabbedPane.contentAreaColor", BACKGROUND);
        UIManager.put("TabbedPane.focus", GOLD);

        UIManager.put("ComboBox.background", FIELD);
        UIManager.put("ComboBox.foreground", TEXT);
        UIManager.put("ComboBox.selectionBackground", new Color(75, 65, 43));
        UIManager.put("ComboBox.selectionForeground", TEXT);

        
        JFrame frame = new JFrame("CineHub | Gestão de sessões");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(760, 720);
        frame.setMinimumSize(new Dimension(650, 650));
        frame.setLocationRelativeTo(null);
        frame.getContentPane().setBackground(BACKGROUND);
        frame.setLayout(new BorderLayout());

        // Cabeçalho
        JPanel header = new JPanel();
        header.setBackground(PANEL);
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setBorder(new EmptyBorder(28, 38, 26, 38));

        JLabel logo = new JLabel("CINEHUB");
        logo.setFont(new Font("SansSerif", Font.BOLD, 15));
        logo.setForeground(GOLD);

        JLabel title = new JLabel("Criar sessão");
        title.setFont(new Font("SansSerif", Font.BOLD, 29));
        title.setForeground(TEXT);

        JLabel subtitle = new JLabel(
                "Configura um novo horário de exibição."
        );
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 14));
        subtitle.setForeground(MUTED);

        header.add(logo);
        header.add(Box.createVerticalStrut(14));
        header.add(title);
        header.add(Box.createVerticalStrut(8));
        header.add(subtitle);

        // Separadores da aplicação
        
        
        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(new Font("SansSerif", Font.BOLD, 13));
        tabs.setBackground(BACKGROUND);
        tabs.setForeground(TEXT);
        tabs.setOpaque(true);

        tabs.setUI(new javax.swing.plaf.basic.BasicTabbedPaneUI() {
            @Override
            protected void installDefaults() {
                super.installDefaults();
                tabPane.setOpaque(true);
            }

            @Override
            protected void paintTabBackground(
                    Graphics g,
                    int tabPlacement,
                    int tabIndex,
                    int x,
                    int y,
                    int w,
                    int h,
                    boolean isSelected) {

                g.setColor(isSelected ? FIELD : PANEL);
                g.fillRect(x, y, w, h);
            }

            @Override
            protected void paintContentBorder(
                    Graphics g,
                    int tabPlacement,
                    int selectedIndex) {

                g.setColor(BORDER);

                int x = tabPane.getInsets().left;
                int y = calculateTabAreaHeight(
                        tabPlacement, runCount, maxTabHeight
                ) + tabPane.getInsets().top;
                int w = tabPane.getWidth()
                        - tabPane.getInsets().left
                        - tabPane.getInsets().right - 1;
                int h = tabPane.getHeight() - y
                        - tabPane.getInsets().bottom - 1;

                g.drawRect(x, y, w, h);
            }
        });


        UIManager.put("TabbedPane.background", PANEL);
        UIManager.put("TabbedPane.foreground", TEXT);
        UIManager.put("TabbedPane.selected", FIELD);
        UIManager.put("TabbedPane.contentAreaColor", BACKGROUND);
        UIManager.put("TabbedPane.focus", GOLD);


        frame.add(header, BorderLayout.NORTH);

        // Formulário
        JPanel content = new JPanel(new GridBagLayout());
        content.setBackground(BACKGROUND);
        content.setBorder(new EmptyBorder(28, 38, 28, 38));

        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBackground(PANEL);
        form.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER),
                new EmptyBorder(26, 28, 28, 28)
        ));

        JLabel sectionTitle = new JLabel("Detalhes da sessão");
        sectionTitle.setFont(new Font("SansSerif", Font.BOLD, 18));
        sectionTitle.setForeground(TEXT);
        sectionTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        form.add(sectionTitle);
        form.add(Box.createVerticalStrut(24));

        JComboBox<MovieItem> movieBox = createMovieBox();
        JComboBox<RoomItem> roomBox = createRoomBox();

        JTextField dateField = createField(
                LocalDate.now().toString()
        );
        JTextField timeField = createField("20:00");

        form.add(createFieldPanel("FILME", movieBox));
        form.add(Box.createVerticalStrut(19));

        form.add(createFieldPanel("SALA", roomBox));
        form.add(Box.createVerticalStrut(19));

        form.add(createFieldPanel("DATA (AAAA-MM-DD)", dateField));
        form.add(Box.createVerticalStrut(19));

        form.add(createFieldPanel("HORA (HH:MM)", timeField));
        form.add(Box.createVerticalStrut(27));

        JButton createButton = new JButton("CRIAR SESSÃO");
        createButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        createButton.setForeground(new Color(30, 27, 20));
        createButton.setBackground(GOLD);
        createButton.setFocusPainted(false);
        createButton.setBorderPainted(false);
        createButton.setCursor(Cursor.getPredefinedCursor(
                Cursor.HAND_CURSOR
        ));
        createButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        createButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
        createButton.setPreferredSize(new Dimension(320, 48));

        JLabel statusLabel = new JLabel(" ");
        statusLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        statusLabel.setForeground(MUTED);
        statusLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        form.add(createButton);
        form.add(Box.createVerticalStrut(14));
        form.add(statusLabel);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.NORTH;
        content.add(form, gbc);

        tabs.addTab("Criar sessão", content);
        tabs.addTab("Sessões existentes", createSessionsPanel());

        frame.add(tabs, BorderLayout.CENTER);

        // Criação da sessão
        createButton.addActionListener(e -> {
            MovieItem selectedMovie =
                    (MovieItem) movieBox.getSelectedItem();

            RoomItem selectedRoom =
                    (RoomItem) roomBox.getSelectedItem();

            if (selectedMovie == null || selectedRoom == null) {
                showMessage(
                        frame,
                        "Seleciona um filme e uma sala.",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            String date = dateField.getText().trim();
            String time = timeField.getText().trim();

            try {
                LocalDate.parse(date);
                LocalTime.parse(time);
            } catch (DateTimeParseException ex) {
                showMessage(
                        frame,
                        "Verifica a data e a hora.\n"
                                + "Data: AAAA-MM-DD (ex.: 2026-10-10)\n"
                                + "Hora: HH:MM (ex.: 20:00)",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            createButton.setEnabled(false);
            statusLabel.setForeground(MUTED);
            statusLabel.setText("A criar sessão...");

            // O pedido de rede corre em segundo plano para a janela
            // continuar responsiva.
            new SwingWorker<HttpResponse<String>, Void>() {
                @Override
                protected HttpResponse<String> doInBackground()
                        throws Exception {

                    String url =
                            "http://localhost:8080/api/admin/sessions"
                            + "?movieId=" + selectedMovie.getId()
                            + "&roomId=" + selectedRoom.getId()
                            + "&date=" + encode(date)
                            + "&time=" + encode(time);

                    HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(url))
                        .header("Authorization", authorizationHeader)
                        .POST(HttpRequest.BodyPublishers.noBody())
                        .build();

                    return CLIENT.send(
                            request,
                            HttpResponse.BodyHandlers.ofString()
                    );
                }

                @Override
                protected void done() {
                    createButton.setEnabled(true);

                    try {
                        HttpResponse<String> response = get();

                        if (response.statusCode() >= 200
                                && response.statusCode() < 300) {

                            statusLabel.setForeground(
                                    new Color(111, 205, 139)
                            );
                            statusLabel.setText(
                                    "Sessão criada com sucesso."
                            );

                            showMessage(
                                    frame,
                                    "A sessão foi criada com sucesso.",
                                    JOptionPane.INFORMATION_MESSAGE
                            );

                        } else {
                            statusLabel.setForeground(
                                    new Color(239, 113, 113)
                            );
                            statusLabel.setText(
                                    "Não foi possível criar a sessão."
                            );

                            showMessage(
                                    frame,
                                    "O servidor respondeu com o código "
                                            + response.statusCode()
                                            + ".\n"
                                            + response.body(),
                                    JOptionPane.ERROR_MESSAGE
                            );
                        }

                    } catch (Exception ex) {
                        ex.printStackTrace();

                        statusLabel.setForeground(
                                new Color(239, 113, 113)
                        );
                        statusLabel.setText(
                                "Erro de ligação ao servidor."
                        );

                        showMessage(
                                frame,
                                "Não foi possível contactar o CineHub.\n"
                                        + "Confirma que o backend está "
                                        + "em execução em localhost:8080.",
                                JOptionPane.ERROR_MESSAGE
                        );
                    }
                }
            }.execute();
        });

        frame.setVisible(true);
    }

    private static String encode(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }

    private static void showMessage(
            Component parent,
            String message,
            int messageType
    ) {
        JOptionPane.showMessageDialog(
                parent,
                message,
                "CineHub",
                messageType
        );
    }
    private static JPanel createSessionsPanel() {
    JPanel panel = new JPanel(new BorderLayout(0, 16));
    panel.setBackground(BACKGROUND);
    panel.setBorder(new EmptyBorder(24, 28, 24, 28));

    String[] columns = {
        "Filme", "Data", "Hora", "Sala",
        "Bilhetes", "Capacidade", "Disponíveis"
    };

    javax.swing.table.DefaultTableModel model =
            new javax.swing.table.DefaultTableModel(columns, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };

    JTable table = new JTable(model);
    table.setFont(new Font("SansSerif", Font.PLAIN, 12));
    table.setRowHeight(28);
    table.setBackground(PANEL);
    table.setForeground(TEXT);
    table.setGridColor(BORDER);
    table.setSelectionBackground(new Color(75, 65, 43));
    table.setSelectionForeground(TEXT);
    table.getTableHeader().setBackground(FIELD);
    table.getTableHeader().setForeground(GOLD);
    table.getTableHeader().setFont(
            new Font("SansSerif", Font.BOLD, 12)
    );
    table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

    JScrollPane scrollPane = new JScrollPane(table);
    scrollPane.getViewport().setBackground(PANEL);
    scrollPane.setBorder(BorderFactory.createLineBorder(BORDER));

    JButton refreshButton = new JButton("ATUALIZAR");
    refreshButton.setFont(new Font("SansSerif", Font.BOLD, 13));
    refreshButton.setForeground(new Color(30, 27, 20));
    refreshButton.setBackground(GOLD);
    refreshButton.setFocusPainted(false);
    refreshButton.setBorderPainted(false);
    refreshButton.setCursor(Cursor.getPredefinedCursor(
            Cursor.HAND_CURSOR
    ));

    JLabel statusLabel = new JLabel(" ");
    statusLabel.setForeground(MUTED);
    statusLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));

    JPanel topPanel = new JPanel(new BorderLayout());
    topPanel.setOpaque(false);

    JLabel title = new JLabel("Sessões existentes");
    title.setForeground(TEXT);
    title.setFont(new Font("SansSerif", Font.BOLD, 20));

    topPanel.add(title, BorderLayout.WEST);
    topPanel.add(refreshButton, BorderLayout.EAST);

    panel.add(topPanel, BorderLayout.NORTH);
    panel.add(scrollPane, BorderLayout.CENTER);
    panel.add(statusLabel, BorderLayout.SOUTH);

    refreshButton.addActionListener(e -> {
        refreshButton.setEnabled(false);
        statusLabel.setText("A carregar sessões...");

        new SwingWorker<JsonNode, Void>() {
            @Override
            protected JsonNode doInBackground() throws Exception {
                return MAPPER.readTree(
                        getSessionsWithOccupancyFromBackend()
                );
            }

            @Override
            protected void done() {
                refreshButton.setEnabled(true);

                try {
                    JsonNode sessions = get();

                    model.setRowCount(0);

                    for (JsonNode session : sessions) {
                        model.addRow(new Object[] {
                            session.path("movie").asText("—"),
                            session.path("date").asText("—"),
                            session.path("time").asText("—"),
                            session.path("room").asText("—"),
                            session.path("registeredTickets").asInt(),
                            session.path("capacity").asInt(),
                            session.path("availableSeats").asInt()
                        });
                    }

                    statusLabel.setText(
                            sessions.size() + " sessões encontradas."
                    );

                } catch (Exception ex) {
                    statusLabel.setText(
                            "Não foi possível carregar as sessões."
                    );

                    JOptionPane.showMessageDialog(
                            panel,
                            "Erro ao carregar as sessões.\n"
                                    + ex.getMessage(),
                            "CineHub",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        }.execute();
    });

    // Carrega os dados ao abrir o separador.
    refreshButton.doClick();

    return panel;
}
}
