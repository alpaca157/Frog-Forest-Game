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
public class Baiacu extends Inimigo {
    public Baiacu(Point posicao, int vida) {
        super(posicao, vida);
    }

    @Override
    public void mover(char[][] mapa, Point jogadorPosicao) {
        // O baiacu não se move
    }
}