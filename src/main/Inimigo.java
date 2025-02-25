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
        this.vida = Math.max(0, vida); // A vida não pode ser negativa.
    }

public void mover(char[][] mapa, Point jogadorPosicao) {
    int dx = Integer.compare(jogadorPosicao.x, posicao.x);
    int dy = Integer.compare(jogadorPosicao.y, posicao.y);
    Point novaPosicao = new Point(posicao.x + dx, posicao.y + dy);

    // Verifica se a nova posição é válida (não é uma parede)
    if (novaPosicao.x >= 0 && novaPosicao.x < mapa[0].length &&
        novaPosicao.y >= 0 && novaPosicao.y < mapa.length &&
        mapa[novaPosicao.y][novaPosicao.x] == 'V') { // Substituído ' ' por 'V' para caminho livre
        posicao = novaPosicao;
    }
  }
}
