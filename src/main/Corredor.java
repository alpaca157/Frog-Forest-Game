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
public class Corredor extends Inimigo {
    public Corredor(Point posicao, int vida) {
        super(posicao, vida);
    }

    @Override
    public void mover(char[][] mapa, Point jogadorPosicao) {
        for (int i = 0; i < 2; i++) { // Move duas vezes
            int dx = Integer.compare(jogadorPosicao.x, posicao.x);
            int dy = Integer.compare(jogadorPosicao.y, posicao.y);
            Point novaPosicao = new Point(posicao.x + dx, posicao.y + dy);

            if (novaPosicao.x >= 0 && novaPosicao.x < mapa[0].length &&
                novaPosicao.y >= 0 && novaPosicao.y < mapa.length &&
                mapa[novaPosicao.y][novaPosicao.x] == 'V') { // 'V' representa caminho livre
                posicao = novaPosicao;
            }
        }
    }
}
