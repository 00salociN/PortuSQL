package Comandos;

/**
 * sintaxe: inserir <(campo(s))> em <nome_tabela> valores <(valor(es)> // ainda não sei se devo mudar a estrutura
 * este comando insere dados em uma tabela // por enquanto não consegue adicionar mais de 2 valores separados
 * 
 * implementa a interface {@link Comando}
 */
import java.util.List;


public class Inserir implements Comando{
	/**
	 * a escolha do private final permite que o objeto seja imutável 
	 * private: impede o acesso de outros arquivos
	 * final: impede a reatribuição do valor
	 */
    private final String tabela;
    private final List<String> colunas;
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
     * retorna uma representação no formatdo String do objeto final do Inserir
     * formato: "Inserir[tabela=" + tabela + ", colunas=" + colunas + ", valores=" + valores + "]"
     * @return representação em forma de texto do objeto
     */
    public String toString() {
        return "Inserir[tabela=" + tabela + ", colunas=" + colunas + ", valores=" + valores + "]";
    }
    
}
