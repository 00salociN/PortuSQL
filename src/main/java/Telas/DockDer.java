package Telas;

import Conexao.DiffDer;
import Conexao.MapaBanco;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.sql.SQLException;
import java.util.function.Consumer;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

/**
 * moldura do DER encaixada no split lateral do IdePanel.
 *
 * <p>cabecalho estilo aba da IDE: titulo do banco, botao de atualizar (↻),
 * botao de limpar destaque, botao de fechar (×) e um resumo do ultimo diff.
  * @version 1.1.0
 */
public class DockDer extends JPanel {

    private String banco;
    private final PainelDer painel = new PainelDer();
    private final JLabel titulo;
    private final JLabel resumo = new JLabel("Sem alterações recentes");
    private final JLabel aviso = new JLabel(" ");
    private final Consumer<DiffDer> aoMudarDestaque;

    public DockDer(String banco, Runnable aoFechar) {
        this(banco, aoFechar, diff -> { });
    }

    public DockDer(String banco, Runnable aoFechar, Consumer<DiffDer> aoMudarDestaque) {
        super(new BorderLayout());
        this.banco = banco;
        this.aoMudarDestaque = aoMudarDestaque == null ? diff -> { } : aoMudarDestaque;
        setBackground(Tema.FUNDO_PAINEL);
        setBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, Tema.BORDA));
        setMinimumSize(new java.awt.Dimension(280, 200));

        String nome = banco == null || banco.isBlank() ? "banco atual" : banco;
        titulo = new JLabel("DER — " + nome);
        titulo.setForeground(Tema.TEXTO);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 12));

        JPanel cabecalho = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        cabecalho.setBackground(Tema.FUNDO_PAINEL);
        cabecalho.setBorder(new EmptyBorder(2, 6, 2, 6));
        cabecalho.add(titulo);
        cabecalho.add(criarBotao("↻", "Atualizar o diagrama", this::atualizar));
        cabecalho.add(criarBotao("Limpar", "Remover os destaques", () -> {
            aplicarDestaque(null);
            aoMudarDestaque.accept(null);
        }));
        cabecalho.add(criarBotao("×", "Fechar o DER", aoFechar));

        resumo.setForeground(Tema.VERDE);
        resumo.setFont(new Font("SansSerif", Font.BOLD, 10));
        resumo.setBorder(new EmptyBorder(2, 12, 0, 6));

        aviso.setForeground(Tema.TEXTO_SECUNDARIO);
        aviso.setFont(new Font("SansSerif", Font.PLAIN, 10));
        aviso.setBorder(new EmptyBorder(0, 12, 4, 6));

        JPanel inferior = new JPanel(new BorderLayout());
        inferior.setBackground(Tema.FUNDO_PAINEL);
        inferior.add(resumo, BorderLayout.NORTH);
        inferior.add(aviso, BorderLayout.SOUTH);

        JPanel topo = new JPanel(new BorderLayout());
        topo.setBackground(Tema.FUNDO_PAINEL);
        topo.add(cabecalho, BorderLayout.NORTH);
        topo.add(inferior, BorderLayout.SOUTH);

        JScrollPane rolagem = new JScrollPane(painel);
        rolagem.setBorder(null);
        rolagem.getViewport().setBackground(Tema.FUNDO_EDITOR);

        add(topo, BorderLayout.NORTH);
        add(rolagem, BorderLayout.CENTER);
        atualizar();
    }

    private JButton criarBotao(String texto, String dica, Runnable acao) {
        JButton botao = new JButton(texto);
        botao.setToolTipText(dica);
        botao.setFocusable(false);
        botao.addActionListener(e -> acao.run());
        return botao;
    }

    /** recarrega os metadados e limpa qualquer destaque. */
    public void atualizar() {
        aplicarDestaque(null);
        aoMudarDestaque.accept(null);
    }

    /**
     * recarrega os metadados e aplica o destaque do ultimo comando.
     *
     * @param diff diferenca calculada entre o antes e o depois, ou null
     */
    public void aplicarDestaque(DiffDer diff) {
        try {
            painel.setMapa(MapaBanco.ler(banco));
            painel.setDestaque(diff);
            resumo.setText(diff == null || diff.vazio()
                    ? "Sem alterações recentes" : diff.resumo());
            aviso.setText("Arraste para navegar · roda do mouse para ampliar");
        } catch (SQLException erro) {
            painel.setMapa(MapaBanco.vazio(banco));
            painel.setDestaque(null);
            resumo.setText("Sem alterações recentes");
            aviso.setText("Não foi possível ler o banco: " + erro.getMessage());
        }
    }

    /** troca o banco inspecionado quando o usuario muda de database. */
    public void trocarBanco(String novoBanco) {
        String atual = banco == null ? "" : banco;
        String novo = novoBanco == null ? "" : novoBanco;
        if (atual.equals(novo)) {
            return;
        }
        banco = novoBanco;
        titulo.setText("DER — " + (novo.isBlank() ? "banco atual" : novo));
    }

    public PainelDer getPainel() {
        return painel;
    }

    public String getBanco() {
        return banco;
    }
}
