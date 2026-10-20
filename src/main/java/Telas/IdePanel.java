package Telas;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.JTree;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import Conexao.Conexao;

/**
 * Tela principal do editor de consultas do PortuSQL: barra de menu,
 * barra de ferramentas (Executar/Execução única/Parar/Salvar/
 * Histórico), navegador de bancos em árvore (com as pastas de tabelas,
 * visões, funções e gatilhos carregadas sob demanda), abas de editor SQL com realce de sintaxe leve,
 * numeração de linha e atalhos (Ctrl+Enter, F5, F6), e painel de
 * resultados/mensagens na parte inferior.
 *
 * <p>toda a lógica real (executar consulta no motor PortuSQL, salvar
 * arquivo, etc.) fica com o {@link IdePanelListener} -- este painel só
 * monta a interface e repassa o texto do editor ativo para o ouvinte.
 *
 * @author Nicolas Gabriel Venâncio dos Santos
 * @version 1.0
 */
public class IdePanel extends JPanel {

    private static final Color COR_FUNDO_PRINCIPAL = new Color(0x1E, 0x1E, 0x1E);
    private static final Color COR_FUNDO_PAINEL = new Color(0x25, 0x25, 0x25);
    private static final Color COR_FUNDO_EDITOR = new Color(0x1A, 0x1A, 0x1A);
    private static final Color COR_TEXTO = new Color(0xE0, 0xE0, 0xE0);
    private static final Color COR_TEXTO_SECUNDARIO = new Color(0x90, 0x90, 0x90);
    private static final Color COR_BORDA = new Color(0x33, 0x33, 0x33);
    private static final Color COR_SELECAO = new Color(0x2A, 0x3A, 0x4A);

    private static final Color COR_VERDE = new Color(0x2E, 0xCC, 0x71);
    private static final Color COR_AZUL = new Color(0x34, 0x98, 0xDB);
    private static final Color COR_VERMELHO = new Color(0xE7, 0x4C, 0x3C);

    // cores do realce de sintaxe
    private static final Color COR_PALAVRA_RESERVADA = new Color(0xC6, 0x78, 0xDD);
    private static final Color COR_TIPO_DADO = new Color(0x4F, 0xC1, 0xFF);
    private static final Color COR_LITERAL = new Color(0x98, 0xC3, 0x79);
    private static final Color COR_NUMERO = new Color(0xD1, 0x9A, 0x66);

    /** palavras reservadas do PortuSQL usadas no realce de sintaxe */
    private static final Set<String> PALAVRAS_RESERVADAS = Set.of(
            "criar", "tabela", "selecionar", "de", "onde", "ordenar", "por", "agrupar",
            "inserir", "em", "valores", "atualizar", "definir", "deletar", "apagar",
            "e", "ou", "nao", "nulo", "chave", "primaria", "estrangeira", "alterar",
            "adicionar", "coluna", "renomear", "restricao", "remover", "excluir",
            "mostrar", "esvaziar", "para", "referencia", "usar", "banco", "dados",
            "verificar", "caractere", "fixo", "variavel");

    /** tipos de dado do PortuSQL usados no realce de sintaxe */
    private static final Set<String> TIPOS_DADO = Set.of(
            "inteiro", "int", "texto", "data", "decimal", "flutuante", "booleano",
            "hora", "data_hora", "binario", "enum", "enumerar", "real", "numerico");

    /** texto entre aspas, numeros e palavras (usados no realce) */
    private static final Pattern PADRAO_TOKENS = Pattern.compile(
            "'[^']*'|\\b\\d+\\.?\\d*\\b|\\b\\p{L}[\\p{L}\\d_]*\\b");

    /** paginas do GitHub abertas pelo menu Ajuda */
    private static final String URL_GITHUB = "https://github.com/00salociN/PortuSQL";

    private final IdePanelListener listener;

    private JTabbedPane abasEditor;
    private final List<JTextPane> editores = new ArrayList<>();
    private JTabbedPane abasResultados;
    private JTextArea areaResultados;
    private JTextArea areaMensagens;
    private JTree arvore;
    private JLabel statusEsquerda;
    private JLabel statusDireita;

    /** contador usado para nomear os arquivos novos do menu Arquivo */
    private int proximoArquivo = 1;

    /** rotulo dos nos-pasta do navegador de bancos */
    private static final String PASTA_TABELAS = "Tabelas";
    private static final String PASTA_VISOES = "Vis\u00f5es";
    private static final String PASTA_FUNCOES = "Fun\u00e7\u00f5es";
    private static final String PASTA_GATILHOS = "Gatilhos";

    /** placeholder que vira o conteudo real quando um no e expandido */
    private static final String AVISO_CARREGANDO = "carregando...";

    public IdePanel(IdePanelListener listener) {
        this.listener = listener;

        setLayout(new BorderLayout());
        setBackground(COR_FUNDO_PRINCIPAL);
        registrarAtalhos();

        JPanel topo = new JPanel(new BorderLayout());
        topo.add(criarMenuBar(), BorderLayout.NORTH);
        topo.add(criarBarraFerramentas(), BorderLayout.SOUTH);

        JPanel areaComSidebar = new JPanel(new BorderLayout());
        areaComSidebar.add(new Sidebar(listener, Sidebar.IconeAtivo.BANCOS), BorderLayout.WEST);
        areaComSidebar.add(criarAreaPrincipal(), BorderLayout.CENTER);

        add(topo, BorderLayout.NORTH);
        add(areaComSidebar, BorderLayout.CENTER);
        add(criarBarraStatus(), BorderLayout.SOUTH);
    }

    // ------------------------------------------------------------------
    // Menu superior
    // ------------------------------------------------------------------

    private JMenuBar criarMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(COR_FUNDO_PAINEL);
        menuBar.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, COR_BORDA));

        menuBar.add(criarMenuArquivo());
        menuBar.add(criarMenuEditar());
        menuBar.add(criarMenuConsulta());
        menuBar.add(criarMenuBanco());
        menuBar.add(criarMenuFerramentas());
        menuBar.add(criarMenuAjuda());

        return menuBar;
    }

    private JMenu novoMenu(String titulo) {
        JMenu menu = new JMenu(titulo);
        menu.setForeground(COR_TEXTO);
        menu.setFont(new Font("SansSerif", Font.PLAIN, 12));
        return menu;
    }

    private JMenuItem itemMenu(String texto, KeyStroke atalho, Runnable acao) {
        JMenuItem item = new JMenuItem(texto);
        if (atalho != null) {
            item.setAccelerator(atalho);
        }
        item.addActionListener(e -> acao.run());
        return item;
    }

    private JMenu criarMenuArquivo() {
        JMenu menu = novoMenu("Arquivo");
        menu.add(itemMenu("Novo arquivo",
                KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK),
                this::novoEditor));
        menu.add(itemMenu("Fechar aba",
                KeyStroke.getKeyStroke(KeyEvent.VK_W, InputEvent.CTRL_DOWN_MASK),
                this::fecharAba));
        menu.addSeparator();
        menu.add(itemMenu("Salvar",
                KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK),
                () -> disparar(listener != null ? listener::aoSalvar : null)));
        menu.addSeparator();
        menu.add(itemMenu("Sair", null, () -> System.exit(0)));
        return menu;
    }

    private JMenu criarMenuEditar() {
        JMenu menu = novoMenu("Editar");
        menu.add(itemMenu("Recortar",
                KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.CTRL_DOWN_MASK),
                () -> noEditorAtivo(JTextPane::cut)));
        menu.add(itemMenu("Copiar",
                KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_DOWN_MASK),
                () -> noEditorAtivo(JTextPane::copy)));
        menu.add(itemMenu("Colar",
                KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.CTRL_DOWN_MASK),
                () -> noEditorAtivo(JTextPane::paste)));
        menu.add(itemMenu("Selecionar tudo",
                KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.CTRL_DOWN_MASK),
                () -> noEditorAtivo(JTextPane::selectAll)));
        return menu;
    }

    private JMenu criarMenuConsulta() {
        JMenu menu = novoMenu("Consulta");
        menu.add(itemMenu("Executar",
                KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0),
                () -> executarAtivo(false)));
        menu.add(itemMenu("Executar linha atual",
                KeyStroke.getKeyStroke(KeyEvent.VK_F6, 0),
                () -> executarAtivo(true)));
        menu.add(itemMenu("Parar execução", null,
                () -> disparar(listener != null ? listener::aoParar : null)));
        menu.addSeparator();
        menu.add(itemMenu("Limpar resultados", null, this::limparResultados));
        menu.add(itemMenu("Histórico de execuções",
                KeyStroke.getKeyStroke(KeyEvent.VK_H, InputEvent.CTRL_DOWN_MASK),
                () -> disparar(listener != null ? listener::aoAbrirHistorico : null)));
        return menu;
    }

    private JMenu criarMenuBanco() {
        JMenu menu = novoMenu("Banco");
        menu.add(itemMenu("Novo banco de dados...",
                KeyStroke.getKeyStroke(KeyEvent.VK_N,
                        InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK),
                this::novoBanco));
        menu.add(itemMenu("Abrir banco de dados...", null, this::abrirBanco));
        menu.add(itemMenu("Excluir banco de dados...", null, this::excluirBanco));
        menu.addSeparator();
        menu.add(itemMenu("Atualizar navegador de bancos", null, this::recarregarNavegador));
        return menu;
    }

    private JMenu criarMenuFerramentas() {
        JMenu menu = novoMenu("Ferramentas");
        menu.add(itemMenu("Abrir pasta dos bancos", null, this::abrirPastaBancos));
        menu.addSeparator();
        menu.add(itemMenu("Configurações...", null,
                () -> mostrarAviso("Configurações em construção.")));
        return menu;
    }

    private JMenu criarMenuAjuda() {
        JMenu menu = novoMenu("Ajuda");
        menu.add(itemMenu("Atalhos de teclado", null, this::mostrarAtalhos));
        menu.addSeparator();
        menu.add(itemMenu("Comandos (GitHub)", null,
                () -> abrirGitHub(URL_GITHUB)));
        menu.add(itemMenu("Sugestões (Issues no GitHub)", null,
                () -> abrirGitHub(URL_GITHUB + "/issues")));
        menu.add(itemMenu("Dúvidas (Discussões no GitHub)", null,
                () -> abrirGitHub(URL_GITHUB + "/discussions")));
        menu.addSeparator();
        menu.add(itemMenu("Sobre o PortuSQL", null, this::mostrarSobre));
        return menu;
    }

    // ------------------------------------------------------------------
    // Acoes dos menus
    // ------------------------------------------------------------------

    private void novoEditor() {
        abasEditor.addTab("novo" + proximoArquivo + ".sql", criarPainelEditorSql(""));
        abasEditor.setSelectedIndex(abasEditor.getTabCount() - 1);
        proximoArquivo++;
    }

    private void fecharAba() {
        if (abasEditor.getTabCount() <= 1) {
            mostrarAviso("Deixe ao menos uma aba de editor aberta.");
            return;
        }
        int indice = abasEditor.getSelectedIndex();
        if (indice < 0) {
            return;
        }
        editores.remove(indice);
        abasEditor.removeTabAt(indice);
        JTextPane novoAtivo = editorAtivo();
        if (novoAtivo != null) {
            novoAtivo.requestFocusInWindow();
        }
    }

    private void noEditorAtivo(Consumer<JTextPane> acao) {
        JTextPane editor = editorAtivo();
        if (editor != null) {
            acao.accept(editor);
        }
    }

    private void limparResultados() {
        areaResultados.setText("");
        areaMensagens.setText("");
        exibirMensagem("Resultados limpos.");
    }

    private void novoBanco() {
        String nome = JOptionPane.showInputDialog(this,
                "Nome do novo banco de dados:", "Novo banco",
                JOptionPane.PLAIN_MESSAGE);
        if (nome == null || nome.trim().isEmpty()) {
            return;
        }
        String limpo = nome.trim();
        try {
            Conexao.obterConexao(limpo);
            recarregarNavegador();
            exibirMensagem("Banco '" + limpo + "' criado e em uso.");
        } catch (SQLException erro) {
            mostrarAviso("Não foi possível criar o banco: " + erro.getMessage());
        }
    }

    private void abrirBanco() {
        List<String> bancos = Conexao.listarBancos();
        if (bancos.isEmpty()) {
            mostrarAviso("Nenhum banco de dados encontrado. Crie um primeiro.");
            return;
        }
        String atual = Conexao.getNomeBancoAtual();
        String[] opcoes = bancos.toArray(new String[0]);
        String escolhido = (String) JOptionPane.showInputDialog(this,
                "Qual banco de dados abrir?", "Abrir banco",
                JOptionPane.QUESTION_MESSAGE, null, opcoes, atual != null ? atual : opcoes[0]);
        if (escolhido == null) {
            return;
        }
        try {
            Conexao.obterConexao(escolhido);
            recarregarNavegador();
            atualizarStatus("Banco: " + escolhido, "Pronto");
            exibirMensagem("Banco '" + escolhido + "' em uso.");
        } catch (SQLException erro) {
            mostrarAviso("Não foi possível abrir o banco: " + erro.getMessage());
        }
    }

    private void excluirBanco() {
        List<String> bancos = Conexao.listarBancos();
        if (bancos.isEmpty()) {
            mostrarAviso("Nenhum banco de dados encontrado.");
            return;
        }
        String[] opcoes = bancos.toArray(new String[0]);
        String escolhido = (String) JOptionPane.showInputDialog(this,
                "Qual banco de dados excluir?", "Excluir banco",
                JOptionPane.QUESTION_MESSAGE, null, opcoes, opcoes[0]);
        if (escolhido == null) {
            return;
        }
        int confirmar = JOptionPane.showConfirmDialog(this,
                "Excluir o banco '" + escolhido + "'?\nEssa ação apaga todos os dados do banco.",
                "Confirmar exclusão", JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);
        if (confirmar != JOptionPane.YES_OPTION) {
            return;
        }
        try {
            if (Conexao.excluirBanco(escolhido)) {
                recarregarNavegador();
                exibirMensagem("Banco '" + escolhido + "' excluído.");
            }
        } catch (SQLException erro) {
            mostrarAviso("Não foi possível excluir o banco: " + erro.getMessage());
        }
    }

    private void abrirPastaBancos() {
        if (!Desktop.isDesktopSupported()) {
            mostrarAviso("Não foi possível abrir o gerenciador de arquivos.");
            return;
        }
        try {
            Desktop.getDesktop().open(new File(Conexao.getPastaBase()));
        } catch (IOException erro) {
            mostrarAviso("Não foi possível abrir a pasta dos bancos: " + erro.getMessage());
        }
    }

    private void abrirGitHub(String url) {
        if (!Desktop.isDesktopSupported()) {
            mostrarAviso("Não foi possível abrir o navegador.\nAcesse: " + url);
            return;
        }
        try {
            Desktop.getDesktop().browse(new URI(url));
        } catch (IOException | URISyntaxException erro) {
            mostrarAviso("Não foi possível abrir o navegador.\nAcesse: " + url);
        }
    }

    private void mostrarAtalhos() {
        String texto = "- F5: executar a consulta do editor\n"
                + "- F6: executar somente a linha atual\n"
                + "- Ctrl+Enter: executar a consulta\n"
                + "- Ctrl+N: novo arquivo\n"
                + "- Ctrl+W: fechar aba\n"
                + "- Ctrl+S: salvar\n"
                + "- Ctrl+Shift+N: novo banco de dados\n"
                + "- Ctrl+H: histórico de execuções\n"
                + "- Ctrl+X / Ctrl+C / Ctrl+V: recortar, copiar e colar\n"
                + "- Ctrl+A: selecionar tudo";
        JOptionPane.showMessageDialog(this, texto, "Atalhos de teclado",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void mostrarSobre() {
        String texto = "PortuSQL 1.0\n\n"
                + "Uma ferramenta para estudantes que traz a sintaxe SQL "
                + "para perto do português.\n\n"
                + "Autor: Nicolas Gabriel Venâncio dos Santos\n"
                + "Repositório: https://github.com/00salociN/PortuSQL";
        JOptionPane.showMessageDialog(this, texto, "Sobre o PortuSQL",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void mostrarAviso(String mensagem) {
        JOptionPane.showMessageDialog(this, mensagem, "PortuSQL",
                JOptionPane.INFORMATION_MESSAGE);
    }

    // ------------------------------------------------------------------
    // Barra de ferramentas
    // ------------------------------------------------------------------

    private JPanel criarBarraFerramentas() {
        JPanel barra = new JPanel(new BorderLayout());
        barra.setBackground(COR_FUNDO_PAINEL);
        barra.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, COR_BORDA),
                new EmptyBorder(6, 12, 6, 12)));

        JPanel botoesEsquerda = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        botoesEsquerda.setOpaque(false);

        botoesEsquerda.add(criarBotaoBarra("\u25B6 Executar (F5)", COR_VERDE,
                () -> executarAtivo(false)));
        botoesEsquerda.add(criarBotaoBarra("\u25B7 Execu\u00e7\u00e3o \u00fanica (F6)", COR_AZUL,
                () -> executarAtivo(true)));
        botoesEsquerda.add(criarBotaoBarra("\u25A0 Parar", COR_VERMELHO,
                () -> disparar(listener != null ? listener::aoParar : null)));
        botoesEsquerda.add(criarBotaoBarra("\uD83D\uDCBE Salvar", COR_TEXTO,
                () -> disparar(listener != null ? listener::aoSalvar : null)));
        botoesEsquerda.add(criarBotaoBarra("\u23F1 Hist\u00f3rico", COR_TEXTO,
                () -> disparar(listener != null ? listener::aoAbrirHistorico : null)));

        String nomeBancoAtual = Conexao.getNomeBancoAtual();
        JLabel statusConexao = new JLabel("\u25CF " + (nomeBancoAtual != null ? nomeBancoAtual : "sem banco"));
        statusConexao.setForeground(COR_VERDE);
        statusConexao.setFont(new Font("SansSerif", Font.PLAIN, 12));

        barra.add(botoesEsquerda, BorderLayout.WEST);
        barra.add(statusConexao, BorderLayout.EAST);
        return barra;
    }

    private JLabel criarBotaoBarra(String texto, Color cor, Runnable aoClicar) {
        JLabel label = new JLabel(texto);
        label.setForeground(cor);
        label.setFont(new Font("SansSerif", Font.PLAIN, 12));
        label.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        label.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                aoClicar.run();
            }
        });
        return label;
    }

    // ------------------------------------------------------------------
    // Area principal (navegador + editor + resultados)
    // ------------------------------------------------------------------

    private JPanel criarAreaPrincipal() {
        JPanel painel = new JPanel(new BorderLayout());
        painel.setBackground(COR_FUNDO_PRINCIPAL);

        JSplitPane splitHorizontal = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                criarNavegadorObjetos(), criarAreaEditorEResultados());
        splitHorizontal.setDividerLocation(230);
        splitHorizontal.setBorder(null);
        splitHorizontal.setBackground(COR_FUNDO_PRINCIPAL);

        painel.add(splitHorizontal, BorderLayout.CENTER);
        return painel;
    }

    // ------------------------------------------------------------------
    // Navegador de bancos (arvore com dados reais)
    // ------------------------------------------------------------------

    private JPanel criarNavegadorObjetos() {
        JPanel painel = new JPanel(new BorderLayout());
        painel.setBackground(COR_FUNDO_PAINEL);
        painel.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, COR_BORDA));

        JLabel titulo = new JLabel("NAVEGADOR DE BANCOS");
        titulo.setForeground(COR_TEXTO_SECUNDARIO);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 11));
        titulo.setBorder(new EmptyBorder(10, 10, 8, 0));

        arvore = new JTree();
        configurarArvore();
        montarArvore();

        // o conteudo de cada banco (pastas de objetos e os objetos em si)
        // e carregado sob demanda, so quando o usuario expande o no --
        // assim listar bancos nunca cria arquivos nem abre conexoes
        arvore.addTreeExpansionListener(new TreeExpansionListener() {
            @Override
            public void treeExpanded(TreeExpansionEvent evento) {
                Object ultimo = evento.getPath().getLastPathComponent();
                if (ultimo instanceof DefaultMutableTreeNode) {
                    carregarConteudoSeNecessario((DefaultMutableTreeNode) ultimo);
                }
            }

            @Override
            public void treeCollapsed(TreeExpansionEvent evento) {
                // nada a fazer ao recolher
            }
        });

        JScrollPane scroll = new JScrollPane(arvore);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(COR_FUNDO_PAINEL);

        painel.add(titulo, BorderLayout.NORTH);
        painel.add(scroll, BorderLayout.CENTER);
        return painel;
    }

    private void configurarArvore() {
        arvore.setBackground(COR_FUNDO_PAINEL);
        arvore.setForeground(COR_TEXTO);
        arvore.setRowHeight(22);
        arvore.setBorder(new EmptyBorder(0, 0, 0, 0));

        // renderer escuro: sem ele o Swing pinta cada linha com o branco
        // padrao (Tree.textBackground), destoando do tema
        DefaultTreeCellRenderer renderizador = new DefaultTreeCellRenderer();
        renderizador.setBackground(COR_FUNDO_PAINEL);
        renderizador.setBackgroundNonSelectionColor(COR_FUNDO_PAINEL);
        renderizador.setBackgroundSelectionColor(COR_SELECAO);
        renderizador.setTextNonSelectionColor(COR_TEXTO);
        renderizador.setTextSelectionColor(Color.WHITE);
        renderizador.setClosedIcon(criarIconePasta(false));
        renderizador.setOpenIcon(criarIconePasta(true));
        renderizador.setLeafIcon(criarIconeFolha());
        arvore.setCellRenderer(renderizador);
        arvore.setOpaque(false);
    }

    /** recarrega a lista de bancos (usado apos criar/excluir um banco) */
    public void recarregarNavegador() {
        montarArvore();
    }

    private void montarArvore() {
        DefaultMutableTreeNode raiz = new DefaultMutableTreeNode("Bancos de Dados");
        for (String banco : Conexao.listarBancos()) {
            DefaultMutableTreeNode noBanco = new DefaultMutableTreeNode(banco);
            noBanco.add(criarPasta(PASTA_TABELAS));
            noBanco.add(criarPasta(PASTA_VISOES));
            noBanco.add(criarPasta(PASTA_FUNCOES));
            noBanco.add(criarPasta(PASTA_GATILHOS));
            raiz.add(noBanco);
        }
        arvore.setModel(new DefaultTreeModel(raiz));
        arvore.expandPath(new TreePath(raiz.getPath()));
    }

    /** cria uma pasta de objetos com placeholder; o conteudo vem ao expandir */
    private DefaultMutableTreeNode criarPasta(String nome) {
        DefaultMutableTreeNode pasta = new DefaultMutableTreeNode(nome);
        pasta.add(new DefaultMutableTreeNode(AVISO_CARREGANDO));
        return pasta;
    }

    /**
     * carrega sob demanda o conteudo do no que acabou de ser expandido.
     * a decisao usa o nivel do no na arvore:
     * banco (1) é so estrutura; pasta (2) vira os objetos; tabela/visao
     * (3) vira a lista de campos; campos sao folhas (4).
     */
    private void carregarConteudoSeNecessario(DefaultMutableTreeNode no) {
        if (no.isLeaf()) {
            return;
        }
        Object primeiro = ((DefaultMutableTreeNode) no.getFirstChild()).getUserObject();
        if (!AVISO_CARREGANDO.equals(primeiro)) {
            return; // ja carregado
        }
        switch (no.getLevel()) {
            case 2 -> carregarItensDaPasta(no);
            case 3 -> carregarCampos(no);
            default -> { /* banco e a raiz: nada a carregar */ }
        }
    }

    private void carregarItensDaPasta(DefaultMutableTreeNode pasta) {
        String nomeBanco = (String) ((DefaultMutableTreeNode) pasta.getParent()).getUserObject();
        String nomePasta = (String) pasta.getUserObject();
        pasta.removeAllChildren();

        try {
            List<String> itens;
            if (PASTA_TABELAS.equals(nomePasta)) {
                itens = Conexao.listarTabelas(nomeBanco);
            } else if (PASTA_VISOES.equals(nomePasta)) {
                itens = Conexao.listarVisoes(nomeBanco);
            } else if (PASTA_FUNCOES.equals(nomePasta)) {
                itens = Conexao.listarFuncoes(nomeBanco);
            } else {
                itens = Conexao.listarGatilhos(nomeBanco);
            }

            if (itens.isEmpty()) {
                pasta.add(new DefaultMutableTreeNode(mensagemPastaVazia(nomePasta)));
            } else {
                for (String item : itens) {
                    if (PASTA_TABELAS.equals(nomePasta) || PASTA_VISOES.equals(nomePasta)) {
                        pasta.add(criarObjetoExpandivel(item));
                    } else {
                        pasta.add(new DefaultMutableTreeNode(item));
                    }
                }
            }
        } catch (Exception erro) {
            pasta.add(new DefaultMutableTreeNode(
                    "(nao foi possivel listar: " + erro.getMessage() + ")"));
        }

        ((DefaultTreeModel) arvore.getModel()).reload(pasta);
        arvore.expandPath(new TreePath(pasta.getPath()));
    }

    /** cria um no de tabela/visao expansivel que mostra os campos ao expandir */
    private DefaultMutableTreeNode criarObjetoExpandivel(String nome) {
        DefaultMutableTreeNode no = new DefaultMutableTreeNode(nome);
        no.add(new DefaultMutableTreeNode(AVISO_CARREGANDO));
        return no;
    }

    private void carregarCampos(DefaultMutableTreeNode objeto) {
        String nomeBanco = (String) ((DefaultMutableTreeNode) objeto.getParent().getParent()).getUserObject();
        String nomeObjeto = (String) objeto.getUserObject();
        objeto.removeAllChildren();

        try {
            List<String> campos = Conexao.listarColunas(nomeBanco, nomeObjeto);
            if (campos.isEmpty()) {
                objeto.add(new DefaultMutableTreeNode("(sem campos)"));
            } else {
                for (String campo : campos) {
                    objeto.add(new DefaultMutableTreeNode(campo));
                }
            }
        } catch (Exception erro) {
            objeto.add(new DefaultMutableTreeNode(
                    "(nao foi possivel listar: " + erro.getMessage() + ")"));
        }

        ((DefaultTreeModel) arvore.getModel()).reload(objeto);
        arvore.expandPath(new TreePath(objeto.getPath()));
    }

    private String mensagemPastaVazia(String nomePasta) {
        switch (nomePasta) {
            case PASTA_TABELAS:
                return "(sem tabelas)";
            case PASTA_VISOES:
                return "(sem vis\u00f5es)";
            case PASTA_FUNCOES:
                return "(sem fun\u00e7\u00f5es)";
            default:
                return "(sem gatilhos)";
        }
    }

    // ------------------------------------------------------------------
    // Editor + resultados
    // ------------------------------------------------------------------

    private JSplitPane criarAreaEditorEResultados() {
        JSplitPane splitVertical = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
                criarAbasEditor(), criarPainelResultados());
        splitVertical.setDividerLocation(300);
        splitVertical.setBorder(null);
        splitVertical.setBackground(COR_FUNDO_PRINCIPAL);
        return splitVertical;
    }

    private JTabbedPane criarAbasEditor() {
        abasEditor = new JTabbedPane();
        abasEditor.setBackground(COR_FUNDO_PAINEL);
        abasEditor.setForeground(COR_TEXTO);

        abasEditor.addTab("seuobjeto.sql", criarPainelEditorSql(""));
        abasEditor.addTab("criar_tabelas.sql", criarPainelEditorSql(""));
        return abasEditor;
    }

    /**
     * monta um editor de SQL com realce de sintaxe leve e numeracao de
     * linha automatica (via row header do JScrollPane, que o Swing
     * sincroniza com a rolagem do editor).
     */
    private JPanel criarPainelEditorSql(String textoInicial) {
        JPanel painel = new JPanel(new BorderLayout());
        painel.setBackground(COR_FUNDO_EDITOR);

        JTextPane editor = new JTextPane();
        editor.setText(textoInicial);
        editor.setBackground(COR_FUNDO_EDITOR);
        editor.setForeground(COR_TEXTO);
        editor.setCaretColor(COR_TEXTO);
        editor.setSelectionColor(COR_SELECAO);
        editor.setSelectedTextColor(COR_TEXTO);
        editor.setFont(new Font("Monospaced", Font.PLAIN, 13));
        editor.setBorder(new EmptyBorder(4, 8, 4, 4));
        editores.add(editor);

        JTextArea numeros = new JTextArea("1");
        numeros.setBackground(COR_FUNDO_EDITOR);
        numeros.setForeground(COR_TEXTO_SECUNDARIO);
        numeros.setFont(editor.getFont());
        numeros.setEditable(false);
        numeros.setFocusable(false);
        numeros.setBorder(new EmptyBorder(4, 8, 4, 8));

        // realce + numeracao so acompanham o documento enquanto este
        // ouvinte esta registrado; aplicar o realce dispara changedUpdate,
        // entao o proprio ouvinte se remove durante a atualizacao
        final StyledDocument doc = editor.getStyledDocument();
        adicionarEstilos(doc);
        aplicarRealce(doc);
        final DocumentListener ouvinte = new DocumentListener() {
            private void atualizar() {
                // mutar o documento DENTRO da notificacao e proibido
                // ("Attempt to mutate in notification"): a atualizacao do
                // realce e da numeracao roda depois, fora do lock
                SwingUtilities.invokeLater(() -> {
                    if (editor.getStyledDocument() != doc) {
                        return; // documento trocado: desiste de atualizar
                    }
                    doc.removeDocumentListener(this);
                    atualizarNumerosLinha(editor, numeros);
                    aplicarRealce(doc);
                    doc.addDocumentListener(this);
                });
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                atualizar();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                atualizar();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                atualizar();
            }
        };
        doc.addDocumentListener(ouvinte);
        atualizarNumerosLinha(editor, numeros);

        // Ctrl+Enter executa a consulta do editor
        editor.getInputMap(JComponent.WHEN_FOCUSED).put(
                KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, InputEvent.CTRL_DOWN_MASK),
                "executar-consulta");
        editor.getActionMap().put("executar-consulta", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                executarAtivo(false);
            }
        });

        JScrollPane scroll = new JScrollPane(editor);
        scroll.setBorder(null);
        scroll.setRowHeaderView(numeros);
        scroll.getViewport().setBackground(COR_FUNDO_EDITOR);

        painel.add(scroll, BorderLayout.CENTER);
        return painel;
    }

    private void registrarAtalhos() {
        InputMap mapa = getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        mapa.put(KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0), "executar-f5");
        mapa.put(KeyStroke.getKeyStroke(KeyEvent.VK_F6, 0), "executar-f6");

        getActionMap().put("executar-f5", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                executarAtivo(false);
            }
        });
        getActionMap().put("executar-f6", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                executarAtivo(true);
            }
        });
    }

    private void executarAtivo(boolean somenteLinha) {
        JTextPane editor = editorAtivo();
        if (editor == null) {
            return;
        }
        String sql = somenteLinha ? linhaNoCursor(editor) : editor.getText();
        if (somenteLinha) {
            disparar(listener != null ? () -> listener.aoExecutarUnica(sql) : null);
        } else {
            disparar(listener != null ? () -> listener.aoExecutar(sql) : null);
        }
    }

    private JTextPane editorAtivo() {
        if (editores.isEmpty()) {
            return null;
        }
        int indice = abasEditor.getSelectedIndex();
        return editores.get(Math.max(0, indice));
    }

    private String linhaNoCursor(JTextPane editor) {
        try {
            int caret = editor.getCaretPosition();
            Element raiz = editor.getDocument().getDefaultRootElement();
            Element elemento = raiz.getElement(raiz.getElementIndex(caret));
            return editor.getDocument().getText(elemento.getStartOffset(),
                    elemento.getEndOffset() - elemento.getStartOffset());
        } catch (BadLocationException ignorado) {
            return editor.getText();
        }
    }

    private void atualizarNumerosLinha(JTextPane editor, JTextArea numeros) {
        int totalLinhas = Math.max(
                editor.getDocument().getDefaultRootElement().getElementCount(), 6);
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i <= totalLinhas; i++) {
            sb.append(i).append("\n");
        }
        numeros.setText(sb.toString());
    }

    // ------------------------------------------------------------------
    // Realce de sintaxe (leve)
    // ------------------------------------------------------------------

    private void adicionarEstilos(StyledDocument doc) {
        Style normal = doc.addStyle("normal", null);
        StyleConstants.setForeground(normal, COR_TEXTO);

        Style palavra = doc.addStyle("palavra", null);
        StyleConstants.setForeground(palavra, COR_PALAVRA_RESERVADA);

        Style tipo = doc.addStyle("tipo", null);
        StyleConstants.setForeground(tipo, COR_TIPO_DADO);

        Style texto = doc.addStyle("texto", null);
        StyleConstants.setForeground(texto, COR_LITERAL);

        Style numero = doc.addStyle("numero", null);
        StyleConstants.setForeground(numero, COR_NUMERO);
    }

    private void aplicarRealce(StyledDocument doc) {
        try {
            String texto = doc.getText(0, doc.getLength());
            doc.setCharacterAttributes(0, doc.getLength(), doc.getStyle("normal"), true);
            Matcher m = PADRAO_TOKENS.matcher(texto);
            while (m.find()) {
                String token = m.group();
                String estilo;
                char primeiro = token.charAt(0);
                if (primeiro == '\'') {
                    estilo = "texto";
                } else if (Character.isDigit(primeiro)) {
                    estilo = "numero";
                } else {
                    String baixo = token.toLowerCase();
                    if (TIPOS_DADO.contains(baixo)) {
                        estilo = "tipo";
                    } else if (PALAVRAS_RESERVADAS.contains(baixo)) {
                        estilo = "palavra";
                    } else {
                        estilo = "normal";
                    }
                }
                doc.setCharacterAttributes(m.start(), m.group().length(),
                        doc.getStyle(estilo), true);
            }
        } catch (BadLocationException ignorado) {
            // documento recriado durante a digitacao: descarta o realce
        }
    }

    // ------------------------------------------------------------------
    // Resultados e mensagens
    // ------------------------------------------------------------------

    private JPanel criarPainelResultados() {
        JPanel painel = new JPanel(new BorderLayout());
        painel.setBackground(COR_FUNDO_PAINEL);
        painel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, COR_BORDA));

        abasResultados = new JTabbedPane();
        abasResultados.setBackground(COR_FUNDO_PAINEL);
        abasResultados.setForeground(COR_TEXTO);

        areaResultados = criarAreaResultado();
        areaMensagens = criarAreaResultado();

        abasResultados.addTab("Resultados", criarRolo(areaResultados));
        abasResultados.addTab("Mensagens", criarRolo(areaMensagens));
        abasResultados.addTab("Plano de Execu\u00e7\u00e3o", criarPainelVazio(
                "Selecione uma consulta para ver o plano de execu\u00e7\u00e3o."));
        abasResultados.addTab("Hist\u00f3rico", criarPainelVazio(
                "Hist\u00f3rico de consultas executadas nesta sess\u00e3o."));

        painel.add(abasResultados, BorderLayout.CENTER);
        return painel;
    }

    private JTextArea criarAreaResultado() {
        JTextArea area = new JTextArea();
        area.setEditable(false);
        area.setBackground(COR_FUNDO_EDITOR);
        area.setForeground(COR_TEXTO);
        area.setFont(new Font("Monospaced", Font.PLAIN, 12));
        area.setBorder(new EmptyBorder(6, 8, 6, 8));
        return area;
    }

    private JScrollPane criarRolo(JTextArea area) {
        JScrollPane rolo = new JScrollPane(area);
        rolo.setBorder(null);
        rolo.getViewport().setBackground(COR_FUNDO_EDITOR);
        return rolo;
    }

    /** painel escuro usado como placeholder das abas de resultado */
    private JPanel criarPainelVazio(String aviso) {
        JPanel painel = new JPanel(new BorderLayout());
        painel.setBackground(COR_FUNDO_PAINEL);

        JLabel label = new JLabel(aviso, SwingConstants.CENTER);
        label.setForeground(COR_TEXTO_SECUNDARIO);
        label.setFont(new Font("SansSerif", Font.PLAIN, 12));

        painel.add(label, BorderLayout.CENTER);
        return painel;
    }

    /**
     * mostra o texto na aba "Resultados" e a seleciona.
     * @param texto resultado formatado (grade de consulta ou linhas afetadas)
     */
    public void exibirResultado(String texto) {
        areaResultados.setText(texto);
        abasResultados.setSelectedIndex(0);
    }

    /**
     * mostra o texto na aba "Mensagens" e a seleciona.
     * @param texto mensagem (sucesso, erro ou SQL gerado)
     */
    public void exibirMensagem(String texto) {
        areaMensagens.setText(texto);
        abasResultados.setSelectedIndex(1);
    }

    /** atualiza os textos da barra de status inferior */
    public void atualizarStatus(String esquerda, String direita) {
        statusEsquerda.setText(esquerda);
        statusDireita.setText(direita);
    }

    // ------------------------------------------------------------------
    // Barra de status
    // ------------------------------------------------------------------

    private JPanel criarBarraStatus() {
        JPanel barra = new JPanel(new BorderLayout());
        barra.setBackground(COR_FUNDO_PAINEL);
        barra.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, COR_BORDA),
                new EmptyBorder(4, 12, 4, 12)));

        String nomeBancoAtual = Conexao.getNomeBancoAtual();
        statusEsquerda = new JLabel("Banco: " + (nomeBancoAtual != null ? nomeBancoAtual : "nenhum"));
        statusEsquerda.setForeground(COR_TEXTO_SECUNDARIO);
        statusEsquerda.setFont(new Font("SansSerif", Font.PLAIN, 11));

        statusDireita = new JLabel("Pronto");
        statusDireita.setForeground(COR_TEXTO_SECUNDARIO);
        statusDireita.setFont(new Font("SansSerif", Font.PLAIN, 11));

        barra.add(statusEsquerda, BorderLayout.WEST);
        barra.add(statusDireita, BorderLayout.EAST);
        return barra;
    }

    // ------------------------------------------------------------------
    // Icones da arvore (desenhados sob demanda, no tom do tema escuro)
    // ------------------------------------------------------------------

    private static Icon criarIconePasta(boolean aberta) {
        int tamanho = 16;
        BufferedImage imagem = new BufferedImage(tamanho, tamanho, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = imagem.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(new Color(0x8A, 0x90, 0x99));
        if (aberta) {
            g.fillRect(2, 3, 12, 3);                    // aba superior
            g.fillRoundRect(2, 5, 12, 9, 3, 3);         // corpo
        } else {
            g.fillRect(4, 2, 5, 3);                     // aba superior dobrada
            g.fillRoundRect(2, 4, 12, 10, 3, 3);        // corpo
        }
        g.dispose();
        return new ImageIcon(imagem);
    }

    private static Icon criarIconeFolha() {
        int tamanho = 16;
        BufferedImage imagem = new BufferedImage(tamanho, tamanho, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = imagem.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(new Color(0x8A, 0x90, 0x99));
        g.fillRoundRect(4, 2, 9, 12, 2, 2);
        g.setColor(new Color(0x1E, 0x1E, 0x1E));        // "canto dobrado" do arquivo
        g.fillPolygon(new int[] {9, 13, 13}, new int[] {2, 2, 6}, 3);
        g.dispose();
        return new ImageIcon(imagem);
    }

    private void disparar(Runnable acao) {
        if (acao != null) {
            acao.run();
        }
    }
}