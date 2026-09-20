package Engine;

/**
 * excecao lancada pelo {@link Parser} quando encontra uma sintaxe invalida
 * no comando PortuSQL.
 *
 * <p>a mensagem inclui a posicao (linha e coluna) onde o erro foi encontrado.
 *
 * @author Nicolas Gabriel Venâncio dos Santos
 * @version 1.0
 * @see Parser
 */
public class ErroParser extends RuntimeException{
	private static final long serialVersionUID = 1L;
	/** linha onde o erro de sintaxe foi encontrado */
	private final int linha;
	/** coluna onde o erro de sintaxe foi encontrado */
	private final int coluna;
	
	/**
	 * construtor que inicializa a mensagem e a posicao do erro
	 * @param mensagem descricao do erro
	 * @param linha linha onde o erro foi encontrado
	 * @param coluna coluna onde o erro foi encontrado
	 */
	public ErroParser(String mensagem, int linha, int coluna) {
		super(String.format("%s (linha %d, coluna %d)", mensagem, linha, coluna));
		this.linha = linha;
		this.coluna = coluna;
	}
	/**
	 * retorna a linha onde o erro foi encontrado
	 * @return linha do erro
	 */
	public int getLinha() {
		return linha;
	}
	/**
	 * retorna a coluna onde o erro foi encontrado
	 * @return coluna do erro
	 */
	public int getColuna() {
		return coluna;
	}

}