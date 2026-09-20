package Comandos;

/**
 * comando que deleta algum dado de uma tabela
 * 
 * <p>sintaxe:
 * <pre>{@code
 * DELETAR DE <nome_tabela> ONDE <condicao>
 * }</pre>
 * 
 * <p> implementa a interface {@link Comando}
 * 
 * @author Nicolas Gabriel Venâncio dos Santos
 * @version 1.0
 * @see Comando
 */
public class Deletar implements Comando{
	
	/** 
	 * a escolha do private final permite que o objeto seja imutável 
	 * private: impede o acesso de outros arquivos
	 * final: impede a reatribuição do valor
	 */
	
	/** nome da tabela */
    private final String tabela;
    
    /** condição (se não tiver condição, ele apaga todos os dados) */
    private final String condicao;

    /**
     * construtor que inicializa os atributos
     * @param tabela nome da tabela
     * @param condicao condição para deletar 
     */
    public Deletar(String tabela, String condicao) {
        this.tabela = tabela;
        this.condicao = condicao;
    }
    
    /**
     * retorna o nome da tabela
     * @return nome da tabela
     */
    public String getTabela() {
        return tabela;
    }
    
    /**
     * retorna a condição, se tiver
     * @return condição
     */
    public String getCondicao(){
        return condicao;
    }
    
    /**
     * retorna uma representação no formato String o comando Deletar
     * <p> formato: {@code "Deletar[tabela=" + tabela + ", condicao=" + condicao + "]"}
     * @return representação em forma de texto do comando 
     */
    @Override
    public String toString() {
        return "Deletar[tabela=" + tabela + ", condicao=" + condicao + "]";
    }
    
}
