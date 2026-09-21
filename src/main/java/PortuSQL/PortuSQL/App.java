package PortuSQL.PortuSQL;

import javax.swing.SwingUtilities;

import Telas.FramePortuSQL;

/**
 * ponto de entrada principal do PortuSQL.
 *
 * <p>abre a janela principal ({@link FramePortuSQL}) com a Homepage e o
 * editor de consultas ja conectados: da Homepage voce clica no card
 * "Bancos" (ou no iconinho de banco da barra lateral) para abrir o
 * editor, escreve a consulta em PortuSQL e executa com F5 ou
 * Ctrl+Enter.
 *
 * <p>quando a janela abre, o editor ja esta conectado ao banco de
 * testes dedicado {@code teste_portusql} -- o banco real do usuario
 * nunca e aberto sem pedir.
 *
 * @author Nicolas Gabriel Venâncio dos Santos
 * @version 1.0
 */
public class App {

    /**
     * metodo principal que inicia a aplicacao
     * @param args argumentos da linha de comando (nao utilizados)
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FramePortuSQL().setVisible(true));
    }
}