package Comandos;

/**
 * comando que adiciona uma restrição em uma coluna de uma tabela
 * 
 * <p> sintaxe:
 * <pre> {@code
 * ALTERAR TABELA <nome_tabela> ADICIONAR RESTRICAO <nome_restricao> <coluna> 
 * 		<tipo_restricao> REFERENCIA <tabela_referenciada> (<coluna_referenciada>)
 * }</pre>
 * 
 * <p>exemplos:
 * 
 * <p> chave primária:
 * <pre>{@code
 * ALTERAR TABELA <nome_tabela> ADICIONAR RESTRICAO <nome_restricao> 
 * 		<coluna> CHAVE PRIMARIA
 * }</pre>
 * 
 * <p>chave estrangeira:
 * <pre> {@code
 * ALTERAR TABELA <nome_tabela> ADICIONAR RESTRICAO <nome_restricao>
 * 		<coluna> CHAVE ESTRANGEIRA REFERENCIA <tabela_referenciada> (<coluna_referenciada>)
 * }</pre>
 * 
 * <p>unica:
 * <pre> {@code
 * ALTERAR TABELA <nome_tabela> ADICIONAR RESTRICAO  <nome_restricao> <coluna> unica
 * }</pre>
 * 
 * <p>restrição do tipo verificar (check), está no {@link AdicionarRestricaoVerificar},
 * 		pois a gramática é diferente o suficiente para não respeitar as outras acima
 * 
 * @author Nicolas Gabriel Venâncio dos Santos
 * @version 1.0
 * @see Comando
 * @see AdicionarRestricaoVerificar
 */

public class AdicionarRestricao implements Comando{
	/**
	 * a escolha do private final permite que o objeto seja imutável
	 * private: impede o acesso de outros arquivos
	 * final: impede a reatribuição do valor
	 */
	
	/** nome da tabela onde a coluna será adicionada */
    private final String tabela;
    
    /** nome da restrição adicionada */
    private final String nomeRestricao;
    
    /** tipo da restrição adicionada */
    private final String definicao;
    
    /** coluna onde a restrição será adicionada */
    private final String coluna;
    
    /** nome da tabela que será referenciada, caso seja uma chave estrangeira */
    private final String tabelaReferenciada;
    
    /** coluna que será referenciada, caso seja uma chave estrangeira */
    private final String colunaReferenciada;

    /**
     * construtor que inicializa os atributos
     * @param tabela nome da tabela onde a restrição será adicionada
     * @param nomeRestricao nome da restrição adicionada
     * @param definicao tipo da restrição
     * @param coluna nome da coluna onde a restrição se aplica
     * @param tabelaReferenciada nome da tabela referenciada, se for chave estrangeira
     * @param colunaReferenciada nome da coluna referenciada, se for chave estrangeira
     */
    public AdicionarRestricao(String tabela, String nomeRestricao, String definicao, String coluna, String tabelaReferenciada, String colunaReferenciada) {
        this.tabela = tabela;
        this.nomeRestricao = nomeRestricao;
        this.definicao = definicao;
        this.coluna = coluna;
        this.tabelaReferenciada = tabelaReferenciada;
        this.colunaReferenciada = colunaReferenciada;
        
        
    }
    
    /**
     * retorna a tabela que a restrição será adicionada
     * @return nome da tabela
     */
    public String getTabela() {
        return tabela;
    }
    
    /**
     * retorna o nome da restrição adicionada
     * @return nome da restrição
     */
    public String getNomeRestricao() {
        return nomeRestricao;
    }
    
    /**
     * retorna o tipo da restricao adicionada
     * @return tipo da restrição
     */
    public String getDefinicao() {
        return definicao;
    }
    
    /**
     * retorna a coluna que a restrição se aplica
     * @return nome da coluna
     */
    public String getColuna() {
    	return coluna;
    }
    
    /**
     * retorna a tabela que está sendo referenciada, caso a restrição seja uma chave estrangeira
     * @return nome da tabela referenciada
     */
    public String getTabelaReferenciada() {
    	return tabelaReferenciada;
    }
    
    /**
     * retorna a coluna que está sendo referenciada, caso a restrição seja uma chave estrangeira
     * @return nome da coluna referenciada
     */
    public String getColunaReferenciada() {
    	return colunaReferenciada;
    }
    /**
     * retorna uma representação no formato String o comando AdicionarRestricao
     * <p>formato: {@code 
     * "AdicionarRestricao[tabela=" + tabela + ", nomeRestricao=" + nomeRestricao + ", coluna=" + coluna + ", definicao=" + definicao + ", tabelaReferenciada=" + tabelaReferenciada + ", colunaReferenciada=" + colunaReferenciada +"]"
     * @return representação em forma de texto do comando
     */
    @Override
    public String toString() {
        return "AdicionarRestricao[tabela=" + tabela + ", nomeRestricao=" + nomeRestricao + ", coluna=" + coluna + ", definicao=" + definicao + ", tabelaReferenciada=" + tabelaReferenciada + ", colunaReferenciada=" + colunaReferenciada +"]";
    }
    
}
