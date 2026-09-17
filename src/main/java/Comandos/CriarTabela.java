package Comandos;

/**
 * @author Nicolas Gabriel Venâncio dos Santos
 * @version 1.0
 * 
 * sintaxe: CRIAR TABELA <nome_tabela> (
 *  	<identificador> <tipo_dado> <restricao>,
 *  	<identificador> <tipo_dado> <restricao>,
 *  	...
 *  	)
 * este comando criar uma nova tabela
 * 
 * implementa a interface {@link Comando}
 * utiliza a classe {@link DefinicaoColuna} para ter um padrão sobr o que é uma coluna
 */
import java.util.List;


/** 
 * a escolha do private final permite que o objeto seja imutável 
 * private: impede o acesso de outros arquivos
 * final: impede a reatribuição do valor
 */
public class CriarTabela implements Comando{
    private final String nomeTabela;
    private final List<DefinicaoColuna> colunas;

    /**
     * construtor que inicializa os atributos
     * @param nomeTabela nome da tabela criada
     * @param colunas nome das colunas criadas
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
     * 
     * @return {@link List} contendo objetos {@link DefinicaoColuna} na ordem
     * 		que serão renderizados, retornando uma lista
     */
    public List<DefinicaoColuna> getColunas(){
        return colunas;
    }

    /**
     * retorna uma representação no formatdo String do objeto final do CriarTabela
     * formato: "CriarTabela[tabela=" + nomeTabela + ", colunas=" + colunas + "]"
     * @return representação em forma de texto do objeto 
     */
    @Override
    public String toString() {
        return "CriarTabela[tabela=" + nomeTabela + ", colunas=" + colunas + "]";
    
    }
}
