/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Conexao;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Etec
 */
public class Conexao {
    private static final String PASTA_BASE = System.getProperty("user.home") + File.separator + ".portusql";
    private static final String USER = "sa";
    private static final String PASSWORD = "";
    private static final String BANCO_PADRAO = "portusql";
    private static Connection conexaoAtual;
    private static String nomeBancoAtual;
    
    private Conexao() { 
    	
    }
    
    public static String caminhoBanco(String nomeBanco) {
    	return PASTA_BASE + File.separator + nomeBanco;
    }
    public static String getNomeBancoAtual() {
    	return nomeBancoAtual;
    }
    
    public static Connection obterConexaoAtual() throws SQLException{
    	String nomeBanco = (nomeBancoAtual != null) ? nomeBancoAtual : BANCO_PADRAO;
    	return obterConexao(nomeBanco);
    }
    
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
    
    public static void fecharConexao() throws SQLException{
    	if(conexaoAtual != null && !conexaoAtual.isClosed()) {
    		conexaoAtual.close();
    	}
    	conexaoAtual = null;
    	nomeBancoAtual = null;
    	
    }
    
    
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
    
  
    

    }
    

