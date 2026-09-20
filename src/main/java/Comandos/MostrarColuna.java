package Comandos;

/**
 * comando que mostrar os detalhes de uma coluna de uma tabela
 * 
 * <p>sintaxe:
 * <pre>{@code
 * MOSTRAR COLUNA <nome_coluna> DE <nome_tabela>
 * }</pre>
 * 
 * <p> implementa a interface {@link Comando}
 * 
 * @author Nicolas Gabriel Venâncio dos Santos
 * @version 1.0
 * @see Comando
 */
public class MostrarColuna implements Comando{
	
	/** 
	 * a escolha do private final permite que o objeto seja imutável 
	 * private: impede o acesso de outros arquivos
	 * final: impede a reatribuição do valor
	 */
	
	/** nome da coluna */
	private final String coluna;
	
	/** nome da tabela */
	private final String tabela;
	
	
	/**
	 * construtor que inicializa os atributos
	 * @param tabela nome da tabela
	 * @param coluna nome da coluna
	 */
	public MostrarColuna (String tabela, String coluna) {
		this.coluna = coluna;
		this.tabela = tabela;
	}
	
	/**
	 * retorna o nome da coluna
	 * @return nome da coluna
	 */
	public String getNomeColuna() {
		return coluna;
	}
	
	/**
	 * retorna o nome da tabela
	 * @return nome da tabela
	 */
	public String getNomeTabela() {
		return tabela;
	}
	
	/**
	 * retorna uma representação no formato String o comando MostrarColuna
	 * <p> formato: {@code "MostrarColuna[coluna=" + coluna + ", tabela=" + tabela + "]"}
	 * @return representação em forma de texto do comando
	 */
	@Override
	public String toString() {
		return "MostrarColuna[coluna=" + coluna + ", tabela=" + tabela + "]";
	}

}
