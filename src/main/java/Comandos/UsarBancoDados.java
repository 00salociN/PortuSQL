package Comandos;

/**
 * comando que troca o banco de dados atual
 * 
 * <p>sintaxe:
 * <pre> {@code
 * USAR BANCO DE DADOS <nome_banco>
 * }</pre>
 * 
 * <p>implementa a interface {@link Comando}
 * 
 * @author Nicolas Gabriel Venâncio dos Santos
 * @version 1.0
 * @see Comando
 */
public class UsarBancoDados implements Comando{
	
	/** 
	 * a escolha do private final permite que o objeto seja imutável 
	 * private: impede o acesso de outros arquivos
	 * final: impede a reatribuição do valor
	 */
	
	/** nome do banco de dados */
	private final String nomeBanco;
	
	/**
	 * construtor que inicializa os atributos
	 * @param nomeBanco nome do banco
	 */
	public UsarBancoDados(String nomeBanco) {
		this.nomeBanco = nomeBanco;
	}
	
	/**
	 * retorna o nome da tabela
	 * @return nome da tabela
	 */
	public String getNomeBanco() {
		return nomeBanco;
	}
	
	/**
	 * retorna uma representação no formato String o comando UsarBancoDados
	 * <p>formato: {@code UsarBancoDados[nomeBanco=" + nomeBanco + "]"}
	 * @return representação em forma de texto do comando
	 */
	@Override
	public String toString() {
		return "UsarBancoDados[nomeBanco=" + nomeBanco + "]";
	}

}
