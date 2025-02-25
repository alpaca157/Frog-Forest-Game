/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main;

import java.awt.Point;

/**
 *
 * @author Duda
 */
public class Estalador extends Inimigo {
    public Estalador(Point posicao) {
        super(posicao, 2); // Vida inicial: 2
    }

    @Override
    public void mover(char[][] mapa, Point jogadorPosicao) {
        // Movimento simples em direção ao jogador
        int dx = Integer.compare(jogadorPosicao.x, posicao.x);
        int dy = Integer.compare(jogadorPosicao.y, posicao.y);
        Point novaPosicao = new Point(posicao.x + dx, posicao.y + dy);

        if (novaPosicao.x >= 0 && novaPosicao.x < mapa[0].length &&
            novaPosicao.y >= 0 && novaPosicao.y < mapa.length &&
            mapa[novaPosicao.y][novaPosicao.x] == ' ') {
            posicao = novaPosicao;
        }
    }
}
