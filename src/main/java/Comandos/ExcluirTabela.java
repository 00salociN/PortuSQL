/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Comandos;

/**
 * comando que remove uma tabela inteira
 * 
 * <p>sintaxe:
 * <pre> {@code
 * EXCLUIR TABELA <nome_tabela>
 * }</pre>
 * 
 * <p>implementa a interface {@link Comando}
 * 
 * @author Nicolas Gabriel Venâncio dos Santos
 * @version 1.0
 * @see Comando
 */
public class ExcluirTabela implements Comando{
	/** 
	 * a escolha do private final permite que o objeto seja imutável 
	 * private: impede o acesso de outros arquivos
	 * final: impede a reatribuição do valor
	 */
	
	/** nome da tabela */
    private final String nomeTabela;

    /**
     * construtor que inicializa os atributos
     * @param nomeTabela nome da tabela que será excluida
     */
    public ExcluirTabela(String nomeTabela) {
        this.nomeTabela = nomeTabela;
    }
    
    /**
     * retorna o nome da tabela
     * @return nome da tabela
     */
    public String getNomeTabela() {
        return nomeTabela;
    }

    /**
     * retorna uma representação no formato String o comando ExcluirTabela
     * <p> formato: {@code "RemoverTabela[tabela=" + nomeTabela + "]"}
     * @return representação em forma de texto do comando
     */
    @Override
    public String toString() {
        return "RemoverTabela[tabela=" + nomeTabela + "]";
    }
    
}
