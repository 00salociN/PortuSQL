package Comandos;

/**
 * comando que renomeia uma coluna de uma tabela
 * 
 * <p>sintaxe:
 * <pre>{@code
 * ALTERAR TABELA <nome_tabela> RENOMEAR COLUNA <nome_antigo> PARA <nome_novo>
 * }</pre>
 * 
 * <p>implementa a interface {@link Comando}
 * 
 * @author Nicolas Gabriel Venâncio dos Santos
 * @version 1.0
 * @see Comando
 */
public class RenomearColuna implements Comando{
    /**
     * a escolha do private final permite que o objeto seja imutável 
     * private: impede o acesso de outros arquivos
     * final: impede a reatribuição do valor
     */
    /** nome da tabela que contem a coluna */
    private final String tabela;
    /** nome atual da coluna */
    private final String nomeAntigo;
    /** novo nome da coluna */
    private final String nomeNovo;

    /**
     * construtor que inicializa os atributos
     * @param tabela nome da tabela que contem a coluna
     * @param nomeAntigo nome atual da coluna
     * @param nomeNovo novo nome da coluna
     */
    public RenomearColuna (String tabela, String nomeAntigo, String nomeNovo) {
        this.tabela = tabela;
        this.nomeAntigo = nomeAntigo;
        this.nomeNovo = nomeNovo;
    }

    /**
     * retorna o nome da tabela
     * @return nome da tabela
     */
    public String getTabela() {
        return tabela;
    }

    /**
     * retorna o nome atual da coluna
     * @return nome atual da coluna
     */
    public String getNomeAntigo() {
        return nomeAntigo;
    }

    /**
     * retorna o novo nome da coluna
     * @return novo nome da coluna
     */
    public String getNomeNovo() {
        return nomeNovo;
    }

    /**
     * retorna uma representação no formato String do comando RenomearColuna
     * <p>formato: {@code "RenomearColuna[tabela=" + tabela + ", de=" + nomeAntigo + ", para=" + nomeNovo + "]"}
     * @return representação em forma de texto do comando
     */
    @Override
    public String toString() {
        return "RenomearColuna[tabela=" + tabela + ", de=" + nomeAntigo + ", para=" + nomeNovo + "]";
    }
}