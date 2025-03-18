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
    public char[][] gameMap;
    private Point playerPosition;
    private final java.util.List<Inimigo> inimigos;
    private final java.util.List<Item> inventario;

    // Constantes para os caracteres do mapa
    private static final char WALL = '#';
    private static final char PATH = 'V';
    private static final char PLAYER = 'P';
    private static final char ENEMY_S = 'S'; // Perseguidor
    private static final char ENEMY_E = 'E'; // Estalador
    private static final char ENEMY_C = 'C'; // Corredor
    private static final char ENEMY_B = 'B'; // Baiacu
    private static final char ITEM = 'I';   // Item
    private JPanel statusPanel;
    public Font customFont;

    public FrogForestGame() {
                try {
            // Carrega a fonte personalizada
            customFont = Font.createFont(Font.TRUETYPE_FONT, new File("fontes/Silkscreen-Bold.ttf")).deriveFont(36f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(customFont); // Registra a fonte no sistema
        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao carregar a fonte personalizada.");
        }
        setTitle("Frog Forest Apocalipse");
        setSize(800, 800);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Inicializa as listas
        inimigos = new ArrayList<>();
        inventario = new ArrayList<>();

        showWelcomeScreen();
    }

private void showWelcomeScreen() {
    JPanel panel = new JPanel() {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            ImageIcon backgroundImage = new ImageIcon("image/wellcomescreen.jpg");
            g.drawImage(backgroundImage.getImage(), 0, 0, getWidth(), getHeight(), this);
        }
    };
    panel.setLayout(new BorderLayout());

    // Rótulo do título com a fonte personalizada e cor #abcc85
    JLabel titleLabel = new JLabel("Frog Forest Apocalipse", SwingConstants.CENTER);
    titleLabel.setFont(customFont); // Usa a fonte carregada
    titleLabel.setForeground(new Color(0xABCC85));

    JPanel centerPanel = new JPanel(new GridBagLayout());
    centerPanel.setOpaque(false);
    centerPanel.add(titleLabel);

    panel.add(centerPanel, BorderLayout.CENTER);

    JPanel bottomPanel = new JPanel(new GridBagLayout());
    bottomPanel.setOpaque(false);
    bottomPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

    // Subtítulo "Escolha a dificuldade:" com a fonte personalizada e cor #abcc85
    JLabel difficultyLabel = new JLabel("Escolha a dificuldade:", SwingConstants.CENTER);
    try {
        Font akayaFont = Font.createFont(Font.TRUETYPE_FONT, new File("fontes/Silkscreen-Bold.ttf")).deriveFont(24f);
        difficultyLabel.setFont(akayaFont);
    } catch (IOException | FontFormatException e) {
        e.printStackTrace();
        difficultyLabel.setFont(new Font("Arial", Font.PLAIN, 24)); // Fallback para Arial
    }
    difficultyLabel.setForeground(new Color(0x000000));

    JButton easyButton = new JButton("Fácil");
    JButton mediumButton = new JButton("Médio");
    JButton hardButton = new JButton("Difícil");

    easyButton.addActionListener(e -> showMainMenu(0));
    mediumButton.addActionListener(e -> showMainMenu(1));
    hardButton.addActionListener(e -> showMainMenu(2));

    GridBagConstraints gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.insets = new Insets(5, 5, 5, 5);
    gbc.anchor = GridBagConstraints.CENTER;

    bottomPanel.add(difficultyLabel, gbc);

    gbc.gridy = 1;
    bottomPanel.add(easyButton, gbc);

    gbc.gridy = 2;
    bottomPanel.add(mediumButton, gbc);

    gbc.gridy = 3;
    bottomPanel.add(hardButton, gbc);

    panel.add(bottomPanel, BorderLayout.SOUTH);

    setContentPane(panel);
    revalidate();
}

private void showMainMenu(int difficultyIndex) {
    JPanel panel = new JPanel() {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            ImageIcon backgroundImage = new ImageIcon("image/finalscreen.gif");
            g.drawImage(backgroundImage.getImage(), 0, 0, getWidth(), getHeight(), this);
        }
    };
    panel.setLayout(new BorderLayout());

    // Rótulo do título com a mesma fonte e cor da tela de boas-vindas
    JLabel titleLabel = new JLabel("Frog Forest Apocalipse", SwingConstants.CENTER);
    if (customFont != null) {
        titleLabel.setFont(customFont);
    } else {
        titleLabel.setFont(new Font("Arial", Font.BOLD, 36)); // Fallback para Arial
    }
    titleLabel.setForeground(new Color(0xABCC85));

    JPanel centerPanel = new JPanel(new GridBagLayout());
    centerPanel.setOpaque(false);
    centerPanel.add(titleLabel);
    panel.add(centerPanel, BorderLayout.CENTER);

    JPanel bottomPanel = new JPanel(new GridBagLayout());
    bottomPanel.setOpaque(false);
    bottomPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

    JButton playButton = new JButton("Jogar");
    JButton debugButton = new JButton("DEBUG");
    JButton exitButton = new JButton("Sair");

    playButton.addActionListener(e -> startGame(false, difficultyIndex));
    debugButton.addActionListener(e -> startGame(true, difficultyIndex));
    exitButton.addActionListener(e -> System.exit(0));

    GridBagConstraints gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.insets = new Insets(5, 5, 5, 5);
    gbc.anchor = GridBagConstraints.CENTER;

    bottomPanel.add(playButton, gbc);

    gbc.gridy = 1;
    bottomPanel.add(debugButton, gbc);

    gbc.gridy = 2;
    bottomPanel.add(exitButton, gbc);

    panel.add(bottomPanel, BorderLayout.SOUTH);

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
        inimigos.clear();
        inventario.clear();

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

                    // Posição inicial do jogador
                    if (gameMap[i][j] == PLAYER) {
                        playerPosition = new Point(j, i);
                    }

                    // Instanciação dos inimigos com vida específica
                    switch (gameMap[i][j]) {
                        case ENEMY_S -> inimigos.add(new Perseguidor(new Point(j, i), 20)); // Vida: 20%
                        case ENEMY_E -> inimigos.add(new Estalador(new Point(j, i), 30)); // Vida: 30%
                        case ENEMY_C -> inimigos.add(new Corredor(new Point(j, i), 30)); // Vida: 30%
                        case ENEMY_B -> inimigos.add(new Baiacu(new Point(j, i), 50)); // Vida: 50%
                        case ITEM -> {
                            // Distribuição dos itens
                            if (inventario.stream().filter(item -> item.getNome().equals("Taco de Baseball")).count() == 0) {
                                inventario.add(new Item("Taco de Baseball", new Point(j, i)));
                            } else if (inventario.stream().filter(item -> item.getNome().equals("Arma")).count() == 0) {
                                inventario.add(new Item("Arma", new Point(j, i)));
                            } else if (inventario.stream().filter(item -> item.getNome().equals("Medicamento")).count() < 2) {
                                inventario.add(new Item("Medicamento", new Point(j, i)));
                            }
                        }
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

private JPanel createStatusPanel() {
    statusPanel = new JPanel();
    statusPanel.setLayout(new GridLayout(1, 4));
    updateStatusPanel(); // Inicializa o painel com os valores atuais
    return statusPanel;
}

public void updateStatusPanel() {
    // Remove todos os componentes do painel de status
    statusPanel.removeAll();

    // Cria novos labels com os valores atualizados
    JLabel healthLabel = new JLabel("Vida: " + jogador.getVida() + "%");
    JLabel perceptionLabel = new JLabel("Percepção: " + jogador.getPercepcao());
    JLabel enemiesLabel = new JLabel("Inimigos Restantes: " + inimigos.size());

    // Adiciona os novos labels ao painel de status
    statusPanel.add(healthLabel);
    statusPanel.add(perceptionLabel);
    statusPanel.add(enemiesLabel);

    // Atualiza o layout do painel
    statusPanel.revalidate();
    statusPanel.repaint();
}

private void initGameInterface() {
    JPanel mainPanel = new JPanel(new BorderLayout());

    JPanel painelDeStatus = createStatusPanel();
    mainPanel.add(painelDeStatus, BorderLayout.NORTH);

    gamePanel = new JPanel(new GridLayout(MAP_SIZE, MAP_SIZE));
    mapLabels = new JLabel[MAP_SIZE][MAP_SIZE];

    for (int i = 0; i < MAP_SIZE; i++) {
        for (int j = 0; j < MAP_SIZE; j++) {
            JLabel cellLabel = new JLabel();
            cellLabel.setPreferredSize(new Dimension(60, 60));
            cellLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
            cellLabel.setHorizontalAlignment(SwingConstants.CENTER);
            cellLabel.setOpaque(true);

            mapLabels[i][j] = cellLabel;
            gamePanel.add(cellLabel);
        }
    }

    mainPanel.add(gamePanel, BorderLayout.CENTER);

    updateMapDisplay();

    // Remove qualquer KeyListener existente antes de adicionar um novo
    for (KeyListener listener : getKeyListeners()) {
        removeKeyListener(listener);
    }

    // Adiciona o KeyListener
    addKeyListener(new MovementHandler());
    setFocusable(true);

    setContentPane(mainPanel);
    revalidate();
}

    void updateMapDisplay() {
        for (int i = 0; i < MAP_SIZE; i++) {
            for (int j = 0; j < MAP_SIZE; j++) {
                JLabel cellLabel = mapLabels[i][j];
                char cell = gameMap[i][j];

                // Verifica se a célula está dentro da visão do jogador
                boolean isVisible = isCellVisible(i, j);

                if (isVisible || debugMode) {
                    // Define a imagem com base no caractere do mapa
                    ImageIcon icon = null;
                    switch (cell) {
                        case PLAYER -> icon = new ImageIcon("image/player.png");
                        case WALL -> icon = new ImageIcon("image/tree.png");
                        case ENEMY_S -> {
                            if (debugMode) {
                                icon = new ImageIcon("image/enemy1.png");
                            } else {
                                icon = new ImageIcon("image/bush.png"); // Perseguidor invisível
                            }
                        }
                        case ENEMY_E -> icon = new ImageIcon("image/enemy2.png");
                        case ENEMY_C -> icon = new ImageIcon("image/enemy3.png");
                        case ENEMY_B -> icon = new ImageIcon("image/enemy4.png");
                        case ITEM -> icon = new ImageIcon("image/item.png");
                        case PATH -> icon = new ImageIcon("image/path.png");
                        default -> icon = new ImageIcon("image/bush.png");
                    }
                    cellLabel.setIcon(icon);
                } else {
                    // Cobre a célula com arbustos se não estiver visível
                    cellLabel.setIcon(new ImageIcon("image/bush.png"));
                }
            }
        }

        // Atualiza a posição do jogador
        mapLabels[playerPosition.y][playerPosition.x].setIcon(new ImageIcon("image/player.png"));
    }

private boolean isCellVisible(int row, int col) {
    int playerRow = playerPosition.y;
    int playerCol = playerPosition.x;

    // Verifica se a célula está na mesma linha ou coluna do jogador
    if (row == playerRow || col == playerCol) {
        // Norte
        if (row < playerRow) {
            for (int i = playerRow - 1; i >= row; i--) {
                char cell = gameMap[i][col];
                if (cell == WALL || cell == ITEM || isEnemy(cell)) {
                    return i == row; // Somente a célula do obstáculo é visível
                }
            }
            return true;
        }
        // Sul
        else if (row > playerRow) {
            for (int i = playerRow + 1; i <= row; i++) {
                char cell = gameMap[i][col];
                if (cell == WALL || cell == ITEM || isEnemy(cell)) {
                    return i == row;
                }
            }
            return true;
        }
        // Oeste
        else if (col < playerCol) {
            for (int j = playerCol - 1; j >= col; j--) {
                char cell = gameMap[row][j];
                if (cell == WALL || cell == ITEM || isEnemy(cell)) {
                    return j == col;
                }
            }
            return true;
        }
        // Leste
        else if (col > playerCol) {
            for (int j = playerCol + 1; j <= col; j++) {
                char cell = gameMap[row][j];
                if (cell == WALL || cell == ITEM || isEnemy(cell)) {
                    return j == col;
                }
            }
            return true;
        }
    }
    return false; // Célula não visível
}

private boolean isEnemy(char cell) {
    return cell == ENEMY_S || cell == ENEMY_E || cell == ENEMY_C || cell == ENEMY_B;
}

    char[][] getGameMap() {
        return gameMap;
    }
    
public void atualizarVisao() {
    int x = playerPosition.y;
    int y = playerPosition.x;

    for (int i = 0; i < MAP_SIZE; i++) {
        for (int j = 0; j < MAP_SIZE; j++) {
            if (isCellVisible(i, j) || debugMode) {
                mapLabels[i][j].setIcon(getCellIcon(gameMap[i][j]));
            } else {
                mapLabels[i][j].setIcon(new ImageIcon("image/bush.png"));
            }
        }
    }
    mapLabels[x][y].setIcon(new ImageIcon("image/player.png"));

    // Visão à direita (leste)
    for (int j = y + 1; j < MAP_SIZE; j++) {
        char cell = gameMap[x][j];
        mapLabels[x][j].setIcon(getCellIcon(cell)); // Exibe o ícone correspondente
        if (cell == WALL || cell == ITEM || cell == ENEMY_S || cell == ENEMY_E || cell == ENEMY_C || cell == ENEMY_B) {
            break; // Para a visão ao encontrar uma parede, baú ou inimigo
        }
    }

    // Visão à esquerda (oeste)
    for (int j = y - 1; j >= 0; j--) {
        char cell = gameMap[x][j];
        mapLabels[x][j].setIcon(getCellIcon(cell)); // Exibe o ícone correspondente
        if (cell == WALL || cell == ITEM || cell == ENEMY_S || cell == ENEMY_E || cell == ENEMY_C || cell == ENEMY_B) {
            break; // Para a visão ao encontrar uma parede, baú ou inimigo
        }
    }

    // Visão abaixo (sul)
    for (int i = x + 1; i < MAP_SIZE; i++) {
        char cell = gameMap[i][y];
        mapLabels[i][y].setIcon(getCellIcon(cell)); // Exibe o ícone correspondente
        if (cell == WALL || cell == ITEM || cell == ENEMY_S || cell == ENEMY_E || cell == ENEMY_C || cell == ENEMY_B) {
            break; // Para a visão ao encontrar uma parede, baú ou inimigo
        }
    }

    // Visão acima (norte)
    for (int i = x - 1; i >= 0; i--) {
        char cell = gameMap[i][y];
        mapLabels[i][y].setIcon(getCellIcon(cell)); // Exibe o ícone correspondente
        if (cell == WALL || cell == ITEM || cell == ENEMY_S || cell == ENEMY_E || cell == ENEMY_C || cell == ENEMY_B) {
            break; // Para a visão ao encontrar uma parede, baú ou inimigo
        }
    }
}
    
private ImageIcon getCellIcon(char cell) {
    switch (cell) {
        case WALL:
            return new ImageIcon("image/tree.png"); // Parede
        case ENEMY_S:
            return new ImageIcon("image/enemy1.png"); // Perseguidor
        case ENEMY_E:
            return new ImageIcon("image/enemy2.png"); // Estalador
        case ENEMY_C:
            return new ImageIcon("image/enemy3.png"); // Corredor
        case ENEMY_B:
            return new ImageIcon("image/enemy4.png"); // Baiacu
        case ITEM:
            return new ImageIcon("image/item.png"); // Item
        case PATH:
            return new ImageIcon("image/path.png"); // Caminho livre
        default:
            return new ImageIcon("image/bush.png"); // Arbusto (visão bloqueada)
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

        if (gameMap[y][x] != WALL) {
            // Limpa a posição anterior do jogador
            gameMap[playerPosition.y][playerPosition.x] = PATH;

            // Atualiza a posição do jogador
            playerPosition.setLocation(x, y);
            gameMap[y][x] = PLAYER;

            // Atualiza o mapa e a visão do jogador
            atualizarVisao();
            checkForItem(x, y);
            checkForCombat(x, y);
            moveEnemies();
        }
    }
}

    private boolean isPositionOccupied(Point posicao) {
        return inimigos.stream()
                .anyMatch(inimigo -> inimigo.getPosicao().equals(posicao));
    }

private void moveEnemies() {
    // Cria um conjunto com as posições atuais de todos os inimigos
    Set<Point> posicoesInimigos = new HashSet<>();
    for (Inimigo inimigo : inimigos) {
        posicoesInimigos.add(inimigo.getPosicao());
    }

    for (Inimigo inimigo : inimigos) {
        if (!(inimigo instanceof Baiacu)) { // Baiacu não se move
            Point posicaoAtual = inimigo.getPosicao();
            posicoesInimigos.remove(posicaoAtual); // Remove a posição atual do inimigo temporariamente

            // Move o inimigo
            inimigo.mover(gameMap, playerPosition, posicoesInimigos);

            // Atualiza a posição no mapa
            gameMap[posicaoAtual.y][posicaoAtual.x] = 'V'; // Limpa a posição anterior
            gameMap[inimigo.getPosicao().y][inimigo.getPosicao().x] = getEnemyChar(inimigo);

            posicoesInimigos.add(inimigo.getPosicao()); // Adiciona a nova posição ao conjunto
        }
    }

    updateMapDisplay();
}

    // Método auxiliar para obter o caractere do inimigo no mapa
    private char getEnemyChar(Inimigo inimigo) {
        if (inimigo instanceof Perseguidor) return ENEMY_S;
        if (inimigo instanceof Estalador) return ENEMY_E;
        if (inimigo instanceof Corredor) return ENEMY_C;
        if (inimigo instanceof Baiacu) return ENEMY_B;
        return ' ';
    }

private Point calcularNovaPosicao(Point posicaoAtual, Point jogadorPosicao) {
    int dx = Integer.compare(jogadorPosicao.x, posicaoAtual.x);
    int dy = Integer.compare(jogadorPosicao.y, posicaoAtual.y);

    // Calcula a próxima posição diretamente na direção do jogador (inclui diagonais)
    Point novaPosicao = new Point(posicaoAtual.x + dx, posicaoAtual.y + dy);

    // Verifica se há um obstáculo na nova posição
    if (!isValidMove(novaPosicao) || isPositionOccupied(novaPosicao)) {
        // Tenta desviar para outras direções
        java.util.List<Point> direcoesPossiveis = new ArrayList<>();
        direcoesPossiveis.add(new Point(posicaoAtual.x + dx, posicaoAtual.y)); // Horizontal
        direcoesPossiveis.add(new Point(posicaoAtual.x, posicaoAtual.y + dy)); // Vertical
        direcoesPossiveis.add(new Point(posicaoAtual.x + dx, posicaoAtual.y + dy)); // Diagonal
        direcoesPossiveis.add(new Point(posicaoAtual.x - dx, posicaoAtual.y)); // Horizontal inverso
        direcoesPossiveis.add(new Point(posicaoAtual.x, posicaoAtual.y - dy)); // Vertical inverso

        // Remove direções inválidas ou ocupadas
        for (Point direcao : direcoesPossiveis) {
            if (isValidMove(direcao) && !isPositionOccupied(direcao)) {
                return direcao;
            }
        }
    }

    // Retorna a nova posição se não houver obstáculos, ou a posição atual se não for possível mover
    return isValidMove(novaPosicao) && !isPositionOccupied(novaPosicao) ? novaPosicao : posicaoAtual;
}

    private boolean isValidMove(Point posicao) {
        return posicao.x >= 0 && posicao.x < MAP_SIZE &&
               posicao.y >= 0 && posicao.y < MAP_SIZE &&
               gameMap[posicao.y][posicao.x] != WALL;
    }

    private void checkForItem(int x, int y) {
    java.util.List<Item> novosItens = new ArrayList<>(); // Lista temporária para armazenar novos itens
    Iterator<Item> it = inventario.iterator();

    while (it.hasNext()) {
        Item item = it.next();
        if (item.getPosicao().equals(new Point(x, y))) {
            String nomeItem = item.getNome();
            switch (nomeItem) {
                case "Medicamento" -> {
                    int opcao = JOptionPane.showOptionDialog(
                            this,
                            "Você encontrou um Medicamento! Deseja usar agora ou guardar?",
                            "Item Encontrado",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.QUESTION_MESSAGE,
                            null,
                            new String[]{"Curar", "Guardar"},
                            "Curar"
                    );
                    if (opcao == JOptionPane.YES_OPTION) {
                        jogador.setVida(Math.min(100, jogador.getVida() + 50));
                        JOptionPane.showMessageDialog(this, "Você usou o medicamento e recuperou 50% de vida!");
                        updateStatusPanel();
                    } else {
                        // Adiciona o medicamento à lista temporária
                        novosItens.add(new Item("Medicamento Guardado", new Point(-1, -1)));
                        JOptionPane.showMessageDialog(this, "Você guardou o medicamento no inventário.");
                    }
                }
                case "Arma" -> {
                    jogador.ganharArma(true);
                    JOptionPane.showMessageDialog(this, "Você encontrou uma arma!");
                }
                case "Taco de Baseball" -> {
                    jogador.ganharTaco(true);
                    JOptionPane.showMessageDialog(this, "Você encontrou um taco de baseball! Ele substitui o ataque da mão e causa 10% de dano a mais.");
                }
            }
            it.remove(); // Remove o item atual da lista principal
            break;
        }
    }

    // Adiciona os novos itens à lista principal após a iteração
    inventario.addAll(novosItens);
}

    private void checkForCombat(int x, int y) {
        for (Inimigo inimigo : inimigos) {
            if (inimigo.getPosicao().equals(new Point(x, y))) {
                iniciarCombate(inimigo);
                break;
            }
        }
    }

    public void removerInimigo(Inimigo inimigo) {
        inimigos.remove(inimigo);
        updateMapDisplay();
    }

    public boolean verificarVitoria() {
        return inimigos.isEmpty();
    }

public void exibirTelaVitoria() {
    JPanel panel = new JPanel() {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            ImageIcon backgroundImage = new ImageIcon("image/screen3.jpg"); // Usa o mesmo fundo da tela inicial
            g.drawImage(backgroundImage.getImage(), 0, 0, getWidth(), getHeight(), this);
        }
    };
    panel.setLayout(new BorderLayout());

    // Título "Você venceu o jogo!" com a fonte personalizada e cor #abcc85
    JLabel titleLabel = new JLabel("Você venceu o jogo!", SwingConstants.CENTER);
    titleLabel.setFont(customFont); // Usa a fonte carregada
    titleLabel.setForeground(new Color(0xABCC85)); // Cor #abcc85

    JPanel centerPanel = new JPanel(new GridBagLayout());
    centerPanel.setOpaque(false);
    centerPanel.add(titleLabel);

    panel.add(centerPanel, BorderLayout.CENTER);

    JPanel bottomPanel = new JPanel(new GridBagLayout());
    bottomPanel.setOpaque(false);
    bottomPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

    JButton restartButton = new JButton("Reiniciar Jogo");
    JButton newGameButton = new JButton("Novo Jogo");

    restartButton.addActionListener(e -> reiniciarJogo());
    newGameButton.addActionListener(e -> novoJogo());

    GridBagConstraints gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.insets = new Insets(5, 5, 5, 5);
    gbc.anchor = GridBagConstraints.CENTER;

    bottomPanel.add(restartButton, gbc);

    gbc.gridy = 1;
    bottomPanel.add(newGameButton, gbc);

    panel.add(bottomPanel, BorderLayout.SOUTH);

    setContentPane(panel);
    revalidate();
}

public void exibirTelaDerrota() {
    // Painel principal com layout BorderLayout
    JPanel panel = new JPanel(new BorderLayout()) {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            // Carrega o GIF de fundo
            ImageIcon backgroundImage = new ImageIcon("image/screen3.jpg");
            g.drawImage(backgroundImage.getImage(), 0, 0, getWidth(), getHeight(), this);
        }
    };

    // Título "Você foi derrotado!" com a fonte personalizada e cor #abcc85
    JLabel titleLabel = new JLabel("Você foi derrotado!", SwingConstants.CENTER);
    titleLabel.setFont(customFont); // Usa a fonte carregada
    titleLabel.setForeground(new Color(0xABCC85)); // Cor #abcc85
    panel.add(titleLabel, BorderLayout.NORTH); // Título no topo (centro)

    // Painel para a animação de 6 frames
    JPanel animationPanel = new JPanel(new GridBagLayout()); // Usa GridBagLayout para centralizar
    animationPanel.setOpaque(false); // Torna o painel transparente

    // JLabel para exibir a animação
    JLabel animationLabel = new JLabel();
    animationLabel.setHorizontalAlignment(SwingConstants.CENTER);

    // Adiciona a animação ao painel de animação (centralizado)
    GridBagConstraints gbcAnimation = new GridBagConstraints();
    gbcAnimation.gridx = 0;
    gbcAnimation.gridy = 0;
    gbcAnimation.anchor = GridBagConstraints.CENTER;
    animationPanel.add(animationLabel, gbcAnimation);

    // Adiciona o painel de animação ao centro do painel principal
    panel.add(animationPanel, BorderLayout.CENTER);

    // Painel para os botões
    JPanel buttonPanel = new JPanel(new GridBagLayout());
    buttonPanel.setOpaque(false); // Torna o painel transparente

    // Botões de Reiniciar e Novo Jogo
    JButton restartButton = new JButton("Reiniciar Jogo");
    JButton newGameButton = new JButton("Novo Jogo");

    restartButton.addActionListener(e -> reiniciarJogo());
    newGameButton.addActionListener(e -> novoJogo());

    // Configuração do layout dos botões
    GridBagConstraints gbcButtons = new GridBagConstraints();
    gbcButtons.gridx = 0;
    gbcButtons.gridy = 0;
    gbcButtons.insets = new Insets(10, 10, 10, 10); // Espaçamento entre os botões
    gbcButtons.anchor = GridBagConstraints.CENTER;
    buttonPanel.add(restartButton, gbcButtons);

    gbcButtons.gridy = 1;
    buttonPanel.add(newGameButton, gbcButtons);

    // Adiciona o painel de botões ao painel principal (parte inferior)
    panel.add(buttonPanel, BorderLayout.SOUTH);

    // Configura o painel principal como conteúdo da janela
    setContentPane(panel);
    revalidate();

    // Inicia a animação de 6 frames
    startAnimation(animationLabel);
}

    // Método para iniciar a animação de 6 frames
private void startAnimation(JLabel animationLabel) {
    String[] frames = {
        "image/frame1.png", // Substitua pelos caminhos das suas imagens
        "image/frame2.png",
        "image/frame3.png",
        "image/frame4.png",
        "image/frame5.png",
        "image/frame6.png"
    };

    javax.swing.Timer animationTimer = new javax.swing.Timer(100, new ActionListener() { // 100ms por frame
        int currentFrame = 0;

        @Override
        public void actionPerformed(ActionEvent e) {
            if (currentFrame >= frames.length) {
                currentFrame = 0; // Reinicia a animação
            }
            // Atualiza a imagem do frame atual
            ImageIcon frameIcon = new ImageIcon(frames[currentFrame]);
            animationLabel.setIcon(frameIcon);
            currentFrame++;
        }
    });

    animationTimer.start(); // Inicia o timer da animação
}

private void reiniciarJogo() {
    // Remove qualquer KeyListener existente
    for (KeyListener listener : getKeyListeners()) {
        removeKeyListener(listener);
    }

    // Reinicia o estado do jogo
    jogador.setVida(100);
    jogador.setPosicao(new Point(0, 0));
    jogador.ganharArma(false);
    jogador.ganharTaco(false);
    inimigos.clear();
    inventario.clear();
    loadRandomMap();
    initGameInterface(); // Adiciona o KeyListener novamente
}

    private void novoJogo() {
        showWelcomeScreen();
    }

    private void iniciarCombate(Inimigo inimigo) {
        new CombatScreen(this, jogador, inimigo);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FrogForestGame().setVisible(true));
    }
}