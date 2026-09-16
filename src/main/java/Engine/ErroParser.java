package Engine;

public class ErroParser extends RuntimeException{
	private static final long serialVersionUID = 1L;
	private final int linha;
	private final int coluna;
	
	public ErroParser(String mensagem, int linha, int coluna) {
		super(String.format("%s (linha %d, coluna %d)", mensagem, linha, coluna));
		this.linha = linha;
		this.coluna = coluna;
	}
	public int getLinha() {
		return linha;
	}
	public int getColuna() {
		return coluna;
	}

}
