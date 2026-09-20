package Comandos;

/**
 * este comando adiciona a restrição verificar
 * 
 * diferente das outras restrições, o verificar não guarda uma coluna,
 * 		ele atua sobre uma condição/expressão
 * 
 * <p>sintaxe:
 * <pre>{@code
 * ALTERAR TABELA <nome_tabela> ADICIONAR RESTRICAO <nome_restricao> VERIFICAR <condicao>
 * }</pre>
 * 
 * <p> implementa a interface {@link Comando}
 * 
 * @author Nicolas Gabriel Venâncio dos Santos
 * @version 1.0
 * @see Comando
 */

public class AdicionarRestricaoVerificar implements Comando{
	
	/** 
	 * a escolha do private final permite que o objeto seja imutável 
	 * private: impede o acesso de outros arquivos
	 * final: impede a reatribuição do valor
	 */
	
	/** nome da tabela */
	private final String tabela;
	
	/** nome da restricao */
	private final String nomeRestricao;
	
	/** condição do verificar */
	private final String condicao;
	
	/**
	 * construtor que inicializa os atributos
	 * @param tabela nome da tabela onde a restrição será adicionada
	 * @param nomeRestricao nome da restrição adicionada
	 * @param condicao condição na qual o verificar irá atuar
	 */
	public AdicionarRestricaoVerificar(String tabela, String nomeRestricao, String condicao) {
		this.tabela = tabela;
		this.nomeRestricao = nomeRestricao;
		this.condicao = condicao;
	}
	
	/**
	 * retorna o nome da tabela que o verificar será adicionada
	 * @return nome da tabela
	 */
	public String getTabela() {
		return tabela;
	}
	
	/**
	 * retorna o nome da restrição do verificar
	 * @return nome da restrição
	 */
	public String getNomeRestricao() {
		return nomeRestricao;
	}
	
	/** 
	 * retorna a condição do verificar
	 * @return condicao
	 */
	public String condicao() {
		return condicao;
	}
	
	/**
	 * retorna uma representação no formato String o comando AdicionarRestricaoVerificar
	 * <p> formato: {@code "AdicionarRestricaoVerificar[tabela=" + tabela + ", nomeRestricao=" + nomeRestricao + ", condicao=" + condicao + "]"}
	 * @return representação em forma de texto do comando
	 */
	@Override
	public String toString() {
		return "AdicionarRestricaoVerificar[tabela=" + tabela + ", nomeRestricao=" + nomeRestricao + ", condicao=" + condicao + "]";
	}

}
