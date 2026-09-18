package Comandos;

/**
 * @author Nicolas Gabriel Venâncio dos Santos
 * @version 1.0
 * 
 * sintaxe: ALTERAR TABELA <nome_tabela> MODIFICAR COLUNA <nome_coluna> <tipo_dado>
 * este comando alterar a definição de uma coluna existe, mudando o seu tipo de dado
 * 
 * implementa a interface {@link Comando}
 */
public class ModificarColuna implements Comando{
	/**
	 * a escolha do private final permite que o objeto seja imutável 
	 * private: impede o acesso de outros arquivos
	 * final: impede a reatribuição do valor
	 */
    private final String tabela;
    private final String nomeColuna;
    private final String novoTipoDado;

    /**
     * construtor que inicializa os atributos
     * @param tabela nome da tabela 
     * @param nomeColuna coluna que será alterada
     * @param novoTipoDado novo tipo de dado da coluna alterada
     */
    public ModificarColuna (String tabela, String nomeColuna, String novoTipoDado) {
        this.tabela = tabela;
        this.nomeColuna = nomeColuna;
        this.novoTipoDado = novoTipoDado;
    }
    
    /**
     * retorna o nome da tabela
     * @return nome da tabela
     */
    public String getTabela(){
        return tabela;
    }
    
    /**
     * retorna a coluna que será alterada
     * @return nome da coluna
     */
    public String getNomeColuna() {
        return nomeColuna;
    }
    
    /**
     * retorna o novo tipo do dado da coluna 
     * @return novo tipo do dado
     */
    public String getNovoTipoDado() {
        return novoTipoDado;
    }

    /**
     * retorna uma representação no formato String do objeto final do ModificarColuna
     * formato: "ModificarColuna[tabela=" + tabela + ", coluna=" + nomeColuna + ", novoTipoDado=" + novoTipoDado + "]"
     * @return representação em forma de texto
     */
    public String toString(){
        return "ModificarColuna[tabela=" + tabela + ", coluna=" + nomeColuna + ", novoTipoDado=" + novoTipoDado + "]";
    
    }
}
