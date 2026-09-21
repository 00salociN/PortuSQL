package Telas;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 * Card clicavel com cantos arredondados, icone circular colorido,
 * titulo em negrito e uma descricao opcional (usada nos dois cards
 * grandes da Homepage; os quatro cards pequenos usam so titulo).
 *
 * O card muda de tom levemente ao passar o mouse por cima (efeito
 * hover), e dispara a acao passada no construtor ao ser clicado.
 *
 * @author Nicolas Gabriel Venâncio dos Santos
 * @version 1.0
 */
public class CardPanel extends JPanel {

    private static final Color COR_FUNDO = new Color(0x27, 0x27, 0x27);
    private static final Color COR_FUNDO_HOVER = new Color(0x30, 0x30, 0x30);
    private static final Color COR_TITULO = new Color(0xF0, 0xF0, 0xF0);
    private static final Color COR_DESCRICAO = new Color(0xA0, 0xA0, 0xA0);
    private static final int RAIO_CANTO = 14;

    /** altura fixa dos cards sem descricao (os quatro pequenos) */
    private static final int ALTURA_SEM_DESCRICAO = 110;
    /** altura fixa dos cards com descricao (os dois grandes) */
    private static final int ALTURA_COM_DESCRICAO = 140;

    private Color corFundoAtual = COR_FUNDO;
    private final boolean temDescricao;
    private final JLabel labelIcone;
    private final JLabel labelTitulo;
    private final JLabel labelDescricao;

    /**
     * @param icone texto/simbolo exibido dentro do circulo colorido
     *              (ex: "+", "\u2699" para engrenagem, etc.)
     * @param corIcone cor de fundo do circulo do icone
     * @param titulo titulo do card, em negrito
     * @param descricao texto secundario abaixo do titulo -- passe
     *                  null ou "" para cards sem descricao (os quatro
     *                  cards pequenos da Homepage)
     * @param aoClicar acao executada quando o card for clicado
     */
    public CardPanel(String icone, Color corIcone, String titulo, String descricao, Runnable aoClicar) {
        setOpaque(false);
        setLayout(null); // posicionamento manual -- card tem geometria simples e fixa
        setCursor(new Cursor(Cursor.HAND_CURSOR));

        labelIcone = criarLabelIcone(icone, corIcone);
        add(labelIcone);

        labelTitulo = criarLabelTitulo(titulo);
        add(labelTitulo);

        this.temDescricao = descricao != null && !descricao.isEmpty();
        labelDescricao = criarLabelDescricao(descricao);
        if (temDescricao) {
            add(labelDescricao);
        }

        // Posicionamento manual dos componentes internos, recalculado
        // quando o card entra na tela e a cada mudanca de tamanho.
        addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent e) {
                posicionarConteudo(labelIcone, labelTitulo, labelDescricao, temDescricao);
            }
        });

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                corFundoAtual = COR_FUNDO_HOVER;
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                corFundoAtual = COR_FUNDO;
                repaint();
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                if (aoClicar != null) {
                    aoClicar.run();
                }
            }
        });
    }

    @Override
    public void addNotify() {
        super.addNotify();
        // garante que o conteudo interno e posicionado no primeiro
        // display, mesmo que nenhum resize tenha acontecido ainda
        posicionarConteudo(labelIcone, labelTitulo, labelDescricao, temDescricao);
    }

    private JLabel criarLabelIcone(String icone, Color corIcone) {
        JLabel label = new JLabel(icone, SwingConstants.CENTER) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(corIcone);
                g2.fillOval(0, 0, getWidth(), getHeight());
                g2.dispose();
                super.paintComponent(g);
            }
        };
        label.setForeground(Color.WHITE);
        label.setFont(new Font("SansSerif", Font.BOLD, 16));
        label.setSize(36, 36);
        return label;
    }

    private JLabel criarLabelTitulo(String titulo) {
        JLabel label = new JLabel(titulo);
        label.setForeground(COR_TITULO);
        label.setFont(new Font("SansSerif", Font.BOLD, 15));
        return label;
    }

    private JLabel criarLabelDescricao(String descricao) {
        JLabel label = new JLabel("<html><body style='width:260px'>" + descricao + "</body></html>");
        label.setForeground(COR_DESCRICAO);
        label.setFont(new Font("SansSerif", Font.PLAIN, 12));
        return label;
    }

    private void posicionarConteudo(JLabel icone, JLabel titulo, JLabel descricao, boolean temDescricao) {
        int margem = 20;
        icone.setBounds(margem, margem, 36, 36);

        titulo.setSize(getWidth() - margem * 2, 22);
        titulo.setLocation(margem, margem + 36 + 12);

        if (temDescricao && descricao != null) {
            descricao.setSize(getWidth() - margem * 2, 34);
            descricao.setLocation(margem, margem + 36 + 12 + 26);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(corFundoAtual);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), RAIO_CANTO, RAIO_CANTO);
        g2.dispose();
        super.paintComponent(g);
    }

    @Override
    public Dimension getPreferredSize() {
        return temDescricao
                ? new Dimension(260, ALTURA_COM_DESCRICAO)
                : new Dimension(260, ALTURA_SEM_DESCRICAO);
    }
}