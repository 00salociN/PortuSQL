package Engine;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import Comandos.*;
import java.util.List;

public class Parser {
	private final List<Token> tokens;
	private int posicao = 0;
	
	public Parser(List<Token> tokens) {
		this.tokens = tokens;
	}
	public Comando parserComando() {
		Token atual = tokenAtual();
		if(atual.getTipo() != TipoToken.palavra_chave) {
			throw new ErroParser("Comando não encontrado '" + atual.getLexema() + "'", atual.getLinha(), atual.getColuna());
		}
		switch(atual.getLexema().toUpperCase()) {
		case"SELECIONAR":
			return parserSelecionar();
		case"CRIAR":
			return parserCriarTabela();
		case"INSERIR":
			return parserInserir();
		case"ATUALIZAR":
			return parserAtualizar();
		case"EXCLUIR":
			return parserExcluir();
//		case"DELETAR":
//			return parserDeletar();
		case"REMOVER":
			return parserRemoverTabela();
		case"ALTERAR":
			return parserAlterarTabela();
		case"ESVAZIAR":
			return parserEsvaziarTabela();
		case"MOSTRAR":
			return parserMostrar();
			default:
				throw new ErroParser("Comando não reconhecido: '" + atual.getLexema() + "'", atual.getLinha(), atual.getColuna());
		}
	}
	private DefinicaoColuna parserDefinicaoColuna() {
		String nomeColuna = esperarIdentificador();
		Token TokenTipo = tokenAtual();
		if(TokenTipo.getTipo() != TipoToken.tipo_dado) {
			throw new ErroParser("Esperava um tipo de dado após o nome da coluna '" + nomeColuna + "'", TokenTipo.getLinha(), TokenTipo.getColuna());
		}
		String tipoDado = TokenTipo.getLexema();
		avancar();
		
		boolean chavePrimaria = false;
		if(tokenAtual().getTipo() == TipoToken.palavra_chave && tokenAtual().getLexema().equalsIgnoreCase("CHAVE")) {
			avancar();
			esperarLexema("PRIMARIA");
			chavePrimaria = true;
		}
		return new DefinicaoColuna(nomeColuna, tipoDado, chavePrimaria);
		
	}
	private Selecionar parserSelecionar() {
	    esperarLexema("SELECIONAR");
	    List<String> colunas = new ArrayList<>();
	    colunas.add(esperarIdentificadorAsterisco());
	    while(tokenAtual().getTipo() == TipoToken.virgula) {
	        avancar();
	        colunas.add(esperarIdentificadorAsterisco());
	    }
	    esperarLexema("DE");
	    String tabela = esperarIdentificador();
	    String condicao = parserCondicao();
	    return new Selecionar(tabela, colunas, condicao);
	}
	private CriarTabela parserCriarTabela() {
		esperarLexema("CRIAR");
		esperarLexema("TABELA");
		String nomeTabela = esperarIdentificador();
		esperarTipo(TipoToken.parenteses_esquerda, "(");
		List<DefinicaoColuna> colunas = new ArrayList<>();
		colunas.add(parserDefinicaoColuna());
		while (tokenAtual().getTipo() == TipoToken.virgula) {
			avancar();
			colunas.add(parserDefinicaoColuna());
		}
		esperarTipo(TipoToken.parenteses_direita, ")");
		return new CriarTabela(nomeTabela, colunas);
	}
	private Inserir parserInserir() {
		esperarLexema("INSERIR");
		List<String> colunas = new ArrayList<>();
		if(tokenAtual().getTipo() == TipoToken.parenteses_esquerda) {
			avancar();
			colunas.add(esperarIdentificador());
			while (tokenAtual().getTipo() == TipoToken.virgula) {
				avancar();
				colunas.add(esperarIdentificador());
			}
			esperarTipo(TipoToken.parenteses_direita, ")");
		}
		esperarLexema("EM");
		String tabela = esperarIdentificador();
		esperarLexema("VALORES");
		esperarTipo(TipoToken.parenteses_esquerda, "(");
		List<String> valores = new ArrayList<>();
		valores.add(esperarValor());
		while(tokenAtual().getTipo() == TipoToken.virgula) {
			avancar();
			valores.add(esperarValor());
		}
		esperarTipo(TipoToken.parenteses_direita, ")");
		return new Inserir(tabela, colunas, valores);
	}
	private String esperarValor() {
		Token atual = tokenAtual();
		if(atual.getTipo() == TipoToken.numerico || atual.getTipo() == TipoToken.texto) {
			avancar();
			return atual.getLexema();
		}
		throw new ErroParser("Era esperado um valor (numerico ou texto), mas não foi encontrado '" + atual.getLexema() + "'", atual.getLinha(), atual.getColuna());
		
	}
	private Atualizar parserAtualizar() {
		esperarLexema("ATUALIZAR");
		String tabela = esperarIdentificador();
		esperarLexema("DEFINIR");
		Map<String, String> atribuicoes = new LinkedHashMap<>();
		parserAtribuicoes(atribuicoes);
		while(tokenAtual().getTipo() == TipoToken.virgula) {
			avancar();
			parserAtribuicoes(atribuicoes);
		}
		String condicao = parserCondicao();
		return new Atualizar (tabela, atribuicoes, condicao);
	}
	
	private void parserAtribuicoes(Map<String, String> atribuicoes) {
		String coluna = esperarIdentificador();
		esperarTipo(TipoToken.operador_simples, "=");
		String valor = esperarValor();
		atribuicoes.put(coluna, valor);
	}
	private Excluir parserExcluir() {
		esperarLexema("EXCLUIR");
		esperarLexema("DE");
		String tabela = esperarIdentificador();
		String condicao = parserCondicao();
		return new Excluir (tabela, condicao);
	}
	 //private Deletar parserDeletar() {
		// ainda preciso arrumar o Deletar anterior (tudo) XDXD
	// }
	private RemoverTabela parserRemoverTabela() {
		esperarLexema("REMOVER");
		esperarLexema("TABELA");
		String nomeTabela = esperarIdentificador();
		return new RemoverTabela(nomeTabela);
	}
	private EsvaziarDadosTabela parserEsvaziarTabela() {
		esperarLexema("ESVAZIAR");
		esperarLexema("TABELA");
		String nomeTabela = esperarIdentificador();
		return new EsvaziarDadosTabela(nomeTabela);
	}
	private Mostrar parserMostrar() {
		esperarLexema("MOSTRAR");
		String alvo = esperarIdentificador();
		return new Mostrar(alvo);
	}
	private Comando parserAlterarTabela() {
		esperarLexema("ALTERAR");
		esperarLexema("TABELA");
		String tabela = esperarIdentificador();
		Token acao = tokenAtual();
		switch(acao.getLexema().toUpperCase()) {
		case "ADICIONAR":
			avancar();
			return parserAdicionar(tabela);
		case "REMOVER":
			avancar();
			return parserRemover(tabela);
		case "ALTERAR":
			avancar();
			return parserAlterarColuna(tabela);
		case "RENOMEAR":
			avancar();
			return parserRenomear(tabela);
			default:
				throw new ErroParser("Esperava ADICIONAR, REMOVER, ALTERAR ou RENOMEAR apos ALTERAR TABELA " + tabela, acao.getLinha(), acao.getColuna());
			
		}
	}
	private Comando parserAdicionar(String tabela) {
		Token proximo = tokenAtual();
		switch (proximo.getLexema().toUpperCase()) {
		case "COLUNA":
			avancar();
			DefinicaoColuna novaColuna = parserDefinicaoColuna();
			return new AdicionarColuna(tabela, novaColuna);
		case "RESTRICAO":
			avancar();
			String nomeRestricao = esperarIdentificador();
			String definicao = capturaTexto();
			return new AdicionarRestricao(tabela, nomeRestricao, definicao);
			default:
				throw new ErroParser("Esperava COLUNA ou RESTRICAO após ADICIONAR", proximo.getLinha(), proximo.getColuna());
		}
	}
	private Comando parserRemover(String tabela) {
		Token proximo = tokenAtual();
		switch (proximo.getLexema().toUpperCase()) {
		case "COLUNA":
			avancar();
			String nomeColuna = esperarIdentificador();
			return new RemoverColuna(tabela, nomeColuna);
		case "RESTRICAO":
			avancar();
			String nomeRestricao = esperarIdentificador();
			return new RemoverRestricao(tabela, nomeRestricao);
			default:
				throw new ErroParser("Esperava COLUNA ou RESTRICAO após REMOVER", proximo.getLinha(), proximo.getColuna());
		}
	}
	private Comando parserRenomear(String tabela) {
		Token proximo = tokenAtual();
		if(proximo.getLexema().equalsIgnoreCase("COLUNA")) {
			avancar();
			String nomeAntigo = esperarIdentificador();
			esperarLexema("PARA");
			String nomeNovo = esperarIdentificador();
			return new RenomearColuna(tabela, nomeAntigo, nomeNovo);
		}
		if(proximo.getLexema().equalsIgnoreCase("RESTRICAO")) {
			avancar();
			String nomeAntigo = esperarIdentificador();
			esperarLexema("PARA");
			String nomeNovo = esperarIdentificador();
			return new RenomearRestricao(tabela, nomeAntigo, nomeNovo);
		}
		esperarLexema("PARA");
		String nomeNovo = esperarIdentificador();
		return new RenomearTabela(tabela, nomeNovo);
	}
	private AlterarColuna parserAlterarColuna(String tabela) {
		esperarLexema("COLUNA");
		String nomeColuna = esperarIdentificador();
		Token tokenTipo = tokenAtual();
		if(tokenTipo.getTipo() != TipoToken.tipo_dado) {
			throw new ErroParser("Esperava um tipo de dado após o nome da coluna '" + nomeColuna + "'", tokenTipo.getLinha(), tokenTipo.getColuna());
		}
		avancar();
		return new AlterarColuna(tabela, nomeColuna, tokenTipo.getLexema());
	}
	private String capturaTexto() {
		StringBuilder texto = new StringBuilder();
		while(tokenAtual().getTipo() != TipoToken.fim) {
			texto.append(tokenAtual().getLexema()).append(" ");
			avancar();
		}
		return texto.toString().trim();
	}
	private String parserCondicao() {
		if(tokenAtual().getTipo() == TipoToken.palavra_chave && tokenAtual().getLexema().equalsIgnoreCase("ONDE")) {
			avancar();
			return capturaTexto();
		}
		return null;
	}
	private Token tokenAtual() {
		return tokens.get(posicao);
	}
	private void avancar() {
		if(posicao < tokens.size() - 1) {
			posicao ++;
		}
	}
	private void esperarLexema(String lexemaEsperado) {
		Token atual = tokenAtual();
		if(atual.getTipo() != TipoToken.palavra_chave || !atual.getLexema().equalsIgnoreCase(lexemaEsperado)) {
			throw new ErroParser("Esperava '" + lexemaEsperado + "', mas encontrei '" + atual.getLexema() + "'", atual.getLinha(), atual.getColuna());
		}
		avancar();
	}
	private String esperarIdentificador() {
		Token atual = tokenAtual();
		if(atual.getTipo() != TipoToken.identificador) {
			throw new ErroParser("Esperava um identificador (nome de tabela, coluna, etc.), mas encontrei '" + atual.getLexema() + "'", atual.getLinha(), atual.getColuna());
		}
		avancar();
		return atual.getLexema();
	}
	private void esperarTipo(TipoToken tipoEsperado, String descricao) {
		Token atual = tokenAtual();
		if(atual.getTipo() != tipoEsperado) {
			throw new ErroParser("Esperava '" + descricao + ", mas encontrei '" + atual.getLexema() + "'", atual.getLinha(), atual.getColuna());
		}
		avancar();
	}
	
private String esperarIdentificadorAsterisco() {
	Token atual = tokenAtual();
	if(atual.getTipo() == TipoToken.operador_simples && atual.getLexema().equals("*")) {
		avancar();
		return "*";
	}
	return esperarIdentificador();
}
}