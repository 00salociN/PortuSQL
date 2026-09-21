package Telas;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

/**
 * Barra lateral de navegação, com os 3 ícones (início, banco,
 * configurações) reaproveitada em todas as telas principais da
 * aplicação. Qual dos três aparece destacado (fundo verde com símbolo
 * azul) depende de qual tela está ativa no momento -- informado via
 * {@link IconeAtivo} no construtor.
 *
 * <p>A ordem dos ícones e as cores são as mesmas da Homepage: início no
 * topo, banco abaixo e configurações por último.
 *
 * @author Nicolas Gabriel Venâncio dos Santos
 * @version 1.0
 */
public class Sidebar extends JPanel {

    /** qual ícone deve aparecer destacado nesta tela */
    public enum IconeAtivo {
        BANCOS, INICIO, CONFIGURACOES, NENHUM
    }

    private static final Color COR_FUNDO = new Color(0x15, 0x15, 0x15);
    private static final Color COR_DESTAQUE = new Color(0x2E, 0xCC, 0x71);
    private static final Color COR_AZUL = new Color(0x34, 0x98, 0xDB);
    private static final Color COR_ICONE_INATIVO = Color.WHITE;

    public Sidebar(SidebarListener listener, IconeAtivo iconeAtivo) {
        setBackground(COR_FUNDO);
        setPreferredSize(new Dimension(46, 0));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(new EmptyBorder(12, 0, 0, 0));

        add(criarIcone("\u2302", iconeAtivo == IconeAtivo.INICIO,
                () -> disparar(listener != null ? listener::aoClicarInicio : null)));
        add(Box.createVerticalStrut(8));
        add(criarIcone("\uD83D\uDDC4", iconeAtivo == IconeAtivo.BANCOS,
                () -> disparar(listener != null ? listener::aoClicarBancos : null)));
        add(Box.createVerticalStrut(8));
        add(criarIcone("\u2699", iconeAtivo == IconeAtivo.CONFIGURACOES,
                () -> disparar(listener != null ? listener::aoClicarConfiguracoes : null)));

        add(Box.createVerticalGlue());
    }

    private JPanel criarIcone(String simbolo, boolean selecionado, Runnable aoClicar) {
        JPanel container = new JPanel();
        container.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        container.setOpaque(selecionado);
        container.setBackground(selecionado ? COR_DESTAQUE : COR_FUNDO);
        container.setMaximumSize(new Dimension(32, 32));
        container.setPreferredSize(new Dimension(32, 32));
        container.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        container.setCursor(new Cursor(Cursor.HAND_CURSOR));
        container.setAlignmentX(CENTER_ALIGNMENT);

        JLabel label = new JLabel(simbolo);
        label.setFont(new Font("SansSerif", Font.PLAIN, 16));
        // icone da tela ativa fica azul sobre o quadrado verde; os
        // demais ficam brancos sobre o fundo escuro
        label.setForeground(selecionado ? COR_AZUL : COR_ICONE_INATIVO);
        container.add(label);

        container.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                aoClicar.run();
            }
        });

        return container;
    }

    private void disparar(Runnable acao) {
        if (acao != null) {
            acao.run();
        }
    }
}