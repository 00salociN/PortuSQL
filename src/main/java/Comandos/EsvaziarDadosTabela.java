package Comandos;

/**
 * comando que esvazia todos os dados de uma tabela
 * 
 * <p>sintaxe:
 * <pre> {@code
 * ESVAZIAR TABELA <nome_tabela>
 * }</pre>
 * 
 * <p> implementa a interface {@link Comando}
 * 
 * @author Nicolas Gabriel Venâncio dos Santos
 * @version 1.0
 * @see Comando
 */
public class EsvaziarDadosTabela implements Comando{
	
	/**
	 * a escolha do private final permite que o objeto seja imutável 
	 * private: impede o acesso de outros arquivos
	 * final: impede a reatribuição do valor
	 */
	
	/** noome da tabela */
    private final String tabela;

    /**
     * contrutor que iniciliza os atributos
     * @param tabela nome da tabela que será esvaziada
     */
    public EsvaziarDadosTabela(String tabela){
        this.tabela = tabela;
    }
    
    /**
     * retorna o nome da tabela que será esvaziada
     * @return nome da tabela
     */
     public String getTabela(){
         return tabela;
     }
     
     /**
      * retorna uma representação no formato String o comando EsvaziarDadosTabela
      * formato: "EsvaziarDadosTabela[tabela=" + tabela + "]"
      * @return representação em forma de texto do comando
      */
    @Override
    public String toString() {
        return"EsvaziarDadosTabela[tabela=" + tabela + "]";
    }
    
}
