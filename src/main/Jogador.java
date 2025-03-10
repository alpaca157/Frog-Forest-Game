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

class Jogador {
    public int percepcao;
    private int vida;
    private Point posicao;
    private boolean temArma;
    private int municao;
    public boolean temTaco;

    public Jogador(int percepcao) {
        this.percepcao = percepcao;
        this.vida = 100;
        this.posicao = new Point(0, 0);
        this.temArma = false;
        this.municao = 0;
        this.temTaco = false;
    }

    public int getPercepcao() {
        return percepcao;
    }

    public int getVida() {
        return vida;
    }

    public void setVida(int vida) {
        this.vida = Math.max(0, Math.min(vida, 100));
    }

    public Point getPosicao() {
        return posicao;
    }

    public void setPosicao(Point posicao) {
        this.posicao = posicao;
    }

    public boolean temArma() {
        return temArma;
    }

    // Método modificado para aceitar um parâmetro booleano
    public void ganharArma(boolean temArma) {
        this.temArma = temArma;
    }

    // Método modificado para aceitar um parâmetro booleano
    public void ganharTaco(boolean temTaco) {
        this.temTaco = temTaco;
    }

    public void adicionarMunicao(int quantidade) {
        this.municao += quantidade;
    }

    public void usarMunicao() {
        if (this.temArma && this.municao > 0) {
            this.municao--;
        } else {
            System.out.println("Você não tem arma ou munição suficiente!");
        }
    }

    public int getMunicao() {
        return municao;
    }

    boolean temTaco() {
        return temTaco;
    }

    boolean ganharArma() {
        return temArma;
    }

    boolean ganharTaco() {
        return temTaco;
    }
}