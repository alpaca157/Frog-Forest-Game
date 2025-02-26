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
import java.awt.Point;

class Item {
    private String nome;
    private Point posicao;

    public Item(String nome, Point posicao) {
        this.nome = nome;
        this.posicao = posicao;
    }

    public String getNome() {
        return nome;
    }

    public Point getPosicao() {
        return posicao;
    }
}
