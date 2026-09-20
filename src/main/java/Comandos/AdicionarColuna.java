package Comandos;


/**
 * comando que adiciona uma coluna em uma tabela
 * 
 * <p>sintaxe:
 * <pre>{@code
 * ALTERAR TABELA <nome_tabela> ADICIONAR COLUNA <nome_coluna> <tipo_dado> <restricao (opcional)>
 * }</pre>
 * 
 * <p>implementa a interface {@link Comando} e utiliza a classe
 *      {@link DefinicaoColuna} para ter um padrão sobre o que é uma coluna
 *      
 * @author Nicolas Gabriel Venâncio dos Santos
 * @version 1.0
 * @see Comando
 * @see DefinicaoColuna
 */

public class AdicionarColuna implements Comando{ 
	/** 
	 * a escolha do private final permite que o objeto seja imutável 
	 * private: impede o acesso de outros arquivos
	 * final: impede a reatribuição do valor
	 */
	
	/** nome da tabela */
    private final String tabela;
    
    /** definição da nova coluna (nome, tipo e restrição (opcional) */
    private final DefinicaoColuna novaColuna;
    
    
    /** 
     * construtor que inicializa os atributos
     * @param tabela nome da tabela em que a coluna será adicionada
     * @param novaColuna definicao da coluna que será adicionada
     */
    public AdicionarColuna(String tabela, DefinicaoColuna novaColuna) {
        this.tabela = tabela;
        this.novaColuna = novaColuna;
    }
    
    /** 
     * retorna a tabela que receberá a nova coluna
     * @return nome da tabela armazenada
     */
    public String getTabela() { 
        return tabela; 
    }
    
    /**
     * retorna a definicao da coluna que será adicionada
     * @return definicao da nova coluna
     */
    public DefinicaoColuna getNovaColuna(){ 
        return novaColuna; 
    }
    
    /**
     * retorna uma representação no formato String o comando AdicionarColuna
     * <p>formato: {@code "AdicionarColuna[tabela=" + tabela + ", coluna=" + novaColuna + "]"}
     * @return representação em forma de texto do comando
     */
    @Override 
    public String toString() {
        return "AdicionarColuna[tabela=" + tabela + ", coluna=" + novaColuna + "]";
    }
    
}
