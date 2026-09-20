package Comandos;

/**
 * comando que mostra a estrutura de uma tabela
 * 
 * <p>sintaxe:
 * <pre>{@code
 * MOSTRAR TABELA <nome_tabela>
 * }</pre>
 * 
 * <p> implementa a interface {@link Comando}
 * 
 * @author Nicolas Gabriel Venâncio dos Santos
 * @version 1.0
 * @see Comando
 */
public class MostrarTabela implements Comando{
	
	/** 
	 * a escolha do private final permite que o objeto seja imutável 
	 * private: impede o acesso de outros arquivos
	 * final: impede a reatribuição do valor
	 */
	
	/** nome da tabela que será exibida */
	private final String tabela;
	
	/**
	 * construtor que inicializa os atributos
	 * @param tabela nome da tabela
	 */
	public MostrarTabela (String tabela) {
		this.tabela = tabela;
	}
	
	/**
	 * retorna o nome da tabela
	 * @return nome da tabela
	 */
	public String getNomeTabela() {
		return tabela;
	}
	
	/**
	 * retorna uma representação no formato String o comando MostrarTabela
	 * <p> formato: {@code "MostrarTabela[tabela=" + tabela + "]"}
	 * @return representação em forma de texto do comando 
	 */
	@Override
	public String toString() {
		return "MostrarTabela[tabela=" + tabela + "]";
	}

}
