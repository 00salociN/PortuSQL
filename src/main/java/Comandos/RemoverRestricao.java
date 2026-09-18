package Comandos;

/**
 * @author Nicolas Gabriel Venâncio dos Santos
 * @version 1.0
 * 
 * sintaxe: ALTERAR TABELA <nome_tabela> REMOVER RESTRICAO <nome_restricao>
 * este comando remover a restricao de uma tabela selecionada
 * 
 * implementa a interface {@link Comando}
 */
public class RemoverRestricao implements Comando{
	/* 
	 * a escolha do private final permite que o objeto seja imutável 
	 * private: impede o acesso de outros arquivos
	 * final: impede a reatribuição do valor
	 */
    private final String tabela;
    private final String nomeRestricao;

    /**
     * construtor que inicializa os atributos
     * @param tabela nome da tabela onde a restrição de encontra
     * @param nomeRestricao nome da restrição que será removida
     */
    public RemoverRestricao(String tabela, String nomeRestricao) {
        this.tabela = tabela;
        this.nomeRestricao = nomeRestricao;
    }
    
    /**
     * retorna o nome da tabela
     * @return nome tabela
     */
    public String getTabela() {
        return tabela;
    }
    
    /**
     * retorna o nome da restrição
     * @return nome da restrição
     */
    public String getNomeRestricao() {
        return nomeRestricao;
    }
    
    /**
     * retorna uma representação no formato String do objeto final do RemoverRestricao
     * formato: "RemoverRestricao[tabela=" + tabela + ", nomeRestricao" + nomeRestricao + "]"
     * 
     * @return representação em forma de texto do objeto
     */
    @Override
    public String toString() {
        return"RemoverRestricao[tabela=" + tabela + ", nomeRestricao" + nomeRestricao + "]";
    }
}
