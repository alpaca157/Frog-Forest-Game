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

    public CombatScreen(JFrame parent, Jogador jogador, Inimigo inimigo) {
        super(parent, "Combate", true);
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
                ImageIcon backgroundImage = new ImageIcon("imagens/combat_background.png"); // Substitua pelo caminho correto
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
        playerLabel.setIcon(new ImageIcon("imagens/player_combat.png")); // Substitua pelo caminho correto
        playerLabel.setHorizontalAlignment(SwingConstants.CENTER);

        enemyLabel = new JLabel();
        enemyLabel.setIcon(new ImageIcon("imagens/enemy_combat.png")); // Substitua pelo caminho correto
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
        animateAttack(playerLabel, () -> {
            int opcao = JOptionPane.showOptionDialog(
                    this,
                    "Escolha sua forma de ataque:",
                    "Atacar",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    new String[]{"Mão", "Revólver"},
                    "Mão"
            );

            if (opcao == JOptionPane.YES_OPTION) {
                // Ataque com a mão
                int dado = new Random().nextInt(6) + 1; // Rola um dado de 6 lados
                if (dado == 6) {
                    JOptionPane.showMessageDialog(this, "Golpe crítico! Você eliminou o inimigo.");
                    inimigo.setVida(0);
                } else {
                    inimigo.setVida(inimigo.getVida() - 20); // Dano normal
                }
            } else {
                // Ataque com revólver
                if (jogador.temArma()) {
                    if (inimigo instanceof Corredor) {
                        JOptionPane.showMessageDialog(this, "Não é possível vencer corredores com o revólver.");
                    } else if (inimigo instanceof Baiacu) {
                        JOptionPane.showMessageDialog(this, "Não é possível ferir o baiacu sem uma arma.");
                    } else {
                        JOptionPane.showMessageDialog(this, "Golpe crítico com revólver! Você eliminou o inimigo.");
                        inimigo.setVida(0);
                        jogador.usarMunicao();
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Sem arma no momento.");
                }
            }

            turnoInimigo();
            verificarFimCombate();
        });
    }

    private void curar() {
        animateHeal(playerLabel, () -> {
            jogador.setVida(Math.min(100, jogador.getVida() + 15));
            JOptionPane.showMessageDialog(this, "Você se curou!");
            turnoInimigo();
            verificarFimCombate();
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
                updateMapDisplay();
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
        });
    }

    private void verificarFimCombate() {
        atualizarBarras();

        if (inimigo.getVida() <= 0) {
            JOptionPane.showMessageDialog(this, "Você derrotou o inimigo!");
            FrogForestGame parent = (FrogForestGame) getParent();
            Point inimigoPosition = inimigo.getPosicao();

            // Substitui a posição do inimigo por um espaço vazio (' ')
            parent.getGameMap()[inimigoPosition.y][inimigoPosition.x] = ' ';
            parent.removerInimigo(inimigo);
            parent.updateMapDisplay();

            if (parent.verificarVitoria()) {
                parent.exibirTelaVitoria();
            }
            dispose(); // Fecha a tela de combate
        } else if (jogador.getVida() <= 0) {
            JOptionPane.showMessageDialog(this, "Você foi derrotado!");
            System.exit(0);
        }
    }

    private void atualizarBarras() {
        playerHealthBar.setValue(jogador.getVida());
        enemyHealthBar.setValue(inimigo.getVida());
    }

    private void animateAttack(JLabel character, Runnable onFinish) {
        int frames = 4;
        String[] attackFrames = {
                "imagens/attack_frame1.png",
                "imagens/attack_frame2.png",
                "imagens/attack_frame3.png",
                "imagens/attack_frame4.png"
        };

        animationTimer = new Timer();
        animationTimer.scheduleAtFixedRate(new TimerTask() {
            int currentFrame = 0;

            @Override
            public void run() {
                if (currentFrame >= frames) {
                    animationTimer.cancel();
                    SwingUtilities.invokeLater(onFinish);
                    character.setIcon(new ImageIcon("imagens/player_combat.png")); // Volta ao estado original
                } else {
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
                "imagens/heal_frame1.png",
                "imagens/heal_frame2.png",
                "imagens/heal_frame3.png",
                "imagens/heal_frame4.png"
        };

        animationTimer = new Timer();
        animationTimer.scheduleAtFixedRate(new TimerTask() {
            int currentFrame = 0;

            @Override
            public void run() {
                if (currentFrame >= frames) {
                    animationTimer.cancel();
                    SwingUtilities.invokeLater(onFinish);
                    character.setIcon(new ImageIcon("imagens/player_combat.png")); // Volta ao estado original
                } else {
                    ImageIcon frameIcon = new ImageIcon(healFrames[currentFrame]);
                    character.setIcon(frameIcon);
                    currentFrame++;
                }
            }
        }, 0, 100); // 100ms por frame
    }

    private boolean isValidMove(Point posicao) {
        FrogForestGame parent = (FrogForestGame) getParent();
        char[][] gameMap = parent.getGameMap();
        return posicao.x >= 0 && posicao.x < gameMap[0].length &&
               posicao.y >= 0 && posicao.y < gameMap.length &&
               gameMap[posicao.y][posicao.x] != '#';
    }

    private void updateMapDisplay() {
        FrogForestGame parent = (FrogForestGame) getParent();
        parent.updateMapDisplay();
    }
}