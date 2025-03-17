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
import java.util.*;

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

public void mover(char[][] mapa, Point jogadorPosicao, Set<Point> posicoesInimigos) {
    posicao = buscarCaminho(mapa, jogadorPosicao, posicoesInimigos);
}

private Point buscarCaminho(char[][] mapa, Point jogadorPosicao, Set<Point> posicoesInimigos) {
    Queue<Point> fila = new LinkedList<>();
    Map<Point, Point> caminho = new HashMap<>();
    Set<Point> visitados = new HashSet<>();

    fila.add(posicao);
    visitados.add(posicao);

    int[][] direcoes = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};

    while (!fila.isEmpty()) {
        Point atual = fila.poll();

        if (atual.equals(jogadorPosicao)) break;

        for (int[] dir : direcoes) {
            Point vizinho = new Point(atual.x + dir[0], atual.y + dir[1]);

            if (ehValido(mapa, vizinho, posicoesInimigos) && !visitados.contains(vizinho)) {
                fila.add(vizinho);
                visitados.add(vizinho);
                caminho.put(vizinho, atual);
            }
        }
    }

    return reconstruirCaminho(caminho, jogadorPosicao);
}

private boolean ehValido(char[][] mapa, Point p, Set<Point> posicoesInimigos) {
    return p.x >= 0 && p.x < mapa[0].length &&
           p.y >= 0 && p.y < mapa.length &&
           (mapa[p.y][p.x] == 'V' || mapa[p.y][p.x] == 'P') && // 'V' = caminho livre, 'P' = jogador
           !posicoesInimigos.contains(p); // Evita colisões com outros inimigos
}

private Point reconstruirCaminho(Map<Point, Point> caminho, Point destino) {
    if (!caminho.containsKey(destino)) return posicao; // Retorna a posição atual se não houver caminho

    Point passo = destino;
    while (caminho.get(passo) != null && !caminho.get(passo).equals(posicao)) {
        passo = caminho.get(passo);
    }

    return passo;
}
}
