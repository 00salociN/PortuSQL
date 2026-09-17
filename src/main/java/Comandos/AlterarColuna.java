package Comandos;

/**
 * @author Nicolas Gabriel Venâncio dos Santos
 * @version 1.0
 * 
 * sintaxe: ALTERAR TABELA <nome_tabela> ALTERAR COLUNA <nome_coluna> <tipo_dado>
 * alterar o tipo de dado de uma coluna existente
 * 
 * implementa a interface {@link Comando}
 */
public class AlterarColuna implements Comando{
    /* 
    * a escolha do private final permite que o objeto seja imutável 
    * private: impede o acesso de outros arquivos
    * final: impede a reatribuição do valor
    */
    private final String tabela;
    private final String nomeColuna;
    private final String novoTipoDado;

    /**
     * construtor que inicializa os atributos
     * @param tabela nome da tabela onde a coluna será alterada
     * @param nomeColuna nome da coluna alterada    
     * @param novoTipoDado novo dado da coluna
     */
    public AlterarColuna(String tabela, String nomeColuna, String novoTipoDado) {
        this.tabela = tabela;
        this.nomeColuna = nomeColuna;
        this.novoTipoDado = novoTipoDado;
    }
    
    /**
     * retorna a tabela onde a coluna será alterada
     * @return nome da tabela
     */
    public String getTabela(){
        return tabela;
    }
    
    /**
     * retorna a coluna que será alterada
     * @return nome da coluna alterada
     */
    public String getNomeColuna(){
        return nomeColuna;
    }
    
    /**
     * retorna o novo tipo do dado
     * @return novo tipo do dado
     */
    public String getNovoTipoDado() {
        return novoTipoDado;
    }
    
    /**
     * retorna uma representação no formatdo String do objeto final do AlterarColuna
     * formato: "AlterarColuna[tabela=" + tabela + ", nomeTabela=" + nomeColuna + ", novoTipoDado=" + novoTipoDado + "]"
     * @return representação em forma de texto do objeto 
     */
    @Override
    public String toString() {
        return "AlterarColuna[tabela=" + tabela + ", nomeTabela=" + nomeColuna + ", novoTipoDado=" + novoTipoDado + "]";
    
    }
}
