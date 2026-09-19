package Comandos;
/**
 * comando que exclui um banco de dados
 * 
 * <p>sintaxe:
 * <pre>{@code
 * EXCLUIR BANCO DE DADOS <nome_banco>
 * }</pre>
 * 
 * <p> implementa a interface {@link Comando}
 * 
 * @author Nicolas Gabriel Venâncio dos Santos
 * @version 1.0
 * @see Comando
 */
public class ExcluirBancoDados implements Comando{
	
	/** 
	 * a escolha do private final permite que o objeto seja imutável 
	 * private: impede o acesso de outros arquivos
	 * final: impede a reatribuição do valor
	 */
	
	/** nome do banco */
	private final String nomeBanco;
	
	/**
	 * construtor que inicializa os atributos
	 * @param nomeBanco nome do banco de dados
	 */
	public ExcluirBancoDados (String nomeBanco) {
		this.nomeBanco = nomeBanco;
	}
	
	/**
	 * retorna o nome do banco de será excluido
	 * @return nome do banco
	 */
	public String getNomeBanco() {
		return nomeBanco;
	}
	
	/**
	 * retorna uma representação no formato String o comando ExcluirBancoDados
	 * <p>formato: {@code "ExcluirBancoDados[nomeBanco=" + nomeBanco + "]"}
	 * @return epresentação em forma de texto do comando
	 */
	@Override
	public String toString() {
		return "ExcluirBancoDados[nomeBanco=" + nomeBanco + "]";
	}
}
