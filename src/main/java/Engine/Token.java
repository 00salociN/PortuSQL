package Engine;

/**
 * Representa um unico token extraido de uma consulta PortuSQL.
 * Um token carrega o texto original (lexema), seu tipo e sua posicao.
 *
 * @author Nicolas Gabriel Venâncio dos Santos
 * @version 1.0
 * @see TipoToken
 */
public class Token {
    private final TipoToken tipo;
    private final String lexema;
    private final int linha;
    private final int coluna;

    public Token(TipoToken tipo, String lexema, int linha, int coluna) {
        this.tipo = tipo;
        this.lexema = lexema;
        this.linha = linha;
        this.coluna = coluna;
    }

    public TipoToken getTipo() { return tipo; }
    public String getLexema() { return lexema; }
    public int getLinha() { return linha; }
    public int getColuna() { return coluna; }

    @Override
    public String toString() {
        return String.format("Token[%s, '%s', linha=%d, coluna=%d]", tipo, lexema, linha, coluna);
    }
}
