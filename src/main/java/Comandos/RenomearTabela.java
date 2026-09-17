package Comandos;

/**
 * @author Nicolas Gabriel Venâncio dos Santos
 * @version 1.0
 *
 * sintaxe: ALTERAR TABELA <nome_tabela> RENOMEAR PARA <nome_novo>
 * este comando altera o nome de uma tabela
 * 
 * implementa a interface {@link Comando}
 */
public class RenomearTabela implements Comando{
	/** 
 	* a escolha do private final permite que o objeto seja imutável 
 	* private: impede o acesso de outros arquivos
 	* final: impede a reatribuição do valor
 	*/
	private final String nomeAntigo;
	private final String nomeNovo;

	/** 
	* construtor que inicializa os atributos
	* @param nomeAntigo nome atual da tabela
	* @param nomeNovo novo nome da tabela
	public RenomearTabela (String nomeAntigo, String nomeNovo) {
		this.nomeAntigo = nomeAntigo;
		this.nomeNovo = nomeNovo;
	}

	/**
	* retorna o nome atual da tabela
	* @return nome da tabela
	*/
	public String getNomeAntigo() {
		return nomeAntigo;
	}

	/**
	* retorna o novo nome da tabela
	* @return novo nome da tabela
	*/
	public String getNomeNovo() {
		return nomeNovo;
	}

	/**
	* retorna uma representação no formato String do objeto final do CriarTabela
	* formato: ("RenomearTabela[nomeAntigo=" + nomeAntigo + "nomeNovo=" + nomeNovo)
	*
	* @return representação em forma de texto do objeto
	*/
	@Override
	public String toString() {
		return ("RenomearTabela[nomeAntigo=" + nomeAntigo + "nomeNovo=" + nomeNovo);
	}
    
}
