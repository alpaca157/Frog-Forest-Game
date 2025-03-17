/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main;

import java.awt.Point;
import java.util.Set;

/**
 *
 * @author Duda
 */
class Corredor extends Inimigo {
    public Corredor(Point posicao, int vida) {
        super(posicao, vida);
    }

    @Override
    public void mover(char[][] mapa, Point jogadorPosicao, Set<Point> posicoesInimigos) {
        for (int i = 0; i < 2; i++) { // Move duas vezes
            super.mover(mapa, jogadorPosicao, posicoesInimigos);
        }
    }
}