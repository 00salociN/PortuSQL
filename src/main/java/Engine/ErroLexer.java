package Engine;

/**
 * excecao lancada pelo {@link Lexer} quando encontra um caractere invalido
 * ou um token mal formado no codigo PortuSQL.
 *
 * <p>a mensagem inclui a posicao (linha e coluna) onde o erro foi encontrado.
 *
 * @author Nicolas Gabriel Venâncio dos Santos
 * @version 1.0
 * @see Lexer
 */
public class ErroLexer extends RuntimeException {
    /** linha onde o erro lexico foi encontrado */
    private final int linha;
    /** coluna onde o erro lexico foi encontrado */
    private final int coluna;

    /**
     * construtor que inicializa a mensagem e a posicao do erro
     * @param mensagem descricao do erro
     * @param linha linha onde o erro foi encontrado
     * @param coluna coluna onde o erro foi encontrado
     */
    public ErroLexer(String mensagem, int linha, int coluna) {
        super(mensagem + " (linha " + linha + ", coluna " + coluna + ")");
        this.linha = linha;
        this.coluna = coluna;
    }

    /**
     * retorna a linha onde o erro foi encontrado
     * @return linha do erro
     */
    public int getLinha() { return linha; }

    /**
     * retorna a coluna onde o erro foi encontrado
     * @return coluna do erro
     */
    public int getColuna() { return coluna; }
}