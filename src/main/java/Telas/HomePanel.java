package Telas;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

/**
 * Tela inicial (Homepage) do PortuSQL: barra lateral de navegacao,
 * breadcrumb, cabecalho de boas-vindas com a logo colorida, dois
 * cards de acao principais e quatro cards secundarios de apoio.
 *
 * A logica de navegacao real (trocar de tela ao clicar em cada
 * elemento) e delegada para um {@link HomePanelListener}, passado no
 * construtor -- este painel nao sabe nada sobre o resto da aplicacao,
 * so avisa "isso foi clicado" e deixa quem o criou decidir o que
 * fazer (normalmente, trocar o card ativo de um CardLayout).
 *
 * @author Nicolas Gabriel Venâncio dos Santos
 * @version 1.0
 */
public class HomePanel extends JPanel {

    // Paleta de cores do tema escuro, baseada na tela de referencia.
    private static final Color COR_FUNDO_PRINCIPAL = new Color(0x1E, 0x1E, 0x1E);
    private static final Color COR_TEXTO_TITULO = new Color(0xF5, 0xF5, 0xF5);
    private static final Color COR_TEXTO_DESCRICAO = new Color(0xA0, 0xA0, 0xA0);
    private static final Color COR_TEXTO_BREADCRUMB = new Color(0x90, 0x90, 0x90);

    // Cores dos icones dos cards, cada uma reaproveitando um tom
    // parecido com o logo do PortuSQL.
    private static final Color COR_VERDE = new Color(0x2E, 0xCC, 0x71);
    private static final Color COR_AZUL = new Color(0x34, 0x98, 0xDB);
    private static final Color COR_LARANJA = new Color(0xE6, 0x7E, 0x22);

    private final HomePanelListener listener;

    public HomePanel(HomePanelListener listener) {
        this.listener = listener;

        setLayout(new BorderLayout());
        setBackground(COR_FUNDO_PRINCIPAL);

        add(new Sidebar(listener, Sidebar.IconeAtivo.INICIO), BorderLayout.WEST);
        add(criarAreaConteudo(), BorderLayout.CENTER);
    }

    // ------------------------------------------------------------------
    // Area de conteudo (breadcrumb + boas-vindas + cards)
    // ------------------------------------------------------------------

    private JPanel criarAreaConteudo() {
        JPanel conteudo = new JPanel();
        conteudo.setBackground(COR_FUNDO_PRINCIPAL);
        conteudo.setLayout(new BoxLayout(conteudo, BoxLayout.Y_AXIS));
        conteudo.setBorder(new EmptyBorder(0, 32, 24, 32));

        conteudo.add(criarBreadcrumb());
        conteudo.add(Box.createVerticalStrut(24));
        conteudo.add(criarCabecalhoBoasVindas());
        conteudo.add(Box.createVerticalStrut(24));
        conteudo.add(criarLinhaCardsPrincipais());
        conteudo.add(Box.createVerticalStrut(16));
        conteudo.add(criarLinhaCardsSecundarios());
        conteudo.add(Box.createVerticalGlue());

        return conteudo;
    }

    private JPanel criarBreadcrumb() {
        JPanel painel = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 12));
        painel.setBackground(COR_FUNDO_PRINCIPAL);
        painel.setAlignmentX(LEFT_ALIGNMENT);

        JLabel inicio = new JLabel("\u2302 In\u00edcio");
        inicio.setForeground(new Color(0x5D, 0xAD, 0xE2));
        inicio.setFont(new Font("SansSerif", Font.BOLD, 12));
        inicio.setCursor(new Cursor(Cursor.HAND_CURSOR));
        inicio.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                disparar(listener != null ? listener::aoClicarInicio : null);
            }
        });

        JLabel separador = new JLabel(">");
        separador.setForeground(COR_TEXTO_BREADCRUMB);

        JLabel bancos = new JLabel("\uD83D\uDDC4 Bancos");
        bancos.setForeground(Color.WHITE);
        bancos.setFont(new Font("SansSerif", Font.PLAIN, 12));
        bancos.setCursor(new Cursor(Cursor.HAND_CURSOR));
        bancos.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                disparar(listener != null ? listener::aoClicarBancos : null);
            }
        });

        painel.add(inicio);
        painel.add(separador);
        painel.add(bancos);
        return painel;
    }

    private JPanel criarCabecalhoBoasVindas() {
        JPanel painel = new JPanel();
        painel.setLayout(new BoxLayout(painel, BoxLayout.Y_AXIS));
        painel.setBackground(COR_FUNDO_PRINCIPAL);
        painel.setAlignmentX(LEFT_ALIGNMENT);

        JLabel bemVindos = new JLabel("Bem-vindos");
        bemVindos.setForeground(COR_TEXTO_TITULO);
        bemVindos.setFont(new Font("SansSerif", Font.BOLD, 30));
        bemVindos.setAlignmentX(LEFT_ALIGNMENT);

        JPanel logoLinha = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 4));
        logoLinha.setBackground(COR_FUNDO_PRINCIPAL);
        logoLinha.setAlignmentX(LEFT_ALIGNMENT);
        logoLinha.add(criarPedacoLogo("Portu", COR_VERDE));
        logoLinha.add(criarPedacoLogo("S", COR_LARANJA));
        logoLinha.add(criarPedacoLogo("Q", COR_AZUL));
        logoLinha.add(criarPedacoLogo("L", COR_VERDE));

        JLabel descricao = new JLabel("<html><body style='width:640px'>"
                + "PortuSQL \u00e9 uma ferramenta pensada para estudantes: traz a sintaxe SQL para "
                + "perto do portugu\u00eas, tornando a aprendizagem mais natural e intuitiva. Explore "
                + "consultas, crie bancos e pratique comandos de forma clara e organizada."
                + "</body></html>");
        descricao.setForeground(COR_TEXTO_DESCRICAO);
        descricao.setFont(new Font("SansSerif", Font.PLAIN, 13));
        descricao.setAlignmentX(LEFT_ALIGNMENT);
        descricao.setBorder(new EmptyBorder(8, 0, 0, 0));

        painel.add(bemVindos);
        painel.add(logoLinha);
        painel.add(descricao);
        return painel;
    }

    private JLabel criarPedacoLogo(String texto, Color cor) {
        JLabel label = new JLabel(texto);
        label.setFont(new Font("SansSerif", Font.BOLD, 26));
        label.setForeground(cor);
        return label;
    }

    private JPanel criarLinhaCardsPrincipais() {
        JPanel painel = new JPanel(new GridLayout(1, 2, 16, 0));
        painel.setBackground(COR_FUNDO_PRINCIPAL);
        painel.setAlignmentX(LEFT_ALIGNMENT);
        painel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 140));

        painel.add(new CardPanel("+", COR_VERDE, "Novo banco",
                "Crie um novo banco de dados e comece a organizar suas tabelas e consultas.",
                () -> disparar(listener != null ? listener::aoClicarNovoBanco : null)));

        painel.add(new CardPanel("\u2699", COR_AZUL, "Configura\u00e7\u00f5es",
                "Ajuste prefer\u00eancias, conex\u00f5es e comportamentos para sua jornada de aprendizado.",
                () -> disparar(listener != null ? listener::aoClicarConfiguracoes : null)));

        return painel;
    }

    private JPanel criarLinhaCardsSecundarios() {
        JPanel painel = new JPanel(new GridLayout(1, 4, 16, 0));
        painel.setBackground(COR_FUNDO_PRINCIPAL);
        painel.setAlignmentX(LEFT_ALIGNMENT);
        painel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 130));

        painel.add(new CardPanel("\uD83D\uDCA1", COR_VERDE, "Sugest\u00f5es", null,
                () -> disparar(listener != null ? listener::aoClicarSugestoes : null)));

        painel.add(new CardPanel("?", COR_AZUL, "D\u00favidas", null,
                () -> disparar(listener != null ? listener::aoClicarDuvidas : null)));

        painel.add(new CardPanel("<>", COR_VERDE, "Comandos", null,
                () -> disparar(listener != null ? listener::aoClicarComandos : null)));

        painel.add(new CardPanel("\uD83D\uDC65", COR_AZUL, "Quem somos", null,
                () -> disparar(listener != null ? listener::aoClicarQuemSomos : null)));

        return painel;
    }

    private void disparar(Runnable acao) {
        if (acao != null) {
            acao.run();
        }
    }
}