package Telas;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

/**
 * Tela inicial (Homepage) do PortuSQL: barra lateral de navegacao,
 * cabecalho com a logo do PortuSQL (imagem da pasta Imagens), dois
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

    // Cores dos icones dos cards, cada uma reaproveitando um tom
    // parecido com o logo do PortuSQL.
    private static final Color COR_VERDE = new Color(0x2E, 0xCC, 0x71);
    private static final Color COR_AZUL = new Color(0x34, 0x98, 0xDB);
    private static final Color COR_LARANJA = new Color(0xE6, 0x7E, 0x22);

    // Logo da Homepage: imagem da pasta Imagens, carregada do classpath.
    private static final String CAMINHO_LOGO = "/Imagens/Design sem nome(2).png";
    private static final int ALTURA_LOGO = 170;

    private final HomePanelListener listener;

    public HomePanel(HomePanelListener listener) {
        this.listener = listener;

        setLayout(new BorderLayout());
        setBackground(COR_FUNDO_PRINCIPAL);

        add(new Sidebar(listener, Sidebar.IconeAtivo.INICIO), BorderLayout.WEST);
        add(criarAreaConteudo(), BorderLayout.CENTER);
    }

    // ------------------------------------------------------------------
    // Area de conteudo (cabecalho com a logo + cards)
    // ------------------------------------------------------------------

    private JPanel criarAreaConteudo() {
        JPanel conteudo = new JPanel();
        conteudo.setBackground(COR_FUNDO_PRINCIPAL);
        conteudo.setLayout(new BoxLayout(conteudo, BoxLayout.Y_AXIS));
        conteudo.setBorder(new EmptyBorder(8, 24, 24, 32));

        conteudo.add(criarCabecalho());
        conteudo.add(Box.createVerticalStrut(24));
        conteudo.add(criarLinhaCardsPrincipais());
        conteudo.add(Box.createVerticalStrut(16));
        conteudo.add(criarLinhaCardsSecundarios());
        conteudo.add(Box.createVerticalGlue());

        return conteudo;
    }

    private JPanel criarCabecalho() {
        JPanel painel = new JPanel();
        painel.setLayout(new BoxLayout(painel, BoxLayout.Y_AXIS));
        painel.setBackground(COR_FUNDO_PRINCIPAL);
        painel.setAlignmentX(LEFT_ALIGNMENT);

        JComponent logo = criarLogo();
        logo.setAlignmentX(LEFT_ALIGNMENT);

        painel.add(logo);
        return painel;
    }

    /**
     * Logo do PortuSQL: usa a imagem da pasta {@code Imagens} quando
     * disponivel no classpath, com altura fixa e proporcao preservada.
     * Se o recurso nao puder ser carregado, cai para o logo em texto
     * colorido (mesmas cores de antes).
     */
    private JComponent criarLogo() {
        ImageIcon icone = carregarIconeLogo();
        if (icone != null) {
            return new JLabel(icone);
        }

        JPanel logoLinha = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 4));
        logoLinha.setBackground(COR_FUNDO_PRINCIPAL);
        logoLinha.add(criarPedacoLogo("Portu", COR_VERDE));
        logoLinha.add(criarPedacoLogo("S", COR_LARANJA));
        logoLinha.add(criarPedacoLogo("Q", COR_AZUL));
        logoLinha.add(criarPedacoLogo("L", COR_VERDE));
        return logoLinha;
    }

    /**
     * Carrega a logo da pasta Imagens e a redimensiona para caber no
     * cabecalho. Retorna {@code null} se o arquivo nao existir no
     * classpath ou nao puder ser decodificado.
     */
    private ImageIcon carregarIconeLogo() {
        try (InputStream entrada = HomePanel.class.getResourceAsStream(CAMINHO_LOGO)) {
            if (entrada == null) {
                return null;
            }
            BufferedImage original = ImageIO.read(entrada);
            if (original == null || original.getWidth() <= 0 || original.getHeight() <= 0) {
                return null;
            }
            int largura = Math.round(original.getWidth()
                    * ((float) ALTURA_LOGO / original.getHeight()));
            Image redimensionada = original.getScaledInstance(
                    largura, ALTURA_LOGO, Image.SCALE_SMOOTH);
            return new ImageIcon(redimensionada);
        } catch (IOException erro) {
            return null;
        }
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