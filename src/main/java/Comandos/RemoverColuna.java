package Comandos;

/**
 *
 * @author Nicolas Gabriel Venâncio dos Santos
 * @version 1.0
 * 
 * sintaxe: ALTERAR <nome_tabela> REMOVER COLUNA <nome_coluna>
 * este comando remove uma coluna da tabela escolhida
 * 
 * implementa a interface {@link Comando}
 */
public class RemoverColuna implements Comando{
	/* 
	 * a escolha do private final permite que o objeto seja imutável 
	 * private: impede o acesso de outros arquivos
	 * final: impede a reatribuição do valor
	 */
    private final String tabela;
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
     * retorna uma representação no formatdo String do objeto final do RemoverColuna
     * formato: "RemoverColuna[tabela=" + tabela + ", coluna=" + nomeColuna + "]"
     * 
     * @return representação em forma de texto do objeto
     */
    @Override
    public String toString() {
        return "RemoverColuna[tabela=" + tabela + ", coluna=" + nomeColuna + "]";
    }
}
