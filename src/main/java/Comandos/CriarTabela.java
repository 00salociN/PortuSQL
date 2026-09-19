package Comandos;

import java.util.List;

/**
 * comando que cria uma nova tabela
 * 
 * <p> sintaxe:
 * <pre> {@code
 * CRIAR TABELA <nome_tabela> (
 *      <identificador> <tipo_dado> <restricao (opicional)> ,
 *      <identificador> <tipo_dado> <restricao (opicional)> 
 *      ...
 * )
 * } </pre>
 * 
 * <p> implementa a interface {@link Comando} e utuliza a classe
 *      {@link DefinicaoColuna} para ter um padrão sobre o que é uma coluna
 * 
 * @author Nicolas Gabriel Venâncio dos Santos
 * @version 1.0
 * @see Comando
 * @see DefinicaoColuna
 */

public class CriarTabela implements Comando{
	/** 
	 * a escolha do private final permite que o objeto seja imutável 
	 * private: impede o acesso de outros arquivos
	 * final: impede a reatribuição do valor
	 */
	
	/** nome da tabela criada */
    private final String nomeTabela;
    
    /** definições das colunas, na ordem que serão criadas */
    private final List<DefinicaoColuna> colunas;

    /**
     * construtor que inicializa os atributos
     * @param nomeTabela nome da tabela criada
     * @param colunas lista de definições das colunas, na ordem criada
     */
    public CriarTabela(String nomeTabela, List<DefinicaoColuna> colunas) {
        this.nomeTabela = nomeTabela;
        this.colunas = colunas;
    }
    
    /**
     * retorna o nome da tabela criada
     * @return nome da tabela
     */
    public String getNomeTabela(){
        return nomeTabela;
    }
    
    /**
     * retorna as definiçãoes das colunas da tabela
     * @return {@link List} imutável de {@link DefinicaoColuna}, na ordem que serão renderizados
     */
    public List<DefinicaoColuna> getColunas(){
        return colunas;
    }

    /**
     * retorna uma representação no formato String do comando CriarTabela
     * <p> formato: {@code "CriarTabela[tabela=" + nomeTabela + ", colunas=" + colunas + "]"}
     * @return representação em forma de texto do objeto 
     */
    @Override
    public String toString() {
        return "CriarTabela[tabela=" + nomeTabela + ", colunas=" + colunas + "]";
    
    }
}
