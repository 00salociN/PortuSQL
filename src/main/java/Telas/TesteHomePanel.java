package Telas;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import java.awt.BorderLayout;

/**
 * Programa de teste manual, so para visualizar a HomePanel isolada,
 * sem precisar integrar no resto da aplicacao ainda. Cada clique
 * imprime no console qual acao foi disparada.
 */
public class TesteHomePanel {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Tema.aplicar();
            JFrame frame = new JFrame("PortuSQL");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(980, 560);
            frame.setLocationRelativeTo(null);

            HomePanel homePanel = new HomePanel(new HomePanelListener() {
                @Override
                public void aoClicarBancos() {
                    System.out.println("Clicou em: Bancos");
                }

                @Override
                public void aoClicarInicio() {
                    System.out.println("Clicou em: Inicio");
                }

                @Override
                public void aoClicarConfiguracoes() {
                    System.out.println("Clicou em: Configuracoes");
                }

                @Override
                public void aoClicarNovoBanco() {
                    System.out.println("Clicou em: Novo banco");
                }

                @Override
                public void aoClicarSugestoes() {
                    System.out.println("Clicou em: Sugestoes");
                }

                @Override
                public void aoClicarDuvidas() {
                    System.out.println("Clicou em: Duvidas");
                }

                @Override
                public void aoClicarComandos() {
                    System.out.println("Clicou em: Comandos");
                }

                @Override
                public void aoClicarQuemSomos() {
                    System.out.println("Clicou em: Quem somos");
                }
            });

            frame.getContentPane().add(homePanel, BorderLayout.NORTH);
            frame.setVisible(true);
        });
    }
}