package Engine;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import Comandos.*;
import java.util.List;

/**
 * analisador sintatico (parser) do PortuSQL.
 *
 * <p>recebe a lista de {@link Token} produzida pelo {@link Lexer} e a
 * transforma em um objeto {@link Comando}, montando cada comando a partir
 * de sua gramatica especifica.
 *
 * <p>caso o texto nao siga a sintaxe esperada, lanca {@link ErroParser} com
 * a posicao (linha e coluna) do problema.
 *
 * @author Nicolas Gabriel Venâncio dos Santos
 * @version 1.0
 * @see Lexer
 * @see Comando
 * @see ErroParser
 */
public class Parser {
	/** lista de tokens do comando a ser analisado */
	private final List<Token> tokens;
	/** posicao atual na lista de tokens */
	private int posicao = 0;
	
	/**
	 * construtor que inicializa a lista de tokens a ser analisada
	 * @param tokens tokens produzidos pelo {@link Lexer}
	 */
	public Parser(List<Token> tokens) {
		this.tokens = tokens;
	}

	/**
	 * analisa o primeiro comando da lista de tokens
	 * @return {@link Comando} montado conforme a palavra-chave inicial
	 * @throws ErroParser se a palavra inicial nao iniciar um comando conhecido
	 */
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
		case"DELETAR":
			return parserDeletar();
		case"ALTERAR":
			return parserAlterarTabela();
		case"ESVAZIAR":
			return parserEsvaziarTabela();
		case"MOSTRAR":
			return parserMostrar();
		case"USAR":
			return parserUsarBancoDados();
			default:
				throw new ErroParser("Comando não reconhecido: '" + atual.getLexema() + "'", atual.getLinha(), atual.getColuna());
		}
	}

	/**
	 * analisa a definicao de uma coluna: nome, tipo e restricoes opcionais
	 * @return {@link DefinicaoColuna} montada
	 */
	private DefinicaoColuna parserDefinicaoColuna() {
		String nomeColuna = esperarIdentificador();
		Token TokenTipo = tokenAtual();
		String tipoDado;
		
		if(TokenTipo.getTipo() == TipoToken.palavra_chave && TokenTipo.getLexema().equalsIgnoreCase("CARACTERE")){
			avancar();
			if(tokenAtual().getTipo() == TipoToken.palavra_chave && tokenAtual().getLexema().equalsIgnoreCase("VARIAVEL")) {
				avancar();
				esperarTipo(TipoToken.parenteses_esquerda, "(");
				if(tokenAtual().getTipo() != TipoToken.numerico) {
					throw new ErroParser("Esperarav um número após '(' em CARACTERE VARIAVEL", tokenAtual().getLinha(), tokenAtual().getColuna());
				}
				String tamanho = tokenAtual().getLexema();
				avancar();
				esperarTipo(TipoToken.parenteses_direita, ")");
				tipoDado = "VARCHAR(" + tamanho + ")";
			} else if(tokenAtual().getTipo() == TipoToken.palavra_chave && tokenAtual().getLexema().equalsIgnoreCase("FIXO")) {
				avancar();
				esperarTipo(TipoToken.parenteses_esquerda, "(");
				if(tokenAtual().getTipo() != TipoToken.numerico) {
					throw new ErroParser("Esperava um número após '(' em CARACTERE FIXO", tokenAtual().getLinha(), tokenAtual().getColuna());
				}
				String tamanho = tokenAtual().getLexema();
				avancar();
				esperarTipo(TipoToken.parenteses_direita, ")");
				tipoDado = "CHAR(" + tamanho + ")";
			} else {
				throw new ErroParser("Esperava 'VARIAVEL' ou 'FIXO' após 'CARACTERE'", tokenAtual().getLinha(), tokenAtual().getColuna());				
			}
		} else if (TokenTipo.getTipo() == TipoToken.tipo_dado) {
			tipoDado = TokenTipo.getLexema();
			avancar();
		} else {
			throw new ErroParser("Esperava um tipo de dado após o nome da coluna '" + nomeColuna + "'", tokenAtual().getLinha(), tokenAtual().getColuna());
		}

		boolean chavePrimaria = false;
		boolean naoNulo = false;
		while(true) {
			Token atual = tokenAtual();
			if(tokenAtual().getTipo() == TipoToken.palavra_chave && tokenAtual().getLexema().equalsIgnoreCase("CHAVE")) {
				avancar();
				esperarLexema("PRIMARIA");
				chavePrimaria = true;
				continue;
		}
			if(atual.getTipo() == TipoToken.palavra_chave && atual.getLexema().equalsIgnoreCase("NAO")) {
				avancar();
				esperarLexema("NULO");
				naoNulo = true;
				continue;
			}
			break;
		}
			
		return new DefinicaoColuna(nomeColuna, tipoDado, chavePrimaria, naoNulo);
		
	}

	/**
	 * analisa o comando SELECIONAR
	 * @return {@link Selecionar} montado
	 */
	private Selecionar parserSelecionar() {
	    esperarLexema("SELECIONAR");
	    
	    boolean distinct = false;
	    if(tokenAtual().getTipo() == TipoToken.palavra_chave && tokenAtual().getLexema().equalsIgnoreCase("DISTINCT")) {
	        distinct = true;
	        avancar();
	    }
	    
	    List<FuncaoAgregada> colunas = new ArrayList<>();
	    colunas.add(lerItemSelecionar());
	    while(tokenAtual().getTipo() == TipoToken.virgula) {
	        avancar();
	        colunas.add(lerItemSelecionar());
	    }
	    esperarLexema("DE");
	    String tabela = esperarIdentificador();
	    String condicao = parserCondicao();
	    List<String> agruparPor = parserListaColunasOpcional("AGRUPAR", "POR");
	    List<String> ordenarPor = parserListaColunasOpcional("ORDENAR", "POR");
	    return new Selecionar(tabela, colunas, condicao, agruparPor, ordenarPor, distinct);
	}
	
	private FuncaoAgregada lerItemSelecionar() {
	    Token atual = tokenAtual();

	    if (atual.getTipo() == TipoToken.operador_simples && atual.getLexema().equals("*")) {
	        avancar();
	        return FuncaoAgregada.coluna("*");
	    }
	    
	    if (atual.getTipo() == TipoToken.funcao_agregada) {
	        String lex = atual.getLexema().toLowerCase();
	        String nomeFuncao = NomeFuncao(lex);
	        avancar(); 
	        esperarTipo(TipoToken.parenteses_esquerda, "(");
	        
	        boolean distinguir = false;
	        if (tokenAtual().getTipo() == TipoToken.palavra_chave && tokenAtual().getLexema().equalsIgnoreCase("DISTINCT")) {
	            distinguir = true;
	            avancar();
	        }
	        
	        String argumento;
	        if (tokenAtual().getTipo() == TipoToken.operador_simples && tokenAtual().getLexema().equals("*")) {
	            argumento = "*";
	            avancar();
	        } else {
	            argumento = esperarIdentificador();
	        }
	        
	        esperarTipo(TipoToken.parenteses_direita, ")");
	        return new FuncaoAgregada(nomeFuncao, distinguir, argumento);
	    }
	    
	    // Coluna simples
	    return FuncaoAgregada.coluna(esperarIdentificador());
	}
	 
	    
	    private String NomeFuncao(String lexema) {
	    	switch (lexema.toLowerCase()) {
	    	case "contar":
	    		return "COUNT";
	    	case "soma":
	    		return "SUM";
	    	case "med":
	    		return "AVG";
	    	case "min":
	    		return "MIN";
	    	case "max":
	    		return "MAX";
	    		default:
	    			return lexema.toUpperCase();
	    	}
	    }




	/**
	 * analisa o comando CRIAR TABELA
	 * @return {@link CriarTabela} montado
	 */
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

	/**
	 * analisa o comando INSERIR
	 * @return {@link Inserir} montado
	 */
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
		
		List<List<String>> valores = new ArrayList<>();
		valores.add(lerListaValores());
		
		while (tokenAtual().getTipo() == TipoToken.virgula) {
			avancar();
			valores.add(lerListaValores());
		}
		
		return new Inserir(tabela, colunas, valores);
	}
	
	private List<String> lerListaValores() {
		esperarTipo(TipoToken.parenteses_esquerda, "(");
		List<String> valores = new ArrayList<>();
		valores.add(esperarValor());
		while(tokenAtual().getTipo() == TipoToken.virgula) {
			avancar();
			valores.add(esperarValor());
		}
		esperarTipo(TipoToken.parenteses_direita, ")");
		return valores;
	}

	/**
	 * espera e le um valor (numerico ou texto)
	 * @return lexema do valor lido
	 * @throws ErroParser se o token atual nao for um valor
	 */
	private String esperarValor() {
		Token atual = tokenAtual();
		if(atual.getTipo() == TipoToken.numerico) {
			avancar();
			return atual.getLexema();
		}
		if(atual.getTipo() == TipoToken.texto) {
			avancar();
			return "'" + atual.getLexema() + "'";
		}
		if(atual.getTipo() == TipoToken.nulo) {
			avancar();
			return "null";
		}
		throw new ErroParser("Era esperado um valor (numerico, texto ou nulo), mas não foi encontrado '" + atual.getLexema() + "'", atual.getLinha(), atual.getColuna());
		
	}

	/**
	 * analisa o comando ATUALIZAR
	 * @return {@link Atualizar} montado
	 */
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
	
	/**
	 * le uma atribuicao {@code coluna = valor} e a guarda no mapa
	 * @param atribuicoes mapa onde a atribuicao sera armazenada
	 */
	private void parserAtribuicoes(Map<String, String> atribuicoes) {
		String coluna = esperarIdentificador();
		esperarTipo(TipoToken.operador_simples, "=");
		String valor = esperarValor();
		atribuicoes.put(coluna, valor);
	}
	
	/**
	 * analisa o comando EXCLUIR (tabela ou banco de dados)
	 * @return comando montado ({@link ExcluirTabela} ou {@link ExcluirBancoDados})
	 */
	private Comando parserExcluir() {
		esperarLexema("EXCLUIR");
		Token proximo = tokenAtual();
		switch(proximo.getLexema().toUpperCase()) {
		case "TABELA":
			avancar();
			String nomeTabela = esperarIdentificador();
			return new ExcluirTabela(nomeTabela);
		
		case "BANCO":
			avancar();
			esperarLexema("DE");
			esperarLexema("DADOS");
			String nomeBanco = esperarIdentificador();
			return new ExcluirBancoDados(nomeBanco);
			
			default: 
				throw new ErroParser("Esperava TABELA ou BANCO DE DADOS", proximo.getLinha(), proximo.getColuna());
			
		}
	}	
	
	/**
	 * analisa o comando DELETAR
	 * @return {@link Deletar} montado
	 */
	private Deletar parserDeletar() {
		esperarLexema("DELETAR");
		esperarLexema("DE");
		String tabela = esperarIdentificador();
		String condicao = parserCondicao();
		return new Deletar(tabela, condicao);
	}
	
	/**
	 * analisa o comando USAR BANCO DE DADOS
	 * @return {@link UsarBancoDados} montado
	 */
	private UsarBancoDados parserUsarBancoDados() {
		esperarLexema("USAR");
		esperarLexema("BANCO");
		esperarLexema("DE");
		esperarLexema("DADOS");
		String nomeBanco = esperarIdentificador();
		return new UsarBancoDados(nomeBanco);
	}
	
	/**
	 * analisa o comando ESVAZIAR TABELA
	 * @return {@link EsvaziarDadosTabela} montado
	 */
	private EsvaziarDadosTabela parserEsvaziarTabela() {
		esperarLexema("ESVAZIAR");
		esperarLexema("TABELA");
		String nomeTabela = esperarIdentificador();
		return new EsvaziarDadosTabela(nomeTabela);
	}

	/**
	 * analisa o comando MOSTRAR (tabela ou coluna)
	 * @return comando montado ({@link MostrarTabela} ou {@link MostrarColuna})
	 */
	private Comando parserMostrar() {
		esperarLexema("MOSTRAR");
		Token proximo = tokenAtual();
		switch(proximo.getLexema().toUpperCase()) {
		case "TABELA":
			avancar();
			String tabela = esperarIdentificador();
			return new MostrarTabela(tabela);
			
		case "COLUNA":
			avancar();
			String coluna = esperarIdentificador();
			esperarLexema("DE");
			String tabelacol = esperarIdentificador();
			return new MostrarColuna(tabelacol, coluna);
		default:
			throw new ErroParser("Esperava uma TABELA ou COLUNA após o mostrar", proximo.getLinha(), proximo.getColuna());
		}
	}

	/**
	 * analisa o comando ALTERAR TABELA e desvia para a acao pedida
	 * @return comando montado conforme a acao (adicionar, remover, alterar ou renomear)
	 */
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

	/**
	 * analisa a acao de adicionar (coluna ou restricao) em uma tabela
	 * @param tabela nome da tabela alterada
	 * @return comando montado ({@link AdicionarColuna}, {@link AdicionarRestricao}
	 *         ou {@link AdicionarRestricaoVerificar})
	 */
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
			if (tokenAtual().getTipo() == TipoToken.palavra_chave && tokenAtual().getLexema().equalsIgnoreCase("VERIFICAR")) {
				avancar();
				String condicao = capturaTexto();
				return new AdicionarRestricaoVerificar(tabela, nomeRestricao, condicao);
			}
			String coluna = esperarIdentificador();
			StringBuilder tipoRestricaoTexto = new StringBuilder();
			while (tokenAtual().getTipo() != TipoToken.fim && !tokenAtual().getLexema().equalsIgnoreCase("REFERENCIA")) {
				tipoRestricaoTexto.append(tokenAtual().getLexema()).append(" ");
				avancar();
			}
			String definicao = tipoRestricaoTexto.toString().trim();
			String tabelaReferenciada = null;
			String colunaReferenciada = null;
			if(tokenAtual().getLexema().equalsIgnoreCase("REFERENCIA")) {
				avancar();
				tabelaReferenciada = esperarIdentificador();
				esperarTipo(TipoToken.parenteses_esquerda, "(");
				colunaReferenciada = esperarIdentificador();
				esperarTipo(TipoToken.parenteses_direita, ")");
			}
			return new AdicionarRestricao(tabela, nomeRestricao, definicao, coluna, tabelaReferenciada, colunaReferenciada);
			default:
				throw new ErroParser("comando inválido", proximo.getLinha(), proximo.getColuna());
		}
	}

	/**
	 * analisa a acao de remover (coluna ou restricao) em uma tabela
	 * @param tabela nome da tabela alterada
	 * @return comando montado ({@link RemoverColuna} ou {@link RemoverRestricao})
	 */
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

	/**
	 * analisa a acao de renomear (coluna, restricao ou tabela)
	 * @param tabela nome da tabela alterada
	 * @return comando montado ({@link RenomearColuna}, {@link RenomearRestricao}
	 *         ou {@link RenomearTabela})
	 */
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

	/**
	 * analisa a alteracao do tipo de uma coluna
	 * @param tabela nome da tabela alterada
	 * @return {@link AlterarColuna} montado
	 */
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

	/**
	 * captura todo o texto restante do comando como uma unica string
	 * @return texto capturado ate o fim dos tokens
	 */
	private String capturaTexto() {
		StringBuilder texto = new StringBuilder();
		while(tokenAtual().getTipo() != TipoToken.fim) {
			if(tokenAtual().getTipo() == TipoToken.texto) {
				// valores de texto voltam com aspas, para o SQL gerado ficar valido
				texto.append("'").append(tokenAtual().getLexema()).append("' ");
			} else {
				texto.append(tokenAtual().getLexema()).append(" ");
			}
			avancar();
		}
		return texto.toString().trim();
	}

	/**
	 * verifica e captura uma condicao iniciada por ONDE
	 * @return texto da condicao, ou {@code null} se nao houver ONDE
	 */
	private String parserCondicao() {
		if(tokenAtual().getTipo() == TipoToken.palavra_chave && tokenAtual().getLexema().equalsIgnoreCase("ONDE")) {
			avancar();
			return capturaTexto();
		}
		return null;
	}

	/**
	 * retorna o token na posicao atual
	 * @return token atual
	 */
	private Token tokenAtual() {
		return tokens.get(posicao);
	}

	/**
	 * avanca para o proximo token, se ainda houver
	 */
	private void avancar() {
		if(posicao < tokens.size() - 1) {
			posicao ++;
		}
	}

	/**
	 * espera que o token atual seja a palavra-chave informada
	 * @param lexemaEsperado palavra-chave esperada
	 * @throws ErroParser se o token atual nao for a palavra esperada
	 */
	private void esperarLexema(String lexemaEsperado) {
		Token atual = tokenAtual();
		boolean corresponde;
		if(lexemaEsperado.equalsIgnoreCase("NULO")) {
			// NULO tem tipo de token proprio no Lexer (TipoToken.nulo)
			corresponde = atual.getTipo() == TipoToken.nulo
					|| (atual.getTipo() == TipoToken.palavra_chave && atual.getLexema().equalsIgnoreCase("NULO"));
		} else {
			corresponde = atual.getTipo() == TipoToken.palavra_chave && atual.getLexema().equalsIgnoreCase(lexemaEsperado);
		}
		if(!corresponde) {
			throw new ErroParser("Esperava '" + lexemaEsperado + "', mas encontrei '" + atual.getLexema() + "'", atual.getLinha(), atual.getColuna());
		}
		avancar();
	}

	/**
	 * espera e le um identificador
	 * @return lexema do identificador lido
	 * @throws ErroParser se o token atual nao for um identificador
	 */
	private String esperarIdentificador() {
		Token atual = tokenAtual();
		if(atual.getTipo() != TipoToken.identificador) {
			throw new ErroParser("Esperava um identificador (nome de tabela, coluna, etc.), mas encontrei '" + atual.getLexema() + "'", atual.getLinha(), atual.getColuna());
		}
		avancar();
		return atual.getLexema();
	}

	/**
	 * espera que o token atual seja de um tipo especifico
	 * @param tipoEsperado tipo de token esperado
	 * @param descricao descricao textual do token para a mensagem de erro
	 * @throws ErroParser se o token atual nao for do tipo esperado
	 */
	private void esperarTipo(TipoToken tipoEsperado, String descricao) {
		Token atual = tokenAtual();
		if(atual.getTipo() != tipoEsperado) {
			throw new ErroParser("Esperava '" + descricao + ", mas encontrei '" + atual.getLexema() + "'", atual.getLinha(), atual.getColuna());
		}
		avancar();
	}
	
	/**
	 * espera e le um identificador, permitindo tambem o asterisco (*)
	 * @return lexema lido ("*" ou o identificador)
	 */
	private String esperarIdentificadorAsterisco() {
		Token atual = tokenAtual();
		if(atual.getTipo() == TipoToken.operador_simples && atual.getLexema().equals("*")) {
			avancar();
			return "*";
		}
		return esperarIdentificador();
	}

	/**
	 * analisa uma lista opcional de colunas iniciada por duas palavras-chave
	 * (ex.: ORDENAR POR) e retorna as colunas em uma lista
	 * @param primeiraPalavra primeira palavra-chave da lista
	 * @param segundaPalavra segunda palavra-chave da lista
	 * @return lista de colunas (vazia se a sequencia nao estiver presente)
	 */
	private List<String> parserListaColunasOpcional(String primeiraPalavra, String segundaPalavra){
		if(tokenAtual().getTipo() == TipoToken.palavra_chave && tokenAtual().getLexema().equalsIgnoreCase(primeiraPalavra)) {
			avancar();
			esperarLexema(segundaPalavra);
			List<String> colunas = new ArrayList<>();
			colunas.add(esperarIdentificador());
			while (tokenAtual().getTipo() == TipoToken.virgula) {
				avancar();
				colunas.add(esperarIdentificador());
			}
			return colunas;
		}
		return new ArrayList<>();
	}
}