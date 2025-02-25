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

public class CombatScreen extends JDialog {
    private Jogador jogador;
    private Inimigo inimigo;
    private JProgressBar playerHealthBar;
    private JProgressBar enemyHealthBar;

public CombatScreen(JFrame parent, Jogador jogador, Inimigo inimigo) {
    super(parent, "Combate", true);
    this.jogador = jogador;
    this.inimigo = inimigo;

    setLayout(new BorderLayout());

    // Painel para as imagens
    JPanel imagePanel = new JPanel(new GridLayout(1, 2));
    JLabel playerImage = new JLabel(new ImageIcon("imagens/jogador.png"));
    JLabel enemyImage = new JLabel(new ImageIcon("imagens/" + getEnemyImageName(inimigo)));

    imagePanel.add(playerImage);
    imagePanel.add(enemyImage);
    add(imagePanel, BorderLayout.NORTH);

    // Barras de vida
    JPanel healthPanel = new JPanel(new GridLayout(2, 1));
    playerHealthBar = new JProgressBar(0, 100);
    playerHealthBar.setValue(jogador.getVida());
    playerHealthBar.setStringPainted(true);

    enemyHealthBar = new JProgressBar(0, 100);
    enemyHealthBar.setValue(inimigo.getVida());
    enemyHealthBar.setStringPainted(true);

    healthPanel.add(new JLabel("Vida do Jogador:"));
    healthPanel.add(playerHealthBar);
    healthPanel.add(new JLabel("Vida do Inimigo:"));
    healthPanel.add(enemyHealthBar);

    add(healthPanel, BorderLayout.CENTER);

    // Botões
    JPanel buttonPanel = new JPanel();
    JButton attackButton = new JButton("Atacar");
    JButton healButton = new JButton("Curar");
    JButton fleeButton = new JButton("Fugir");

    attackButton.addActionListener(e -> atacar());
    healButton.addActionListener(e -> curar());
    fleeButton.addActionListener(e -> fugir());

    buttonPanel.add(attackButton);
    buttonPanel.add(healButton);
    buttonPanel.add(fleeButton);

    add(buttonPanel, BorderLayout.SOUTH);

    pack();
    setLocationRelativeTo(parent);
    setVisible(true);
}

// Método auxiliar para obter o nome da imagem do inimigo
private String getEnemyImageName(Inimigo inimigo) {
    if (inimigo instanceof Perseguidor) {
        return "perseguidor.png";
    } else if (inimigo instanceof Estalador) {
        return "estalador.png";
    } else if (inimigo instanceof Corredor) {
        return "corredor.png";
    } else if (inimigo instanceof Baiacu) {
        return "baiacu.png";
    }
    return "background.png"; // Caso padrão
}

    private void atacar() {
        // Reduz a vida do inimigo em 20 pontos (golpe crítico)
        inimigo.setVida(inimigo.getVida() - 20);

        // Reduz a vida do jogador em 10 pontos (ataque do inimigo)
        jogador.setVida(jogador.getVida() - 10);

        // Atualiza as barras de vida
        atualizarBarras();

        // Verifica se o combate terminou
        verificarFimCombate();
    }

private void curar() {
    jogador.setVida(jogador.getVida() + 15); // Aumenta a vida do jogador
    atualizarBarras();
    verificarFimCombate();
}

    private void fugir() {
        // Fecha a tela de combate
        dispose();
    }

    private void atualizarBarras() {
        // Atualiza as barras de vida com os valores atuais
        playerHealthBar.setValue(jogador.getVida());
        enemyHealthBar.setValue(inimigo.getVida());
    }

    private void verificarFimCombate() {
        if (jogador.getVida() <= 0) {
            JOptionPane.showMessageDialog(this, "Você perdeu!");
            dispose(); // Fecha a tela de combate
        } else if (inimigo.getVida() <= 0) {
            JOptionPane.showMessageDialog(this, "Você venceu o combate!");
            dispose(); // Fecha a tela de combate
        }
    }
}
