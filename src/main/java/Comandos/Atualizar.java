package Comandos;

/**
 * @author Nicolas Gabriel Venâncio dos Santos
 * @version 1.0
 * 
 * sintaxe: atualizar <nome_tabela> 'set' <coluna> = <novo_valor> onde <condicao>
 * este comando atualiza dados que já existem em uma tabela
 * 
 * implemta a interface {@link Comando}
 */
import java.util.Map;


public class Atualizar implements Comando{
    /**
    * a escolha do private final permite que o objeto seja imutável
    * private: impede o acesso de outros arquivos
    * final: impede a reatribuição do valor
    * 
    * 
    */
    private final String tabela;
    private final Map<String, String> campos;
    private final String condicao;
    
    /**
     * construtor que inicaliza os atributos
     * @param tabela nome da tabela em que o campo que será atualizado está
     * @param campos campo que será atualizado
     * @param condicao condicao da atualização(opicional)   
     */
    public Atualizar(String tabela, Map<String, String> campos, String condicao){
        this.tabela = tabela;
        this.campos = campos;
        this.condicao = condicao;
    }
    
    /**
     * retorna a tabela que o campo atualizado está
     * @return nome da tabela
     */
    public String getTabela(){
        return tabela;
    }
    
    /**
     * retorna os campos e valores que serão atualizados
     * @return {@link Map} contendo os pares chave-valor (coluna -> novo valor)
     * 		onde a chave é nome da coluna e o valor é o novo valor
     */
    public Map<String, String> getCampos() {
        return campos;
    }
    
    /**
     * retorna a condição(opicional)
     * @return nome da condicao
     */
    public String getCondicao() {
        return condicao;
    }

    /**
     * retorna uma representação no formato String do objeto final do Atualizar
     * formato: "Atualizar[tabela=" + tabela + ", campos=" + campos + ", condicao=" + condicao + "]"
     * @return representação em forma de texto do objeto 
     */
    @Override
    public String toString() {
        return "Atualizar[tabela=" + tabela + ", campos=" + campos + ", condicao=" + condicao + "]";
    }
}
