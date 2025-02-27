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
public class main2 {
    public static void main(String[] args) {
        Jogador jogador = new Jogador(3); // Cria um jogador com percepção 3

        // Definindo posição inicial
        jogador.setPosicao(new Point(5, 5));
        System.out.println("Posição do jogador: " + jogador.getPosicao());

        // Vida do jogador
        jogador.setVida(80);
        System.out.println("Vida do jogador: " + jogador.getVida());

        // Arma e munição
        jogador.ganharArma();
        jogador.adicionarMunicao(10);
        System.out.println("Tem arma? " + jogador.temArma());
        System.out.println("Munição disponível: " + jogador.getMunicao());

        // Usando munição
        jogador.usarMunicao();
        System.out.println("Munição após uso: " + jogador.getMunicao());
    }
}