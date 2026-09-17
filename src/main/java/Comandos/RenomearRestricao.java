package Comandos;

/**
 * @author Nicolas Gabriel Venâncio dos Santos
 * @version 1.0
 *
 * sintaxe: ALTERAR TABELA <nome_tabela> RENOMEAR RESTRICAO <nome_antigo> PARA <nome_novo>
 * este comando renomeia uma restrição
 */
public class RenomearRestricao implements Comando{
    /* 
	 * a escolha do private final permite que o objeto seja imutável 
	 * private: impede o acesso de outros arquivos
	 * final: impede a reatribuição do valor
	 */
    private final String tabela;
    private final String nomeAntigo;
    private final String nomeNovo;

    /**
    * construtor que inicializa os atributos
    * @param tabela nome da tabela onde a restrição se encontra
    * @param nomeAntigo nome atual da restrição
    * @param nomeNovo novo nome da restrição
    */
    public RenomearRestricao(String tabela, String nomeAntigo, String nomeNovo) {
        this.tabela = tabela;
        this.nomeAntigo = nomeAntigo;
        this.nomeNovo = nomeNovo;
    }

    /**
    * retorna o nome da tabela
    * @return nome tabela
    */
    public String getTabela(){
        return tabela;
    }

    /**
    * retorna o nome atual da restrição
    * @return nome da restrição
    */
    public String getNomeAntigo(){
        return nomeAntigo;
    }

    /** retorna o novo nome da restrição
    * @return novo nome da restrição
    */
    public String getNomeNovo() {
        return nomeNovo;
    }

    /**
    * retorna uma representação no formato String do objeto final do RenomearRestricao
    * formato: "RenomearRestricao[tabela=" + tabela + ", nomeAntigo" + nomeAntigo + ", nomeNovo" + nomeNovo + "]"
    *
    * @return representação em forma de texto do objeto
    */
    @Override
    public String toString(){
        return"RenomearRestricao[tabela=" + tabela + ", nomeAntigo" + nomeAntigo + ", nomeNovo" + nomeNovo + "]";
    }    
}
