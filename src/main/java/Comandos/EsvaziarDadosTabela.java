package Comandos;

/**
 * @author Nicolas Gabriel Venâncio dos Santos
 * @version 1.0
 * 
 * sintaxe: ESVAZIAR TABELA <nome_tabela>
 * este comando remove todas as linhas de uma tabela
 * 
 * implementa a interface {@link Comando}
 */
public class EsvaziarDadosTabela implements Comando{
	
	/**
	 * a escolha do private final permite que o objeto seja imutável 
	 * private: impede o acesso de outros arquivos
	 * final: impede a reatribuição do valor
	 */
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
      * retorna uma representação no formatdo String do objeto final do EsvaziarDadosTabela
      * formato: "EsvaziarDadosTabela[tabela=" + tabela + "]"
      * @return representação em forma de texto do objeto
      */
    @Override
    public String toString() {
        return"EsvaziarDadosTabela[tabela=" + tabela + "]";
    }
    
}
