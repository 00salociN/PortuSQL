package PortuSQL.PortuSQL;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import Telas.FramePortuSQL;

/**
 * ponto de entrada principal do PortuSQL.
 *
 * <p>a janela principal ({@link FramePortuSQL}) abre com a Homepage e o editor
 * de consultas ja conectados: da Homepage voce clica no card "Bancos" (ou no
 * icone de banco da barra lateral) para abrir o editor, escreve a consulta em
 * PortuSQL e executa com F5 ou Ctrl+Enter.
 *
 * <p>a verificacao de atualizacoes roda em segundo plano e nunca impede a
 * abertura do programa.
 *
 * @author Nicolas Gabriel Venâncio dos Santos
 * @version 1.1.0
 */
public class App {

    /**
     * metodo principal que inicia a aplicacao
     * @param args argumentos recebidos na inicializacao
     */
    public static void main(String[] args) {
        Atualizador.verificarEmSegundoPlano(info ->
                SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(
                        null,
                        "PortuSQL " + info.versao() + " disponível.\n\nNotas: " + info.notas(),
                        "Atualização disponível",
                        JOptionPane.INFORMATION_MESSAGE)));

        SwingUtilities.invokeLater(() -> new FramePortuSQL().setVisible(true));
    }
}
