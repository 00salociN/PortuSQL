package Comandos;

/**
 * comando que renomeia uma restricao de uma tabela
 * 
 * <p>sintaxe:
 * <pre>{@code
 * ALTERAR TABELA <nome_tabela> RENOMEAR RESTRICAO <nome_antigo> PARA <nome_novo>
 * }</pre>
 * 
 * <p>implementa a interface {@link Comando}
 * 
 * @author Nicolas Gabriel Venâncio dos Santos
 * @version 1.0
 * @see Comando
 */
public class RenomearRestricao implements Comando{
    /**
	 * a escolha do private final permite que o objeto seja imutável 
	 * private: impede o acesso de outros arquivos
	 * final: impede a reatribuição do valor
	 */
	/** nome da tabela que contem a restricao */
    private final String tabela;
    /** nome atual da restricao */
    private final String nomeAntigo;
    /** novo nome da restricao */
    private final String nomeNovo;

    /**
     * construtor que inicializa os atributos
     * @param tabela nome da tabela onde a restricao se encontra
     * @param nomeAntigo nome atual da restricao
     * @param nomeNovo novo nome da restricao
     */
    public RenomearRestricao(String tabela, String nomeAntigo, String nomeNovo) {
        this.tabela = tabela;
        this.nomeAntigo = nomeAntigo;
        this.nomeNovo = nomeNovo;
    }

    /**
     * retorna o nome da tabela
     * @return nome da tabela
     */
    public String getTabela(){
        return tabela;
    }

    /**
     * retorna o nome atual da restricao
     * @return nome da restricao
     */
    public String getNomeAntigo(){
        return nomeAntigo;
    }

    /**
     * retorna o novo nome da restricao
     * @return novo nome da restricao
     */
    public String getNomeNovo() {
        return nomeNovo;
    }

    /**
     * retorna uma representação no formato String do comando RenomearRestricao
     * <p>formato: {@code "RenomearRestricao[tabela=" + tabela + ", nomeAntigo" + nomeAntigo + ", nomeNovo" + nomeNovo + "]"}
     * @return representação em forma de texto do comando
     */
    @Override
    public String toString(){
        return"RenomearRestricao[tabela=" + tabela + ", nomeAntigo" + nomeAntigo + ", nomeNovo" + nomeNovo + "]";
    }    
}