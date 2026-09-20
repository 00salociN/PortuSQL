package Comandos;

import java.util.Map;

/**
 * comando que atualiza dados existentes em uma tabela
 * 
 * <p>sintaxe:
 * <pre>{@code
 * ATUALIZAR <nome_tabela> DEFINIR <coluna> = <novo_valor>
 * 		[, <coluna> = <novo_valor> ...]
 * 		ONDE <condicao>
 * }</pre>
 * 
 * <p>a clausula ONDE e opcional; sem ela, todas as linhas sao atualizadas
 * 
 * <p>implementa a interface {@link Comando}
 * 
 * @author Nicolas Gabriel Venâncio dos Santos
 * @version 1.0
 * @see Comando
 */
public class Atualizar implements Comando{
    /**
     * a escolha do private final permite que o objeto seja imutável
     * private: impede o acesso de outros arquivos
     * final: impede a reatribuição do valor
     */
    /** nome da tabela atualizada */
    private final String tabela;
    /** pares coluna -> novo valor que serao atualizados */
    private final Map<String, String> campos;
    /** condicao da atualizacao (opcional) */
    private final String condicao;
    
    /**
     * construtor que inicializa os atributos
     * @param tabela nome da tabela em que os campos serao atualizados
     * @param campos pares chave-valor (coluna -> novo valor)
     * @param condicao condicao da atualizacao (opcional)
     */
    public Atualizar(String tabela, Map<String, String> campos, String condicao){
        this.tabela = tabela;
        this.campos = campos;
        this.condicao = condicao;
    }
    
    /**
     * retorna a tabela em que os campos serao atualizados
     * @return nome da tabela
     */
    public String getTabela(){
        return tabela;
    }
    
    /**
     * retorna os campos e novos valores que serao atualizados
     * @return {@link Map} contendo os pares chave-valor (coluna -> novo valor)
     */
    public Map<String, String> getCampos() {
        return campos;
    }
    
    /**
     * retorna a condicao da atualizacao, se houver
     * @return condicao da atualizacao
     */
    public String getCondicao() {
        return condicao;
    }

    /**
     * retorna uma representação no formato String do comando Atualizar
     * <p>formato: {@code "Atualizar[tabela=" + tabela + ", campos=" + campos + ", condicao=" + condicao + "]"}
     * @return representação em forma de texto do comando
     */
    @Override
    public String toString() {
        return "Atualizar[tabela=" + tabela + ", campos=" + campos + ", condicao=" + condicao + "]";
    }
}