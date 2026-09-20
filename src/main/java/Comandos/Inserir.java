package Comandos;

import java.util.List;

/**
 * comando que insere dados nas colunas
 * 
 * <p>sintaxe:
 * <pre> {@code
 * INSERIR <coluna(s)> EM <nome_tabela> VALORES (<valor(es)>) 
 * }</pre>
 * 
 * <p> implementa a interface {@link Comando}
 * 
 * @author Nicolas Gabriel Venâncio dos Santos
 * @version 1.0
 * @see Comando
 */


public class Inserir implements Comando{
	/**
	 * a escolha do private final permite que o objeto seja imutável 
	 * private: impede o acesso de outros arquivos
	 * final: impede a reatribuição do valor
	 */
	
	/** nome da tabela */
    private final String tabela;
    
    /** lista as colunas onde deseja inserir os valores*/
    private final List<String> colunas;
    
    /** listas os valores na ordem da coluna */
    private final List<String> valores;

    /**
     * construtor que inicializa os atributos
     * @param tabela nome da tabela que os dados serão inseridos
     * @param colunas colunas onde os dados serão inseridos
     * @param valores valores dos dados inseridos
     */
    public Inserir(String tabela, List<String> colunas, List<String> valores) {
        this.tabela = tabela;
        this.colunas = colunas;
        this.valores = valores;
    }

    /**
     * retorna o nome da tabela
     * @return nome tabela
     */
    public String getTabela() {
        return tabela;
    }
    
    /**
     * @return {@link List} contendo as colunas escolhidas para inserir os dados
     * 		serão renderizados na ordem passada, retornando uma lista
     */
    public List<String> getColunas() {
        return colunas;
    }
    
    /**
     * @return {@link List} contendo os valores que serão atribuidos as colunas
     * 		serão renderizados na ordem que as colunas foram escritas, retorna uma lista
     */
    public List<String> getValores() {
        return valores;
    }

    /**
     * retorna uma representação no formato String o comando Inserir
     * <p>formato: {@code "Inserir[tabela=" + tabela + ", colunas=" + colunas + ", valores=" + valores + "]"}
     * @return representação em forma de texto do comando
     */
    @Override
    public String toString() {
        return "Inserir[tabela=" + tabela + ", colunas=" + colunas + ", valores=" + valores + "]";
    }
    
}
