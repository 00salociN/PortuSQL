package Comandos;

/** 
 * @author Nicolas Gabriel Venâncio dos Santos
 * @version 1.0
 * 
 * Sintaxe: ALTERAR TABELA <nome_tabela> ADICIONAR COLUNA <nome_coluna> <tipo_dadp>
 * Este comando adiciona uma coluna a uma tabela ja existente
 * 
 * implementa a interface {@link Comando}
 * utiliza a class {@link DefinicaoColuna} para ter um padrão sobre o que é uma coluna
 */
public class AdicionarColuna implements Comando{ 
	/* 
	 * a escolha do private final permite que o objeto seja imutável 
	 * private: impede o acesso de outros arquivos
	 * final: impede a reatribuição do valor
	 */
    private final String tabela;
    private final DefinicaoColuna novaColuna;
    
    
    /** 
     * construtor que inicializa os atributos
     * @param tabela nome da tabela em que a coluna será adicionada
     * @param novaColuna definicao da coluna que será adicionada
     */
    public AdicionarColuna(String tabela, DefinicaoColuna novaColuna) {
        this.tabela = tabela;
        this.novaColuna = novaColuna;
    }
    
    /** 
     * retorna a tabela que receberá a nova coluna
     * @return nome da tabela armazenada
     */
    public String getTabela() { 
        return tabela; 
    }
    
    /**
     * retorna a definicao da coluna que será adicionada
     * @return definicao da nova coluna
     */
    public DefinicaoColuna getNovaColuna(){ 
        return novaColuna; 
    }
    
    /**
     * retorna uma representação no formatdo String do objeto final do AdicionarColuna
     * Formato: AdicionarColuna[tabela=" + tabela + ", coluna=" + novaColuna + "]"
     * 
     * @return representação em forma de texto do objeto
     */
    @Override 
    public String toString() {
        return "AdicionarColuna[tabela=" + tabela + ", coluna=" + novaColuna + "]";
    }
    
}
