/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Tela;

/**
 *
 * @author nicolas
 */
import javax.swing.*;

import javax.swing.*;

public class MenuDDL {
    public static void main(String[] args) {

        JFrame frame = new JFrame("Menu DDL");

        JMenuBar menuBar = new JMenuBar();

        JMenu ddlMenu = new JMenu("DDL");

        JMenuItem criarTabela = new JMenuItem("Criar tabela");
        JMenuItem criarIndex = new JMenuItem("Criar index");

        ddlMenu.add(criarTabela);
        ddlMenu.add(criarIndex);

        menuBar.add(ddlMenu);

        frame.setJMenuBar(menuBar);

        criarTabela.addActionListener(e -> {
            System.out.println("Ir para página Criar tabela");
        });

        criarIndex.addActionListener(e -> {
            System.out.println("Ir para página Criar index");
        });

        frame.setSize(400,300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}