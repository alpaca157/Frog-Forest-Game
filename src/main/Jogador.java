/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main;

/**
 *
 * @author Duda
 */

public class Jogador {
    private int percepcao;
    private int vida;

    public Jogador(int percepcao) {
        this.percepcao = percepcao;
        this.vida = 100;
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
}
