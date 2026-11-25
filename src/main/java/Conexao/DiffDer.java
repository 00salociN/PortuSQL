package Conexao;

import Conexao.MapaBanco.ColunaDer;
import Conexao.MapaBanco.RelacionamentoDer;
import Conexao.MapaBanco.TabelaDer;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * diferenca entre dois retratos do mesmo banco, usada para destacar no DER
 * o que um comando acabou de criar, adicionar, remover ou modificar.
 *
 * @version 1.1.0
 * @param anterior retrato lido antes da execucao
 * @param atual retrato lido depois da execucao
 */
public record DiffDer(
		MapaBanco anterior,
		MapaBanco atual,
		List<TabelaDer> tabelasAdicionadas,
		List<TabelaDer> tabelasRemovidas,
		List<ColunaAlterada> colunasAdicionadas,
		List<ColunaAlterada> colunasRemovidas,
		List<ColunaModificada> colunasModificadas,
		List<RelacionamentoDer> fksAdicionadas,
		List<RelacionamentoDer> fksRemovidas) {

	/** coluna que entrou ou saiu de uma tabela. */
	public record ColunaAlterada(String tabela, ColunaDer coluna) {
	}

	/** coluna cujo tipo mudou (ex.: inteiro -> decimal). */
	public record ColunaModificada(
			String tabela, String coluna, String tipoAntigo, String tipoNovo) {
	}

	public static DiffDer entre(MapaBanco antes, MapaBanco depois) {
		MapaBanco a = antes == null ? MapaBanco.vazio("—") : antes;
		MapaBanco b = depois == null ? MapaBanco.vazio("—") : depois;

		Map<String, TabelaDer> tabA = tabelas(a);
		Map<String, TabelaDer> tabB = tabelas(b);

		List<TabelaDer> tabelasNovas = new ArrayList<>();
		List<TabelaDer> tabelasRemovidas = new ArrayList<>();
		for (Map.Entry<String, TabelaDer> e : tabB.entrySet()) {
			if (!tabA.containsKey(e.getKey())) tabelasNovas.add(e.getValue());
		}
		for (Map.Entry<String, TabelaDer> e : tabA.entrySet()) {
			if (!tabB.containsKey(e.getKey())) tabelasRemovidas.add(e.getValue());
		}

		Map<String, ColunaDer> colA = colunas(a);
		Map<String, ColunaDer> colB = colunas(b);
		List<ColunaAlterada> adicionadas = new ArrayList<>();
		List<ColunaAlterada> removidas = new ArrayList<>();
		List<ColunaModificada> modificadas = new ArrayList<>();

		for (Map.Entry<String, ColunaDer> e : colB.entrySet()) {
			ColunaDer antiga = colA.get(e.getKey());
			if (antiga == null) {
				adicionadas.add(new ColunaAlterada(tabelaDe(e.getKey()), e.getValue()));
			} else if (!iguais(antiga, e.getValue())) {
				modificadas.add(new ColunaModificada(
						tabelaDe(e.getKey()), e.getValue().nome(),
						antiga.tipo(), e.getValue().tipo()));
			}
		}
		for (Map.Entry<String, ColunaDer> e : colA.entrySet()) {
			if (!colB.containsKey(e.getKey())) {
				removidas.add(new ColunaAlterada(tabelaDe(e.getKey()), e.getValue()));
			}
		}

		Map<String, RelacionamentoDer> fkA = fks(a);
		Map<String, RelacionamentoDer> fkB = fks(b);
		List<RelacionamentoDer> fksNovas = new ArrayList<>();
		List<RelacionamentoDer> fksRemovidas = new ArrayList<>();
		for (Map.Entry<String, RelacionamentoDer> e : fkB.entrySet()) {
			if (!fkA.containsKey(e.getKey())) fksNovas.add(e.getValue());
		}
		for (Map.Entry<String, RelacionamentoDer> e : fkA.entrySet()) {
			if (!fkB.containsKey(e.getKey())) fksRemovidas.add(e.getValue());
		}

		return new DiffDer(a, b, List.copyOf(tabelasNovas), List.copyOf(tabelasRemovidas),
				List.copyOf(adicionadas), List.copyOf(removidas), List.copyOf(modificadas),
				List.copyOf(fksNovas), List.copyOf(fksRemovidas));
	}

	public boolean vazio() {
		return tabelasAdicionadas.isEmpty() && tabelasRemovidas.isEmpty()
				&& colunasAdicionadas.isEmpty() && colunasRemovidas.isEmpty()
				&& colunasModificadas.isEmpty()
				&& fksAdicionadas.isEmpty() && fksRemovidas.isEmpty();
	}

	/** resumo curto para o cabecalho do dock. */
	public String resumo() {
		List<String> partes = new ArrayList<>();
		if (!tabelasAdicionadas.isEmpty()) {
			partes.add(tabelasAdicionadas.size() + (tabelasAdicionadas.size() == 1
					? " tabela nova" : " tabelas novas"));
		}
		if (!tabelasRemovidas.isEmpty()) {
			partes.add(tabelasRemovidas.size() + (tabelasRemovidas.size() == 1
					? " tabela removida" : " tabelas removidas"));
		}
		if (!colunasAdicionadas.isEmpty()) {
			partes.add(colunasAdicionadas.size() + (colunasAdicionadas.size() == 1
					? " coluna +" : " colunas +"));
		}
		if (!colunasRemovidas.isEmpty()) {
			partes.add(colunasRemovidas.size() + (colunasRemovidas.size() == 1
					? " coluna -" : " colunas -"));
		}
		if (!colunasModificadas.isEmpty()) {
			partes.add(colunasModificadas.size() + (colunasModificadas.size() == 1
					? " tipo alterado" : " tipos alterados"));
		}
		if (!fksAdicionadas.isEmpty() || !fksRemovidas.isEmpty()) {
			int total = fksAdicionadas.size() + fksRemovidas.size();
			partes.add(total + (total == 1 ? " FK alterada" : " FKs alteradas"));
		}
		return partes.isEmpty() ? "Sem alterações recentes" : String.join(" · ", partes);
	}

	public boolean ehTabelaAdicionada(String nome) {
		return contemTabela(tabelasAdicionadas, nome);
	}

	public boolean ehTabelaRemovida(String nome) {
		return contemTabela(tabelasRemovidas, nome);
	}

	public boolean ehFkAdicionada(RelacionamentoDer relacao) {
		return contemFk(fksAdicionadas, relacao);
	}

	public boolean ehFkRemovida(RelacionamentoDer relacao) {
		return contemFk(fksRemovidas, relacao);
	}

	private static boolean contemTabela(List<TabelaDer> lista, String nome) {
		return lista.stream().anyMatch(t -> t.nome().equalsIgnoreCase(nome));
	}

	private static boolean contemFk(List<RelacionamentoDer> lista, RelacionamentoDer relacao) {
		String alvo = chaveFk(relacao);
		return lista.stream().anyMatch(r -> chaveFk(r).equals(alvo));
	}

	private static boolean iguais(ColunaDer a, ColunaDer b) {
		return a.tipo().equalsIgnoreCase(b.tipo()) && a.chavePrimaria() == b.chavePrimaria();
	}

	private static Map<String, TabelaDer> tabelas(MapaBanco mapa) {
		Map<String, TabelaDer> resultado = new LinkedHashMap<>();
		for (TabelaDer tabela : mapa.tabelas()) {
			resultado.put(tabela.nome().toUpperCase(), tabela);
		}
		return resultado;
	}

	private static Map<String, ColunaDer> colunas(MapaBanco mapa) {
		Map<String, ColunaDer> resultado = new LinkedHashMap<>();
		for (TabelaDer tabela : mapa.tabelas()) {
			for (ColunaDer coluna : tabela.colunas()) {
				resultado.put(chaveColuna(tabela.nome(), coluna.nome()), coluna);
			}
		}
		return resultado;
	}

	private static Map<String, RelacionamentoDer> fks(MapaBanco mapa) {
		Map<String, RelacionamentoDer> resultado = new LinkedHashMap<>();
		for (RelacionamentoDer relacao : mapa.relacionamentos()) {
			resultado.put(chaveFk(relacao), relacao);
		}
		return resultado;
	}

	private static String chaveColuna(String tabela, String coluna) {
		return tabela.toUpperCase() + "." + coluna.toUpperCase();
	}

	private static String tabelaDe(String chaveColuna) {
		return chaveColuna.substring(0, chaveColuna.indexOf('.'));
	}

	private static String chaveFk(RelacionamentoDer r) {
		return r.tabelaOrigem().toUpperCase() + "." + r.colunaOrigem().toUpperCase()
				+ "->" + r.tabelaDestino().toUpperCase() + "." + r.colunaDestino().toUpperCase();
	}

}
