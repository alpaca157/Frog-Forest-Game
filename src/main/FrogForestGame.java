/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main;

/**
 *
 * @author Duda
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

public class FrogForestGame extends JFrame {
    private Jogador jogador;
    private boolean debugMode;
    private JPanel gamePanel;
    private JLabel[][] mapLabels;
    private final int MAP_SIZE = 10;
    private char[][] gameMap;
    private Point playerPosition;
    private java.util.List<Inimigo> inimigos;
    private java.util.List<Item> inventario;

    public FrogForestGame() {
        setTitle("Frog Forest - O Apocalipse do Cordyceps");
        setSize(800, 800);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Inicializa as listas
        inimigos = new ArrayList<>();
        inventario = new ArrayList<>();

        showWelcomeScreen();
    }

    private void showWelcomeScreen() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("Frog Forest - O Apocalipse do Cordyceps");
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel difficultyLabel = new JLabel("Escolha a dificuldade:");
        difficultyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        String[] difficulties = {"Fácil", "Médio", "Difícil"};
        JComboBox<String> difficultyBox = new JComboBox<>(difficulties);

        JButton playButton = new JButton("Jogar");
        JButton debugButton = new JButton("DEBUG");
        JButton exitButton = new JButton("Sair");

        playButton.addActionListener(e -> startGame(false, difficultyBox.getSelectedIndex()));
        debugButton.addActionListener(e -> startGame(true, difficultyBox.getSelectedIndex()));
        exitButton.addActionListener(e -> System.exit(0));

        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(title);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(difficultyLabel);
        panel.add(difficultyBox);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(playButton);
        panel.add(debugButton);
        panel.add(exitButton);

        setContentPane(panel);
        revalidate();
    }

    private void startGame(boolean debugMode, int difficultyIndex) {
        this.debugMode = debugMode;
        int playerPerception = switch (difficultyIndex) {
            case 0 -> 3; // Fácil
            case 1 -> 2; // Médio
            case 2 -> 1; // Difícil
            default -> 2;
        };
        jogador = new Jogador(playerPerception);
        inimigos = new ArrayList<>();
        inventario = new ArrayList<>();

        loadRandomMap();
        initGameInterface();
    }

    private void loadRandomMap() {
        File mapDir = new File("maps");
        File[] mapFiles = mapDir.listFiles((dir, name) -> name.endsWith(".txt"));

        if (mapFiles == null || mapFiles.length == 0) {
            JOptionPane.showMessageDialog(this, "Nenhum mapa encontrado na pasta 'maps'.");
            System.exit(0);
        }

        File selectedMap = mapFiles[new Random().nextInt(mapFiles.length)];
        gameMap = new char[MAP_SIZE][MAP_SIZE];

        try (BufferedReader br = new BufferedReader(new FileReader(selectedMap))) {
            for (int i = 0; i < MAP_SIZE; i++) {
                String line = br.readLine();
                for (int j = 0; j < MAP_SIZE; j++) {
                    gameMap[i][j] = line.charAt(j);
                    switch (gameMap[i][j]) {
                        case 'P' -> playerPosition = new Point(j, i);
                        case 'S' -> inimigos.add(new Perseguidor(new Point(j, i)));
                        case 'E' -> inimigos.add(new Estalador(new Point(j, i)));
                        case 'C' -> inimigos.add(new Corredor(new Point(j, i)));
                        case 'B' -> inimigos.add(new Baiacu(new Point(j, i)));
                        case 'I' -> inventario.add(new Item("Medicamento", new Point(j, i)));
                        default -> {
                        }
                    }
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar o mapa.");
            System.exit(0);
        }
    }

    private void initGameInterface() {
        gamePanel = new JPanel(new GridLayout(MAP_SIZE, MAP_SIZE));
        mapLabels = new JLabel[MAP_SIZE][MAP_SIZE];

        for (int i = 0; i < MAP_SIZE; i++) {
            for (int j = 0; j < MAP_SIZE; j++) {
                JLabel cellLabel = new JLabel();
                cellLabel.setPreferredSize(new Dimension(60, 60));
                cellLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
                cellLabel.setHorizontalAlignment(SwingConstants.CENTER);
                cellLabel.setOpaque(true);
                cellLabel.setBackground(Color.WHITE);

                mapLabels[i][j] = cellLabel;
                gamePanel.add(cellLabel);
            }
        }

        updateMapDisplay();

        JPanel sidePanel = new JPanel();
        sidePanel.setLayout(new BoxLayout(sidePanel, BoxLayout.Y_AXIS));
        sidePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel healthLabel = new JLabel("Vida: " + jogador.getVida());
        JLabel perceptionLabel = new JLabel("Percepção: " + jogador.getPercepcao());

        sidePanel.add(healthLabel);
        sidePanel.add(perceptionLabel);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(gamePanel, BorderLayout.CENTER);
        mainPanel.add(sidePanel, BorderLayout.EAST);

        setContentPane(mainPanel);
        addKeyListener(new MovementHandler());
        setFocusable(true);
        requestFocusInWindow(); // Garante que o JFrame tenha o foco
        revalidate();
    }

    private void updateMapDisplay() {
        for (int i = 0; i < MAP_SIZE; i++) {
            for (int j = 0; j < MAP_SIZE; j++) {
                if (debugMode || (i == playerPosition.y && j == playerPosition.x)) {
                    mapLabels[i][j].setText(String.valueOf(gameMap[i][j]));
                    mapLabels[i][j].setBackground(Color.WHITE); // Define a cor de fundo para caminhos visíveis
                } else {
                    mapLabels[i][j].setText("?");
                    mapLabels[i][j].setBackground(Color.GREEN); // Define a cor de fundo para áreas desconhecidas
                }
            }
        }
    }

private class MovementHandler extends KeyAdapter {
    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        int x = playerPosition.x;
        int y = playerPosition.y;

        switch (key) {
            case KeyEvent.VK_UP -> y = Math.max(0, y - 1);
            case KeyEvent.VK_DOWN -> y = Math.min(MAP_SIZE - 1, y + 1);
            case KeyEvent.VK_LEFT -> x = Math.max(0, x - 1);
            case KeyEvent.VK_RIGHT -> x = Math.min(MAP_SIZE - 1, x + 1);
        }

        // Verifica se a nova posição é válida (não é uma parede)
        if (gameMap[y][x] != '#') {
            boolean collisionWithEnemy = false;

            // Verifica colisão com inimigos
            for (Inimigo inimigo : inimigos) {
                if (inimigo.getPosicao().equals(new Point(x, y))) {
                    iniciarCombate(inimigo); // Inicia combate ao colidir com um inimigo
                    collisionWithEnemy = true;
                    break;
                }
            }

            if (!collisionWithEnemy) {
                playerPosition.setLocation(x, y); // Atualiza a posição do jogador
                updateMapDisplay();
                checkForItem(x, y); // Verifica se há itens na nova posição
                moveEnemies(); // Move os inimigos após o jogador se mover
            }
        }
    }
}

    private void moveEnemies() {
        for (Inimigo inimigo : inimigos) {
            inimigo.mover(gameMap, playerPosition);
        }
        updateMapDisplay(); // Atualiza o mapa após os inimigos se moverem
    }

    private void checkForItem(int x, int y) {
      Iterator<Item> it = inventario.iterator();
      while (it.hasNext()) {
        Item item = it.next();
        if (item.getPosicao().equals(new Point(x, y))) {
            JOptionPane.showMessageDialog(this, "Você encontrou um " + item.getNome() + "!");
            it.remove(); // Remove o item do inventário
            break;
        }
    }
}

    private void checkForCombat(int x, int y) {
        for (Inimigo inimigo : inimigos) {
            if (inimigo.getPosicao().equals(new Point(x, y))) {
                iniciarCombate(inimigo);
                return; // Sai do loop após iniciar o combate
            }
        }
    }

    private void iniciarCombate(Inimigo inimigo) {
        new CombatScreen(this, jogador, inimigo);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FrogForestGame().setVisible(true));
    }
}