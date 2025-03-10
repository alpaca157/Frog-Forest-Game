/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main;

/**
 *
 * @author Duda
 */
import java.awt.Point;

abstract class Inimigo {
    public Point posicao;
    private int vida;

    public Inimigo(Point posicao, int vida) {
        this.posicao = posicao;
        this.vida = vida;
    }

    public Point getPosicao() {
        return posicao;
    }

    public void setPosicao(Point novaPosicao) {
        this.posicao = novaPosicao;
    }

    public int getVida() {
        return vida;
    }

    public void setVida(int vida) {
        this.vida = Math.max(0, vida);
    }

    public abstract void mover(char[][] mapa, Point jogadorPosicao);
}
