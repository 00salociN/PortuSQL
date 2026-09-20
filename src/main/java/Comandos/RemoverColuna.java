package Comandos;

/**
 * comando que remove uma coluna de uma tabela
 * 
 * <p>sintaxe:
 * <pre>{@code
 * ALTERAR TABELA <nome_tabela> REMOVER COLUNA <nome_coluna>
 * }</pre>
 * 
 * <p> implementa a interface {@link Comando}
 * 
 * @author Nicolas Gabriel Venâncio dos Santos
 * @version 1.0
 * @see Comando
 */
public class RemoverColuna implements Comando{
	/* 
	 * a escolha do private final permite que o objeto seja imutável 
	 * private: impede o acesso de outros arquivos
	 * final: impede a reatribuição do valor
	 */
	
	/** nome da tabela */
    private final String tabela;
    
    /** nome da coluna */
    private final String nomeColuna;
    
    /**
     * construtor que inicializa os atributos
     * @param tabela nome da tabela que a coluna será removida
     * @param nomeColuna nome da coluna que será removida
     */
    public RemoverColuna(String tabela, String nomeColuna) {
        this.tabela = tabela;
        this.nomeColuna = nomeColuna;
    }
    
    /**
     * retorna o nome da tabela
     * @return nome da tabela
     */
    public String getTabela() {
        return tabela;
    }
    
    /**
     * retorna o nome da coluna
     * @return nome da coluna
     */
    public String getNomeColuna() {
        return nomeColuna;
    }

    /**
     * retorna uma representação no formato String o comando RemoverColuna
     * formato: "RemoverColuna[tabela=" + tabela + ", coluna=" + nomeColuna + "]"
     * 
     * @return representação em forma de texto do comando
     */
    @Override
    public String toString() {
        return "RemoverColuna[tabela=" + tabela + ", coluna=" + nomeColuna + "]";
    }
}
