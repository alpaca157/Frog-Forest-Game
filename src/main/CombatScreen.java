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
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

class CombatScreen extends JDialog {
    private final Jogador jogador;
    private final Inimigo inimigo;
    private final JProgressBar playerHealthBar;
    private final JProgressBar enemyHealthBar;
    private final JLabel playerLabel;
    private final JLabel enemyLabel;
    private Timer animationTimer;
    private final FrogForestGame game;

    public CombatScreen(JFrame parent, Jogador jogador, Inimigo inimigo) {
        super(parent, "Combate", true);
        this.game = (FrogForestGame) parent;
        this.jogador = jogador;
        this.inimigo = inimigo;

        setSize(800, 600);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(parent);

        // Painel principal com imagem de fundo
        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon backgroundImage = new ImageIcon("image/screen2.jpg");
                g.drawImage(backgroundImage.getImage(), 0, 0, getWidth(), getHeight(), this);
            }
        };
        backgroundPanel.setLayout(new BorderLayout());
        setContentPane(backgroundPanel);

        // Painel superior para as vidas
        JPanel topPanel = new JPanel();
        topPanel.setOpaque(false);
        topPanel.setLayout(new GridLayout(2, 1));

        playerHealthBar = new JProgressBar(0, 100);
        playerHealthBar.setValue(jogador.getVida());
        playerHealthBar.setStringPainted(true);
        playerHealthBar.setForeground(Color.GREEN);

        enemyHealthBar = new JProgressBar(0, 100);
        enemyHealthBar.setValue(inimigo.getVida());
        enemyHealthBar.setStringPainted(true);
        enemyHealthBar.setForeground(Color.RED);

        topPanel.add(new JLabel("Vida do Jogador:", SwingConstants.CENTER));
        topPanel.add(playerHealthBar);
        topPanel.add(new JLabel("Vida do Inimigo:", SwingConstants.CENTER));
        topPanel.add(enemyHealthBar);

        add(topPanel, BorderLayout.NORTH);

        // Painel central para o jogador e o inimigo
        JPanel centerPanel = new JPanel();
        centerPanel.setOpaque(false);
        centerPanel.setLayout(new GridLayout(1, 2));

        playerLabel = new JLabel();
        playerLabel.setIcon(new ImageIcon("image/player.png")); // Imagem do jogador
        playerLabel.setHorizontalAlignment(SwingConstants.CENTER);

        enemyLabel = new JLabel();
        // Define a imagem do inimigo com base no tipo
        if (inimigo instanceof Perseguidor) {
            enemyLabel.setIcon(new ImageIcon("image/enemy1.png"));
        } else if (inimigo instanceof Estalador) {
            enemyLabel.setIcon(new ImageIcon("image/enemy2.png"));
        } else if (inimigo instanceof Corredor) {
            enemyLabel.setIcon(new ImageIcon("image/enemy3.png"));
        } else if (inimigo instanceof Baiacu) {
            enemyLabel.setIcon(new ImageIcon("image/enemy4.png"));
        }
        enemyLabel.setHorizontalAlignment(SwingConstants.CENTER);

        centerPanel.add(playerLabel);
        centerPanel.add(enemyLabel);

        add(centerPanel, BorderLayout.CENTER);

        // Painel inferior para os botões
        JPanel bottomPanel = new JPanel();
        bottomPanel.setOpaque(false);
        bottomPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));

        JButton attackButton = new JButton("Atacar");
        JButton healButton = new JButton("Curar");
        JButton fleeButton = new JButton("Fugir");

        attackButton.addActionListener(e -> atacar());
        healButton.addActionListener(e -> curar());
        fleeButton.addActionListener(e -> fugir());

        bottomPanel.add(attackButton);
        bottomPanel.add(healButton);
        bottomPanel.add(fleeButton);

        add(bottomPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

private void atacar() {
    if (inimigo.getVida() > 0) { // Verifica se o inimigo ainda está vivo
        animateAttack(playerLabel, () -> {
            // Lógica de combate após a animação
            int opcao = JOptionPane.showOptionDialog(
                    this,
                    "Escolha sua forma de ataque:",
                    "Atacar",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    new String[]{"Mão/Taco", "Revólver"},
                    "Mão/Taco"
            );

            int dano = 0;
            if (opcao == JOptionPane.YES_OPTION) {
                // Ataque com a mão ou taco
                int dado = new Random().nextInt(6) + 1;
                if (dado == 6) {
                    dano = inimigo.getVida(); // Golpe crítico elimina o inimigo
                    JOptionPane.showMessageDialog(this, "Golpe crítico! Você causou " + dano + " de dano.");
                } else {
                    dano = jogador.temTaco() ? 30 : 20; // Dano normal (20% com mão, 30% com taco)
                    JOptionPane.showMessageDialog(this, "Você causou " + dano + " de dano.");
                }
            } else {
                // Ataque com revólver
                if (jogador.temArma()) {
                    if (inimigo instanceof Corredor || inimigo instanceof Baiacu) {
                        JOptionPane.showMessageDialog(this, "Não é possível ferir este inimigo com o revólver.");
                    } else {
                        dano = inimigo.getVida();
                        JOptionPane.showMessageDialog(this, "Golpe crítico com revólver! Você causou " + dano + " de dano.");
                        jogador.usarMunicao();
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Sem arma no momento.");
                }
            }

            inimigo.setVida(inimigo.getVida() - dano);
            verificarFimCombate(); // Verifica se o inimigo morreu após o ataque

            if (inimigo.getVida() > 0) { // Se o inimigo ainda estiver vivo, ele ataca
                turnoInimigo();
            }
        });
    } else {
        verificarFimCombate(); // Se o inimigo já estiver morto, verifica o fim do combate
    }
}

private void curar() {
    animateHeal(playerLabel, () -> {
        jogador.setVida(Math.min(100, jogador.getVida() + 15));
        JOptionPane.showMessageDialog(this, "Você se curou!");
        turnoInimigo();
        verificarFimCombate();
        game.updateStatusPanel(); // Atualiza o painel de status
    });
}
    private void fugir() {
        Point posicaoAtual = jogador.getPosicao();
        Point[] posicoesAdjacentes = {
                new Point(posicaoAtual.x + 1, posicaoAtual.y),
                new Point(posicaoAtual.x - 1, posicaoAtual.y),
                new Point(posicaoAtual.x, posicaoAtual.y + 1),
                new Point(posicaoAtual.x, posicaoAtual.y - 1)
        };

        for (Point posicao : posicoesAdjacentes) {
            if (isValidMove(posicao)) {
                jogador.setPosicao(posicao);
                game.updateMapDisplay();
                dispose(); // Fecha a tela de combate
                return;
            }
        }
        JOptionPane.showMessageDialog(this, "Não há para onde fugir!");
    }

private void turnoInimigo() {
    animateAttack(enemyLabel, () -> {
        int danoInimigo = new Random().nextInt(10) + 10; // Dano aleatório entre 10 e 20
        jogador.setVida(jogador.getVida() - danoInimigo);
        JOptionPane.showMessageDialog(this, "O inimigo te atacou causando " + danoInimigo + " de dano!");
        verificarFimCombate();
        game.updateStatusPanel(); // Atualiza o painel de status
    });
}

private void verificarFimCombate() {
    atualizarBarras();

    if (inimigo.getVida() <= 0) {
        JOptionPane.showMessageDialog(this, "Você derrotou o inimigo!");
        Point inimigoPosition = inimigo.getPosicao();
        game.getGameMap()[inimigoPosition.y][inimigoPosition.x] = ' ';
        game.removerInimigo(inimigo);
        game.updateMapDisplay();
        game.updateStatusPanel(); // Atualiza o painel de status

        if (game.verificarVitoria()) {
            game.exibirTelaVitoria();
        }
        dispose(); // Fecha a tela de combate
    } else if (jogador.getVida() <= 0) {
        JOptionPane.showMessageDialog(this, "Você foi derrotado!");
        game.exibirTelaDerrota();
        dispose(); // Fecha a tela de combate
    }
}

    private void atualizarBarras() {
        playerHealthBar.setValue(jogador.getVida());
        enemyHealthBar.setValue(inimigo.getVida());
    }

private void animateAttack(JLabel character, Runnable onFinish) {
    int frames = 4;
    String[] attackFrames = {
        "image/player.png",  // Frame inicial (personagem parado)
        "image/player2.png", // Frame de ataque 1
        "image/player3.png", // Frame de ataque 2
        "image/player.png"   // Frame final (personagem parado)
    };

    animationTimer = new Timer();
    animationTimer.scheduleAtFixedRate(new TimerTask() {
        int currentFrame = 0;

        @Override
        public void run() {
            if (currentFrame >= frames) {
                animationTimer.cancel(); // Para o timer quando a animação termina
                SwingUtilities.invokeLater(() -> {
                    character.setIcon(new ImageIcon("image/player.png")); // Restaura o ícone original
                    onFinish.run(); // Executa o código após a animação
                });
            } else {
                // Atualiza o ícone do personagem com o frame atual
                ImageIcon frameIcon = new ImageIcon(attackFrames[currentFrame]);
                character.setIcon(frameIcon);
                currentFrame++;
            }
        }
    }, 0, 100); // 100ms por frame
}

    private void animateHeal(JLabel character, Runnable onFinish) {
        int frames = 4;
        String[] healFrames = {
                "image/player.png",
                "image/playerc1.png",
                "image/playerc2.png",
                "image/playerc3.png"
        };

        animationTimer = new Timer();
        animationTimer.scheduleAtFixedRate(new TimerTask() {
            int currentFrame = 0;

            @Override
            public void run() {
                if (currentFrame >= frames) {
                    animationTimer.cancel();
                    SwingUtilities.invokeLater(onFinish);
                    character.setIcon(new ImageIcon("image/player.png")); // Volta ao estado original
                } else {
                    ImageIcon frameIcon = new ImageIcon(healFrames[currentFrame]);
                    character.setIcon(frameIcon);
                    currentFrame++;
                }
            }
        }, 0, 100); // 100ms por frame
    }

    private boolean isValidMove(Point posicao) {
        char[][] gameMap = game.getGameMap();
        return posicao.x >= 0 && posicao.x < gameMap[0].length &&
               posicao.y >= 0 && posicao.y < gameMap.length &&
               gameMap[posicao.y][posicao.x] != '#';
    }
}