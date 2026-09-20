package Comandos;

/**
 * comando que remove uma restricao de uma tabela
 * 
 * <p>sintaxe:
 * <pre>{@code
 * ALTERAR TABELA <nome_tabela> REMOVER RESTRICAO <nome_restricao>
 * }</pre>
 * 
 * <p>implementa a interface {@link Comando}
 * 
 * @author Nicolas Gabriel Venâncio dos Santos
 * @version 1.0
 * @see Comando
 */
public class RemoverRestricao implements Comando{
	/**
	 * a escolha do private final permite que o objeto seja imutável
	 * private: impede o acesso de outros arquivos
	 * final: impede a reatribuição do valor
	 */
	/** nome da tabela que contem a restricao */
    private final String tabela;
    /** nome da restricao que sera removida */
    private final String nomeRestricao;

    /**
     * construtor que inicializa os atributos
     * @param tabela nome da tabela onde a restricao se encontra
     * @param nomeRestricao nome da restricao que sera removida
     */
    public RemoverRestricao(String tabela, String nomeRestricao) {
        this.tabela = tabela;
        this.nomeRestricao = nomeRestricao;
    }
    
    /**
     * retorna o nome da tabela
     * @return nome da tabela
     */
    public String getTabela() {
        return tabela;
    }
    
    /**
     * retorna o nome da restricao
     * @return nome da restricao
     */
    public String getNomeRestricao() {
        return nomeRestricao;
    }
    
    /**
     * retorna uma representação no formato String do comando RemoverRestricao
     * <p>formato: {@code "RemoverRestricao[tabela=" + tabela + ", nomeRestricao" + nomeRestricao + "]"}
     * @return representação em forma de texto do comando
     */
    @Override
    public String toString() {
        return"RemoverRestricao[tabela=" + tabela + ", nomeRestricao" + nomeRestricao + "]";
    }
}