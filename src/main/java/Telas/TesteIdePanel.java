package Telas;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 * Programa de teste manual, so para visualizar a {@link IdePanel}
 * isolada, sem precisar integrar no resto da aplicacao ainda. Cada
 * clique imprime no console qual acao foi disparada.
 */
public class TesteIdePanel {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Tema.aplicar();
            JFrame frame = new JFrame("PortuSQL");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1024, 600);
            frame.setLocationRelativeTo(null);

            IdePanel idePanel = new IdePanel(new IdePanelListener() {
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
                public void aoExecutar(String sql) {
                    System.out.println("Executar (F5). SQL:\n" + sql);
                }

                @Override
                public void aoExecutarUnica(String sql) {
                    System.out.println("Execucao unica (F6). SQL:\n" + sql);
                }

                @Override
                public void aoParar() {
                    System.out.println("Parar");
                }

                @Override
                public void aoSalvar() {
                    System.out.println("Salvar");
                }

                @Override
                public void aoAbrirHistorico() {
                    System.out.println("Abrir historico");
                }
            });

            frame.add(idePanel);
            frame.setVisible(true);
        });
    }
}