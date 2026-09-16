package Engine;

public class ErroLexer extends RuntimeException {
    private final int linha;
    private final int coluna;

    public ErroLexer(String mensagem, int linha, int coluna) {
        super(mensagem + " (linha " + linha + ", coluna " + coluna + ")");
        this.linha = linha;
        this.coluna = coluna;
    }

    public int getLinha() { return linha; }
    public int getColuna() { return coluna; }
}
