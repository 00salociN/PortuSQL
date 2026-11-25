package Telas;

import java.awt.CardLayout;
import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import Conexao.Conexao;
import Conexao.Resultado;
import PortuSQL.PortuSQL.Formatador;
import PortuSQL.PortuSQL.Interpretador;
import PortuSQL.PortuSQL.ResultadoExecucao;

/**
 * Janela principal do PortuSQL.
 *
 * <p>une a Homepage ({@link HomePanel}) e o editor de consultas
 * ({@link IdePanel}) num {@link CardLayout}: clicar no card "Bancos" ou
 * no iconinho de banco da barra lateral leva ao editor; "Inicio" volta
 * para a Homepage. esta classe implementa os dois listeners das telas
 * porque e ela que sabe O QUE fazer com cada clique (as telas apenas
 * avisam QUAL elemento foi clicado).
 *
 * <p>a execucao de uma consulta do editor passa pelo {@link Interpretador},
 * que reune lexer, parser, tradutor e executor, e devolve o tempo gasto e o
 * SQL gerado. O resultado (grade da consulta ou linhas afetadas) vai para a
 * aba "Resultados" do editor, com o SQL gerado na aba "Mensagens".
 *
 * <p>por seguranca o IDE nunca abre sobre o banco real do usuario:
 * ele usa o banco dedicado {@code teste_portusql}, o mesmo compromisso
 * dos testes JUnit. expandir um banco no navegador (ou criar um banco
 * novo) e que passa a usar o banco escolhido.
 *
 * @author Nicolas Gabriel Venâncio dos Santos
 * @version 1.1.0
 */
public class FramePortuSQL extends JFrame implements HomePanelListener, IdePanelListener {

    /** nome do card da Homepage no CardLayout */
    private static final String CARTAO_INICIO = "inicio";
    /** nome do card do editor de consultas no CardLayout */
    private static final String CARTAO_BANCO = "banco";

    /** paginas do GitHub abertas pelos cards de apoio da Homepage */
    private static final String URL_GITHUB = "https://github.com/00salociN/PortuSQL";
    private static final String URL_SUGESTOES = URL_GITHUB + "discussions/1";
    private static final String URL_DUVIDAS = URL_GITHUB + "/discussions/2";

    private final CardLayout navegacao;
    private final JPanel cartoes;
    private HomePanel home;
    private IdePanel ide;

    public FramePortuSQL() {
        super("PortuSQL");
        Tema.aplicar();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1024, 640);
        setLocationRelativeTo(null);

        // abre o banco de testes antes de montar o IDE para que a barra
        // de status ja mostre o banco em uso correto
        abrirBancoDeTeste();

        navegacao = new CardLayout();
        cartoes = new JPanel(navegacao);
        cartoes.setBackground(Tema.FUNDO);

        home = new HomePanel(this);
        ide = new IdePanel(this);

        cartoes.add(home, CARTAO_INICIO);
        cartoes.add(ide, CARTAO_BANCO);
        setContentPane(cartoes);

        mostrar(CARTAO_INICIO);
    }

    private void abrirBancoDeTeste() {
        try {
            Conexao.obterConexao("teste_portusql");
        } catch (SQLException erro) {
            // segue sem conexao; o Executor avisa no primeiro uso
        }
    }

    private void mostrar(String cartao) {
        navegacao.show(cartoes, cartao);
        if (CARTAO_BANCO.equals(cartao)) {
            ide.recarregarNavegador();
        }
    }

    // ------------------------------------------------------------------
    // HomePanelListener
    // ------------------------------------------------------------------

    @Override
    public void aoClicarInicio() {
        mostrar(CARTAO_INICIO);
    }

    @Override
    public void aoClicarBancos() {
        mostrar(CARTAO_BANCO);
    }

    @Override
    public void aoClicarConfiguracoes() {
        aviso("Configurações em construção.");
    }

    @Override
    public void aoClicarNovoBanco() {
        criarNovoBanco();
    }

    @Override
    public void aoClicarSugestoes() {
        abrirPaginaGitHub(URL_SUGESTOES);
    }

    @Override
    public void aoClicarDuvidas() {
        abrirPaginaGitHub(URL_DUVIDAS);
    }

    @Override
    public void aoClicarComandos() {
        abrirPaginaGitHub(URL_GITHUB);
    }

    @Override
    public void aoClicarQuemSomos() {
        abrirPaginaGitHub(URL_GITHUB);
    }

    /**
     * abre uma pagina do GitHub no navegador padrao do sistema.
     * se o ambiente nao tiver suporte a abrir o navegador, mostra a
     * mensagem com o link para o usuario copiar.
     */
    private void abrirPaginaGitHub(String url) {
        try {
            if (!Desktop.isDesktopSupported()) {
                aviso("Não foi possível abrir o navegador.\nAcesse: " + url);
                return;
            }
            Desktop.getDesktop().browse(new URI(url));
        } catch (IOException | URISyntaxException | UnsupportedOperationException erro) {
            aviso("Não foi possível abrir o navegador.\nAcesse: " + url);
        }
    }

    private void criarNovoBanco() {
        String nome = JOptionPane.showInputDialog(this,
                "Nome do novo banco de dados:", "Novo banco",
                JOptionPane.PLAIN_MESSAGE);
        if (nome == null || nome.trim().isEmpty()) {
            return;
        }
        String limpo = nome.trim();
        try {
            Conexao.obterConexao(limpo);
            mostrar(CARTAO_BANCO);
            ide.exibirMensagem("Banco '" + limpo + "' criado e em uso.");
        } catch (SQLException erro) {
            aviso("Não foi possível criar o banco: " + erro.getMessage());
        }
    }

    private void aviso(String mensagem) {
        JOptionPane.showMessageDialog(this, mensagem, "PortuSQL",
                JOptionPane.INFORMATION_MESSAGE);
    }

    // ------------------------------------------------------------------
    // IdePanelListener
    // ------------------------------------------------------------------

    @Override
    public void aoExecutar(String sql) {
        executar(sql, false);
    }

    @Override
    public void aoExecutarUnica(String sql) {
        executar(sql, true);
    }

    @Override
    public void aoParar() {
        ide.exibirMensagem("Execução interrompida.");
    }

    @Override
    public void aoSalvar() {
        ide.exibirMensagem("Salvar ainda em construção: copie o texto e salve manualmente.");
    }

    @Override
    public void aoAbrirHistorico() {
        ide.exibirMensagem("Histórico ainda em construção.");
    }

    /**
     * roda uma consulta no motor completo do PortuSQL e mostra o
     * resultado (ou o erro) nas abas do editor.
     * @param sql texto bruto escrito em PortuSQL
     * @param unica {@code true} quando veio da execucao da linha atual
     */
    private void executar(String sql, boolean unica) {
        if (sql == null || sql.trim().isEmpty()) {
            ide.exibirMensagem("Digite uma consulta no editor antes de executar.");
            return;
        }
        try {
            ResultadoExecucao execucao = Interpretador.executar(sql);
            Resultado resultado = execucao.getResultado();
            long ms = execucao.getMilissegundos();
            String sqlH2 = execucao.getSqlH2();

            String rotulo = unica ? "Linha executada" : "Consulta executada";
            String direita = "Tempo: " + ms + " ms";
            if (resultado.isConsulta()) {
                ide.exibirResultado(Formatador.consulta(resultado));
                direita += "    Linhas: " + resultado.getLinhas().size();
            } else {
                ide.exibirResultado(resultado.getLinhasAfetadas() + " linha(s) afetada(s).");
                direita += "    Linhas afetadas: " + resultado.getLinhasAfetadas();
                // comando que mudou o banco: atualiza o navegador de bancos
                ide.recarregarNavegador();
            }

            ide.atualizarStatus("SQL: " + sqlH2, direita);
            ide.exibirMensagem(rotulo + " em " + ms + " ms\n\nSQL gerado:\n" + sqlH2);
        } catch (SQLException | RuntimeException erro) {
            // ErroLexer, ErroParser e os erros do Tradutor (comando nao
            // suportado) sao RuntimeException; o SQLException vem do H2
            ide.exibirMensagem("ERRO: " + erro.getMessage());
        }
    }
}