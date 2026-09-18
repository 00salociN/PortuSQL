package Comandos;


import java.util.List;

/**
 * @author Nicolas Gabriel Venâncio dos Santos
 * @version 1.0
 * 
 * sintaxe: SELECIONAR <coluna1, coluna2> DE <nome_tabela> ONDE <condicao>
 * este comando seleciona as colunas desejadas de determinada tabela
 *
 * implementa a interface {@link Comando}
 */
public class Selecionar implements Comando{
    /** 
     * a escolha do private final permite que o objeto seja imutável 
     * private: impede o acesso de outros arquivos
     * final: impede a reatribuição do valor
     */
    private final String tabela;
    private final List<String> colunas;
    private final String condicao;

    /**
    * construtor que inicializa os atributos
    * @param tabela nome da tabela
    * @param colunas colunas desejadas
    * @param condicao condição (opicional)
    */
    public Selecionar(String tabela, List<String> colunas, String condicao) {
        this.tabela = tabela;
        this.colunas = colunas;
        this.condicao = condicao;
    }

    /**
    * retorna o nome da tabela
    * @return nome da tabela
    */
    public String getTabela() {
        return tabela;
    }

    /** @return {@link List} contendo objetos (colunas) na ordem
    * 		que serão renderizados, retornando uma lista
    */
    public List<String> getColunas() {
        return colunas;
    }

    /**
    * retorna a condição, se tiver
     * @return retorna a condicao
    */
    public String getCondicao() {
        return condicao;
    }

    /** retorna uma representação no formato String do objeto final do CriarTabela
    * formato: "Selecionar[tabela=" + tabela + ", colunas=" + colunas + ", condicao=" + condicao + "]"
    *
    * @return representação em forma de texto do objeto
    */
    @Override
    public String toString() {
        return "Selecionar[tabela=" + tabela + ", colunas=" + colunas + ", condicao=" + condicao + "]";
        
    
    }
}
