package Comandos;

public class AdicionarRestricaoVerificar implements Comando{
	
	private final String tabela;
	private final String nomeRestricao;
	private final String condicao;
	
	
	public AdicionarRestricaoVerificar(String tabela, String nomeRestricao, String condicao) {
		this.tabela = tabela;
		this.nomeRestricao = nomeRestricao;
		this.condicao = condicao;
	}
	
	public String getTabela() {
		return tabela;
	}
	public String getNomeRestricao() {
		return nomeRestricao;
	}
	public String condicao() {
		return condicao;
	}
	
	@Override
	public String toString() {
		return "AdicionarRestricaoVerificar[tabela=" + tabela + ", nomeRestricao=" + nomeRestricao + ", condicao=" + condicao + "]";
	}

}
