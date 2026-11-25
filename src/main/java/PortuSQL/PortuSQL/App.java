package PortuSQL.PortuSQL;

import java.util.function.Consumer;

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
 * abertura do programa. Enquanto a janela fica aberta, a consulta se repete
 * periodicamente.
 *
 * @author Nicolas Gabriel Venâncio dos Santos
 * @version 1.1.1
 */
public class App {

    /**
     * metodo principal que inicia a aplicacao
     * @param args argumentos recebidos na inicializacao
     */
    public static void main(String[] args) {
        Consumer<Atualizador.InfoVersao> aviso = info ->
                SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(
                        null,
                        "PortuSQL " + info.versao() + " disponível.\n\nNotas: " + info.notas(),
                        "Atualização disponível",
                        JOptionPane.INFORMATION_MESSAGE));

        Atualizador.verificarEmSegundoPlano(aviso);
        Atualizador.verificarEnquantoAberto(aviso);

        SwingUtilities.invokeLater(() -> new FramePortuSQL().setVisible(true));
    }
}
