package Conexao;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * camada de conexao com o banco H2 embarcado do PortuSQL.
 *
 * <p>cada "banco de dados" do PortuSQL e um arquivo H2 dentro da pasta
 * {@code ~/.portusql}, criado automaticamente pelo driver no momento em que
 * a conexao e aberta. esta classe centraliza a criacao, o uso e a exclusao
 * desses bancos, mantendo a conexao ativa e o nome do banco em uso.
 *
 * <p>como o H2 (modo embarcado) nao possui o comando SQL CREATE DATABASE,
 * "criar um banco" aqui significa abrir uma conexao com o caminho desejado;
 * o arquivo do banco e gerado pelo proprio driver.
 *
 * @author Nicolas Gabriel Venâncio dos Santos
 * @version 1.0
 * @see Executor
 */
public class Conexao {
    /** pasta onde os bancos do PortuSQL ficam guardados */
    private static final String PASTA_BASE = System.getProperty("user.home") + File.separator + ".portusql";
    /** usuario padrao do H2 */
    private static final String USER = "sa";
    /** senha padrao do H2 (vazia) */
    private static final String PASSWORD = "";
    /** nome do banco usado quando nenhum outro foi escolhido */
    private static final String BANCO_PADRAO = "portusql";
    /** conexao ativa no momento (ou {@code null} se nao houver) */
    private static Connection conexaoAtual;
    /** nome do banco conectado no momento (ou {@code null}) */
    private static String nomeBancoAtual;
    
    private Conexao() { 
    	
    }
    
    /**
     * retorna o caminho do arquivo de um banco dentro da pasta base
     * @param nomeBanco nome do banco
     * @return caminho completo (sem extensao) do arquivo do banco
     */
    public static String caminhoBanco(String nomeBanco) {
    	return PASTA_BASE + File.separator + nomeBanco;
    }

    /**
     * retorna a pasta onde os bancos do PortuSQL ficam guardados
     * @return caminho completo da pasta base (~/.portusql)
     */
    public static String getPastaBase() {
    	return PASTA_BASE;
    }

    /**
     * retorna o nome do banco em uso no momento
     * @return nome do banco atual, ou {@code null} se nenhum estiver conectado
     */
    public static String getNomeBancoAtual() {
    	return nomeBancoAtual;
    }
    
    /**
     * retorna a conexao ativa com o banco atual, ou com o banco padrao
     * caso nenhum tenha sido escolhido
     * @return conexao H2 ativa
     * @throws SQLException se a conexao falhar
     */
    public static Connection obterConexaoAtual() throws SQLException{
    	String nomeBanco = (nomeBancoAtual != null) ? nomeBancoAtual : BANCO_PADRAO;
    	return obterConexao(nomeBanco);
    }
    
    /**
     * retorna (e mantem ativa) a conexao com o banco informado, trocando o
     * banco atual quando necessario
     * @param nomeBanco nome do banco
     * @return conexao H2 com o banco informado
     * @throws SQLException se a conexao falhar
     */
    public static Connection obterConexao(String nomeBanco) throws SQLException{
    	boolean troca = conexaoAtual == null || conexaoAtual.isClosed() || !nomeBanco.equals(nomeBancoAtual);
    	
    	if(troca) {
    		fecharConexao();
    		new File(PASTA_BASE).mkdirs();
    		String url = "jdbc:h2:" + caminhoBanco(nomeBanco);
    		conexaoAtual = DriverManager.getConnection(url, USER, PASSWORD);
    		nomeBancoAtual = nomeBanco;
    	}
    	return conexaoAtual;
    }
    
    /**
     * fecha a conexao ativa e limpa o nome do banco em uso
     * @throws SQLException se houver erro ao fechar a conexao
     */
    public static void fecharConexao() throws SQLException{
    	if(conexaoAtual != null && !conexaoAtual.isClosed()) {
    		conexaoAtual.close();
    	}
    	conexaoAtual = null;
    	nomeBancoAtual = null;
    	
    }
    
    
    /**
     * exclui o arquivo de um banco, fechando a conexao antes se for o banco atual
     * @param nomeBanco nome do banco a ser excluido
     * @return {@code true} se o arquivo principal do banco foi apagado
     * @throws SQLException se houver erro ao fechar a conexao
     */
    public static boolean excluirBanco(String nomeBanco) throws SQLException {
    	if (nomeBanco.equals(nomeBancoAtual)) {
    		fecharConexao();
    	}
    	String caminho = caminhoBanco(nomeBanco);
    	File arquivoPrincipal = new File(caminho + ".mv.db");
    	File arquivoTrace = new File(caminho + ".trace.db");
    	boolean apagouPrincipal = !arquivoPrincipal.exists() || arquivoPrincipal.delete();
    	if(arquivoTrace.exists()) {
    		arquivoTrace.delete();
    	}
    	return apagouPrincipal;
    }

    /**
     * lista os bancos existentes na pasta base (~/.portusql), em ordem
     * alfabetica. apenas le o diretorio -- nao abre nem cria conexoes.
     * @return nomes dos bancos encontrados
     */
    public static List<String> listarBancos() {
    	List<String> bancos = new ArrayList<>();
    	File pasta = new File(PASTA_BASE);
    	File[] arquivos = pasta.listFiles((dir, nome) -> nome.endsWith(".mv.db"));
    	if (arquivos != null) {
    		for (File arquivo : arquivos) {
    			String nome = arquivo.getName();
    			bancos.add(nome.substring(0, nome.length() - ".mv.db".length()));
    		}
    		Collections.sort(bancos);
    	}
    	return bancos;
    }

    /**
     * lista as tabelas (esquema PUBLIC) de um banco. abre a conexao com o
     * banco informado, ou seja: listar as tabelas tambem deixa esse banco
     * como o banco em uso.
     * @param nomeBanco nome do banco
     * @return nomes das tabelas em ordem alfabetica
     * @throws SQLException se nao for possivel conectar ou consultar
     */
    public static List<String> listarTabelas(String nomeBanco) throws SQLException {
    	List<String> tabelas = new ArrayList<>();
    	Connection conexao = obterConexao(nomeBanco);
    	try (Statement comando = conexao.createStatement();
    		 ResultSet resultado = comando.executeQuery(
    		         "select table_name from information_schema.tables "
    		         + "where table_schema = 'PUBLIC' order by table_name")) {
    		while (resultado.next()) {
    			tabelas.add(resultado.getString(1));
    		}
    	}
    	return tabelas;
    }
    
  

    }