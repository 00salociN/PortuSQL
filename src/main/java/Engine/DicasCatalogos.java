package Engine;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * textos curtos usados no tooltip da IDE.
 *
 * <p>a busca tem tres niveis: alias exato (casado como subsequencia, para
 * ignorar identificadores no meio), visao geral dos comandos relacionados e,
 * por fim, a categoria do catálogo alinhado.
  * @version 1.1.0
 */
public final class DicasCatalogos {

	private static final List<Documentacao> TODOS = List.of(
			new Documentacao("criar-tabela", "CRIAR TABELA",
					"Quando você quer criar uma nova tabela — ou seja, definir a estrutura de armazenamento de um conjunto de dados (colunas, tipos e restrições).",
					"CRIAR TABELA alunos ( id inteiro CHAVE PRIMARIA, nome texto, idade inteiro )",
					List.of("criar tabela", "criar")),

			new Documentacao("alterar-adicionar-coluna", "ALTERAR TABELA — ADICIONAR COLUNA",
					"Quando você precisa incluir uma coluna nova em uma tabela que já existe.",
					"ALTERAR TABELA alunos ADICIONAR COLUNA email texto",
					List.of("alterar tabela adicionar coluna", "alterar adicionar coluna", "adicionar coluna")),

			new Documentacao("alterar-adicionar-restricao", "ALTERAR TABELA — ADICIONAR RESTRICAO",
					"Quando você quer adicionar uma restrição nomeada a uma coluna de uma tabela que já existe, sem refazer a tabela.",
					"ALTERAR TABELA alunos ADICIONAR RESTRICAO pk_id id chave primaria",
					List.of("alterar tabela adicionar restricao", "alterar adicionar restricao", "adicionar restricao")),

			new Documentacao("alterar-adicionar-restricao-verificar", "ALTERAR TABELA — ADICIONAR RESTRICAO ... VERIFICAR",
					"Quando você quer uma restrição de verificação (CHECK), que valida uma condição sobre as linhas (ex.: idade sempre positiva).",
					"ALTERAR TABELA alunos ADICIONAR RESTRICAO ck_idade VERIFICAR idade > 0",
					List.of("alterar tabela adicionar restricao verificar", "adicionar restricao verificar")),

			new Documentacao("alterar-remover-coluna", "ALTERAR TABELA — REMOVER COLUNA",
					"Quando você quer apagar uma coluna (e todos os dados dela) de uma tabela existente.",
					"ALTERAR TABELA alunos REMOVER COLUNA email",
					List.of("alterar tabela remover coluna", "alterar remover coluna", "remover coluna")),

			new Documentacao("alterar-remover-restricao", "ALTERAR TABELA — REMOVER RESTRICAO",
					"Quando você quer tirar uma restrição nomeada (chave, unicidade, etc.) de uma tabela.",
					"ALTERAR TABELA alunos REMOVER RESTRICAO fk_turma",
					List.of("alterar tabela remover restricao", "alterar remover restricao", "remover restricao")),

			new Documentacao("alterar-alterar-coluna", "ALTERAR TABELA — ALTERAR COLUNA",
					"Quando você quer mudar o tipo de dado de uma coluna existente (ex.: inteiro → decimal).",
					"ALTERAR TABELA alunos ALTERAR COLUNA idade decimal",
					List.of("alterar tabela alterar coluna", "alterar coluna")),

			new Documentacao("tipo-caractere-variavel", "CARACTERE/TEXTO VARIAVEL(n)",
					"Tipo de texto com tamanho máximo. O número entre parênteses é o limite de caracteres; traduz para VARCHAR(n) no H2.",
					"CRIAR TABELA alunos ( nome CARACTERE VARIAVEL(80) )",
					List.of("caractere variavel", "texto variavel", "variavel")),

			new Documentacao("tipo-caractere-fixo", "CARACTERE/TEXTO FIXO(n)",
					"Tipo de texto com tamanho exato. O número entre parênteses define quantos caracteres a coluna ocupa; traduz para CHAR(n) no H2.",
					"CRIAR TABELA alunos ( uf CARACTERE FIXO(2) )",
					List.of("caractere fixo", "texto fixo", "fixo")),

			new Documentacao("alterar-renomear-coluna", "ALTERAR TABELA — RENOMEAR COLUNA",
					"Quando você quer dar um novo nome a uma coluna existente (ex.: nome → nome_completo).",
					"ALTERAR TABELA alunos RENOMEAR COLUNA nome PARA nome_completo",
					List.of("alterar tabela renomear coluna", "renomear coluna")),

			new Documentacao("alterar-renomear-restricao", "ALTERAR TABELA — RENOMEAR RESTRICAO",
					"Quando você quer renomear uma restrição nomeada.",
					"ALTERAR TABELA alunos RENOMEAR RESTRICAO fk_turma PARA fk_turma_aluno",
					List.of("alterar tabela renomear restricao", "renomear restricao")),

			new Documentacao("alterar-renomear-para", "ALTERAR TABELA — RENOMEAR PARA (tabela)",
					"Quando você quer renomear a própria tabela (ex.: alunos → estudantes).",
					"ALTERAR TABELA alunos RENOMEAR PARA estudantes",
					List.of("alterar tabela renomear para", "renomear para")),

			new Documentacao("esvaziar-tabela", "ESVAZIAR TABELA",
					"Quando você quer apagar todas as linhas da tabela, mas manter a estrutura (colunas) para uso futuro.",
					"ESVAZIAR TABELA alunos",
					List.of("esvaziar tabela", "esvaziar")),

			new Documentacao("excluir-tabela", "EXCLUIR TABELA",
					"Quando você quer remover a tabela inteira — estrutura e dados — do banco.",
					"EXCLUIR TABELA alunos",
					List.of("excluir tabela")),

			new Documentacao("inserir", "INSERIR",
					"Quando você quer adicionar uma linha nova com dados em uma tabela.",
					"INSERIR (nome, idade) EM alunos VALORES ('Joao', 20)",
					List.of("inserir")),

			new Documentacao("atualizar", "ATUALIZAR",
					"Quando você quer modificar valores de linhas que já existem na tabela.",
					"atualizar alunos DEFINIR idade = 21 ONDE nome PARECIDO COM 'Joao'",
					List.of("atualizar")),

			new Documentacao("deletar", "DELETAR",
					"Quando você quer remover linhas de uma tabela, mantendo a estrutura.",
					"DELETAR DE alunos ONDE idade > 18",
					List.of("deletar")),

			new Documentacao("selecionar", "SELECIONAR",
					"Quando você quer consultar/ler dados do banco — é o comando mais usado do dia a dia. Aceita funções agregadas: CONTAR, SOMA, MED, MIN e MAX.",
					"SELECIONAR * DE alunos",
					List.of("selecionar")),

			new Documentacao("mostrar-tabela", "MOSTRAR TABELA",
					"Quando você quer ver a estrutura de uma tabela (suas colunas e seus metadados).",
					"MOSTRAR TABELA alunos",
					List.of("mostrar tabela")),

			new Documentacao("mostrar-coluna", "MOSTRAR COLUNA",
					"Quando você quer ver os detalhes de uma coluna específica de uma tabela.",
					"MOSTRAR COLUNA idade DE alunos",
					List.of("mostrar coluna")),

			new Documentacao("usar-banco-de-dados", "USAR BANCO DE DADOS",
					"Quando você quer definir qual banco de dados será usado pelo PortuSQL.",
					"USAR BANCO DE DADOS loja",
					List.of("usar banco de dados", "usar banco", "usar")),

			new Documentacao("excluir-banco-de-dados", "EXCLUIR BANCO DE DADOS",
					"Quando você quer apagar um banco de dados inteiro.",
					"EXCLUIR BANCO DE DADOS escola",
					List.of("excluir banco de dados", "excluir banco")),

			new Documentacao("funcao-contar", "CONTAR",
					"Conta linhas ou valores de uma coluna. CONTAR(*) conta todas as linhas; CONTAR(DISTINCT cidade) conta valores distintos. Traduz para COUNT no SQL.",
					"SELECIONAR CONTAR(*) DE alunos",
					List.of("contar")),

			new Documentacao("funcao-soma", "SOMA",
					"Soma os valores de uma coluna. Traduz para SUM no SQL.",
					"SELECIONAR SOMA(valor) DE vendas",
					List.of("soma")),

			new Documentacao("funcao-med", "MED",
					"Calcula a média aritmética dos valores de uma coluna. Traduz para AVG no SQL.",
					"SELECIONAR MED(idade) DE alunos",
					List.of("med")),

			new Documentacao("funcao-min", "MIN",
					"Retorna o menor valor de uma coluna. Traduz para MIN no SQL.",
					"SELECIONAR MIN(idade) DE alunos",
					List.of("min")),

			new Documentacao("funcao-max", "MAX",
					"Retorna o maior valor de uma coluna. Traduz para MAX no SQL.",
					"SELECIONAR MAX(idade) DE alunos",
					List.of("max"))
	);

	/** alias normalizado -> documentacao */
	private static final Map<String, Documentacao> POR_CHAVE = new LinkedHashMap<>();
	/** id -> documentacao */
	private static final Map<String, Documentacao> POR_ID = new LinkedHashMap<>();
	/** palavra -> documentacoes que a contem no nome ou em algum alias */
	private static final Map<String, Set<Documentacao>> POR_PALAVRA = new LinkedHashMap<>();

	static {
		for (Documentacao d : TODOS) {
			POR_ID.put(d.id(), d);
			POR_CHAVE.put(normalizar(d.nome()), d);
			adicionarPalavras(normalizar(d.nome()), d);

			for (String alias : d.aliases()) {
				POR_CHAVE.put(normalizar(alias), d);
				adicionarPalavras(normalizar(alias), d);
			}
		}

		relacionar("chave", "criar-tabela", "alterar-adicionar-restricao",
				"alterar-adicionar-restricao-verificar", "alterar-remover-restricao",
				"alterar-renomear-restricao");
		relacionar("primaria", "criar-tabela", "alterar-adicionar-restricao",
				"alterar-adicionar-restricao-verificar", "alterar-remover-restricao",
				"alterar-renomear-restricao");
		relacionar("estrangeira", "alterar-adicionar-restricao");
		relacionar("unica", "alterar-adicionar-restricao");
		relacionar("verificar", "alterar-adicionar-restricao-verificar");
		relacionar("referencia", "alterar-adicionar-restricao");
		relacionar("restricao", "criar-tabela", "alterar-adicionar-restricao",
				"alterar-adicionar-restricao-verificar", "alterar-remover-restricao",
				"alterar-renomear-restricao");

		relacionar("onde", "selecionar", "atualizar", "deletar");
		relacionar("agrupar", "selecionar");
		relacionar("ordenar", "selecionar");
		relacionar("distinct", "selecionar");
		relacionar("valores", "inserir");
		relacionar("definir", "atualizar");
		relacionar("banco", "usar-banco-de-dados", "excluir-banco-de-dados");
	}

	private DicasCatalogos() {
	}

	private static void adicionarPalavras(String texto, Documentacao d) {
		for (String termo : texto.split("\\s+")) {
			if (!termo.isBlank()) {
				POR_PALAVRA.computeIfAbsent(termo, k -> new LinkedHashSet<>()).add(d);
			}
		}
	}

	private static void relacionar(String termo, String... ids) {
		Set<Documentacao> docs = POR_PALAVRA.computeIfAbsent(termo, k -> new LinkedHashSet<>());
		for (String id : ids) {
			Documentacao d = POR_ID.get(id);
			if (d != null) docs.add(d);
		}
	}

	private static String normalizar(String s) {
		if (s == null) return "";
		return s.toLowerCase()
				.replace("—", " ")
				.replace("...", " ")
				.replaceAll("\\([^)]*\\)", "")
				.replaceAll("\\s+", " ")
				.trim();
	}

	/** casamento por subsequencia: permite ignorar identificadores no meio. */
	private static boolean contemSubsequencia(String[] palavras, String[] alias) {
		int j = 0;
		for (String p : palavras) {
			if (j < alias.length && p.equals(alias[j])) j++;
			if (j == alias.length) return true;
		}
		return false;
	}

	private static boolean contemPalavra(String[] alias, String alvo) {
		for (String p : alias) {
			if (p.equals(alvo)) return true;
		}
		return false;
	}

	private static String nomesEmLinhas(Set<Documentacao> docs) {
		return docs.stream()
				.map(Documentacao::nome)
				.distinct()
				.map(nome -> "• " + nome)
				.collect(Collectors.joining("\n"));
	}

	private static String exemploRepresentativo(String primeira, Set<Documentacao> docs) {
		return docs.stream()
				.filter(d -> d.nome().toLowerCase().startsWith(primeira))
				.map(Documentacao::exemplo)
				.filter(e -> e != null && !e.isBlank())
				.findFirst()
				.orElseGet(() -> docs.stream()
						.map(Documentacao::exemplo)
						.filter(e -> e != null && !e.isBlank())
						.findFirst()
						.orElse(""));
	}

	/** busca usando a primeira palavra como foco (compatibilidade). */
	public static Optional<Documentacao> buscar(String... palavras) {
		if (palavras == null || palavras.length == 0) return Optional.empty();
		return buscarComFoco(palavras[0], palavras);
	}

	/**
	 * busca a dica para a palavra sob o mouse, usando o entorno para resolver
	 * comandos completos mesmo quando ha identificadores no meio.
	 *
	 * @param foco palavra sob o mouse
	 * @param contexto palavras da janela (anteriores + foco + posteriores)
	 */
	public static Optional<Documentacao> buscarComFoco(String foco, String... contexto) {
		String alvo = normalizar(foco);
		List<String> palavras = contexto == null ? List.of()
				: Arrays.stream(contexto)
						.map(DicasCatalogos::normalizar)
						.filter(p -> !p.isBlank())
						.flatMap(p -> Arrays.stream(p.split("\\s+")))
						.toList();

		Optional<Documentacao> alias = melhorAlias(alvo, palavras);
		if (alias.isPresent()) return alias;
		if (alvo.isBlank()) return Optional.empty();

		// nivel 2: visao geral dos comandos relacionados a palavra sob o mouse
		Set<Documentacao> relacionados = POR_PALAVRA.get(alvo);
		if (relacionados != null && !relacionados.isEmpty()) {
			return Optional.of(new Documentacao(
					alvo,
					alvo.toUpperCase(),
					"Comandos relacionados:\n" + nomesEmLinhas(relacionados),
					exemploRepresentativo(alvo, relacionados),
					List.of()));
		}

		// nivel 3: categoria no catalogo alinhado
		for (InfoComandos c : CatalogoComandos.todos()) {
			if (c.nome().equals(alvo)) {
				String resumo = switch (c.categoria()) {
					case PALAVRA_CHAVE -> "Palavra-chave da linguagem PortuSQL.";
					case TIPO_DADO -> "Tipo de dado PortuSQL.";
					case FUNCAO -> "Função agregada PortuSQL.";
					default -> "Elemento da linguagem PortuSQL.";
				};
				return Optional.of(new Documentacao(c.id(), c.nome(), resumo, "", List.of()));
			}
		}
		return Optional.empty();
	}

	/**
	 * Prefere aliases que contenham a palavra sob o mouse; entre eles, vence
	 * o mais longo. Se nenhum contiver o foco, usa o alias mais longo casado.
	 */
	private static Optional<Documentacao> melhorAlias(String alvo, List<String> contexto) {
		String[] palavras = contexto.toArray(String[]::new);
		Documentacao melhor = null;
		int melhorTamanho = 0;

		if (!alvo.isBlank()) {
			for (Map.Entry<String, Documentacao> e : POR_CHAVE.entrySet()) {
				String[] alias = e.getKey().split("\\s+");
				if (alias.length <= melhorTamanho || !contemPalavra(alias, alvo)) continue;
				if (contemSubsequencia(palavras, alias)) {
					melhor = e.getValue();
					melhorTamanho = alias.length;
				}
			}
			if (melhor != null) return Optional.of(melhor);
		}

		melhor = null;
		melhorTamanho = 0;
		for (Map.Entry<String, Documentacao> e : POR_CHAVE.entrySet()) {
			String[] alias = e.getKey().split("\\s+");
			if (alias.length <= melhorTamanho) continue;
			if (contemSubsequencia(palavras, alias)) {
				melhor = e.getValue();
				melhorTamanho = alias.length;
			}
		}
		return Optional.ofNullable(melhor);
	}
}
