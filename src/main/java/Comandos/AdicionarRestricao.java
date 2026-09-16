package Comandos;

/**
 * Sintaxe: ALTERAR TABELA <nome_tabela> ADICIONAR RESTRICAO <nome_restricao> <tipo_restricao> (colunas)
 * O comando adiciona uma restricao em um campo que já existe
 * 
 * implementa a interface {@link Comando}
 */
public class AdicionarRestricao implements Comando{
	/**
	 * a escolha do private final permite que o objeto seja imutável
	 * private: impede o acesso de outros arquivos
	 * final: impede a reatribuição do valor
	 */
    private final String tabela;
    private final String nomeRestricao;
    private final String definicao;

    /**
     * construtor que inicializa os atributos
     * @param tabela nome da tabela onde a restrição será adicionada
     * @param nomeRestricao nome da Restricao adicionada
     * @param definicao tipo da restrição
     */
    public AdicionarRestricao(String tabela, String nomeRestricao, String definicao) {
        this.tabela = tabela;
        this.nomeRestricao = nomeRestricao;
        this.definicao = definicao;
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
     * retorna uma representação no formatdo String do objeto final do AdicionarRestricao
     * formato: AdicionarRestricao[tabela=" + tabela + ", nomeRestricao" + nomeRestricao + ", definicao" + definicao + "]"
     * 
     * @return representação do AdicionarRestricao em formato de texto
     */
    @Override
    public String toString() {
        return "AdicionarRestricao[tabela=" + tabela + ", nomeRestricao" + nomeRestricao + ", definicao" + definicao + "]";
    }
    
}
