package Engine;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * catalogo unico das palavras da linguagem PortuSQL.
 *
 * <p>toda palavra reconhecida pelo {@link Lexer} tambem e encontrada aqui,
 * e toda palavra destacada pela IDE sai desta mesma lista. A classificacao
 * define o uso: {@link Categoria#PALAVRA_CHAVE}, {@link Categoria#TIPO_DADO}
 * ou {@link Categoria#FUNCAO}.
 *
 * @author Nicolas Gabriel Venâncio dos Santos
 * @version 1.1.0
 * @see InfoComandos
 * @see KW
 */
public final class CatalogoComandos {

	private static InfoComandos palavra(String nome) {
		return new InfoComandos(nome, nome, Categoria.PALAVRA_CHAVE,
				List.of(), TipoToken.palavra_chave);
	}

	private static InfoComandos tipo(String nome) {
		return new InfoComandos(nome, nome, Categoria.TIPO_DADO,
				List.of(), TipoToken.tipo_dado);
	}

	private static InfoComandos funcao(String nome) {
		return new InfoComandos(nome, nome, Categoria.FUNCAO,
				List.of(), TipoToken.funcao_agregada);
	}

	private static final List<InfoComandos> TODOS = List.of(

			// palavras-chave
			palavra("criar"),
			palavra("tabela"),
			palavra("selecionar"),
			palavra("de"),
			palavra("onde"),
			palavra("ordenar"),
			palavra("por"),
			palavra("agrupar"),
			palavra("inserir"),
			palavra("valores"),
			palavra("atualizar"),
			palavra("deletar"),
			palavra("apagar"),
			palavra("e"),
			palavra("ou"),
			palavra("nao"),
			palavra("nulo"),
			palavra("chave"),
			palavra("primaria"),
			palavra("estrangeira"),
			palavra("alterar"),
			palavra("adicionar"),
			palavra("coluna"),
			palavra("renomear"),
			palavra("restricao"),
			palavra("definir"),
			palavra("remover"),
			palavra("excluir"),
			palavra("mostrar"),
			palavra("esvaziar"),
			palavra("em"),
			palavra("para"),
			palavra("referencia"),
			palavra("unica"),
			palavra("verificar"),
			palavra("banco"),
			palavra("dados"),
			palavra("usar"),
			palavra("distinct"),

			// tipos de dado
			tipo("int"),
			tipo("inteiro"),
			tipo("texto"),
			tipo("data"),
			tipo("decimal"),
			tipo("gint"),
			tipo("cint"),
			tipo("mint"),
			tipo("hora"),
			tipo("data_hora"),
			tipo("caractere"),
			tipo("variavel"),
			tipo("fixo"),
			tipo("flutuante"),
			tipo("booleano"),
			tipo("binario"),
			tipo("enum"),
			tipo("enumerar"),
			tipo("real"),
			tipo("numerico"),

			// funcoes agregadas
			funcao("max"),
			funcao("min"),
			funcao("soma"),
			funcao("med"),
			funcao("contar")
	);

	private CatalogoComandos() {
	}

	/** @return todas as entradas do catalogo */
	public static List<InfoComandos> todos() {
		return TODOS;
	}

	/** @return entradas classificadas como palavra-chave */
	public static List<InfoComandos> palavras() {
		return filtrar(Categoria.PALAVRA_CHAVE);
	}

	/** @return entradas classificadas como tipo de dado */
	public static List<InfoComandos> tipos() {
		return filtrar(Categoria.TIPO_DADO);
	}

	/** @return entradas classificadas como funcao */
	public static List<InfoComandos> funcoes() {
		return filtrar(Categoria.FUNCAO);
	}

	private static List<InfoComandos> filtrar(Categoria categoria) {
		return TODOS.stream()
				.filter(c -> c.categoria() == categoria)
				.toList();
	}

	/** @return palavras, funcoes e demais entradas que nao sao tipos (realce roxo) */
	public static Set<String> destaquesIDE() {
		Set<String> saida = new LinkedHashSet<>();
		for (InfoComandos c : TODOS) {
			if (c.categoria() != Categoria.TIPO_DADO) {
				saida.add(c.nome());
			}
		}
		return saida;
	}

	/** @return tipos de dado (realce azul) */
	public static Set<String> tiposDestaqueIDE() {
		Set<String> saida = new LinkedHashSet<>();
		for (InfoComandos c : tipos()) {
			saida.add(c.nome());
		}
		return saida;
	}
}
