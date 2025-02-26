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

public abstract class Inimigo {
    protected Point posicao;
    protected int vida;

    public Inimigo(Point posicao, int vida) {
        this.posicao = posicao;
        this.vida = vida;
    }

    public Point getPosicao() {
    return posicao;
}

    public int getVida() {
        return vida;
    }

    public void setVida(int vida) {
        this.vida = Math.max(0, vida); // Vida não pode ser negativa
    }

    public abstract void mover(char[][] mapa, Point jogadorPosicao);

    void setPosicao(Point novaPosicao) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
