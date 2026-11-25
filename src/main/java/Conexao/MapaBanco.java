package Conexao;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * retrato das tabelas, colunas e chaves estrangeiras de um banco PortuSQL.
 *
 * <p>os dados vem do {@link DatabaseMetaData} do H2, ou seja: o mesmo
 * esquema que o navegador de bancos ja mostra. Esta classe nao altera o
 * banco -- apenas le metadados.
 *
 * @version 1.1.0
 * @param banco nome do banco lido
 * @param tabelas tabelas do esquema PUBLIC
 * @param relacionamentos chaves estrangeiras entre tabelas
 */
public record MapaBanco(
		String banco,
		List<TabelaDer> tabelas,
		List<RelacionamentoDer> relacionamentos) {

	/** tabela com suas colunas, na ordem em que foram definidas. */
	public record TabelaDer(String nome, List<ColunaDer> colunas) {
	}

	/** coluna de uma tabela, com a indicacao de pertencer a chave primaria. */
	public record ColunaDer(String nome, String tipo, boolean chavePrimaria) {
	}

	/** ligacao entre uma coluna FK e a coluna PK que ela referencia. */
	public record RelacionamentoDer(
			String tabelaOrigem,
			String colunaOrigem,
			String tabelaDestino,
			String colunaDestino,
			String nomeRestricao) {
	}

	/** mapa vazio, usado quando ainda nao ha banco selecionado. */
	public static MapaBanco vazio(String banco) {
		return new MapaBanco(banco, List.of(), List.of());
	}

	/**
	 * le os metadados do banco informado. Se {@code nomeBanco} for nulo ou
	 * vazio, usa a conexao atual (inclusive o banco padrao do PortuSQL).
	 *
	 * @param nomeBanco nome do banco, ou null para o banco atual
	 * @return retrato do esquema PUBLIC
	 * @throws SQLException se a leitura dos metadados falhar
	 */
	public static MapaBanco ler(String nomeBanco) throws SQLException {
		boolean usarAtual = nomeBanco == null || nomeBanco.isBlank();

		// Nunca usar obterConexao(nomeBanco) aqui: ele troca a conexao global e
		// poderia fazer o proximo comando do usuario rodar no banco errado.
		Connection conexao;
		String banco;
		if (usarAtual) {
			conexao = Conexao.obterConexaoAtual();
			banco = Conexao.getNomeBancoAtual() != null ? Conexao.getNomeBancoAtual() : "padrão";
		} else {
			conexao = Conexao.abrirConexaoIsolada(nomeBanco);
			banco = nomeBanco;
		}

		try {
			String catalogo = conexao.getCatalog();
			DatabaseMetaData meta = conexao.getMetaData();

			List<String> nomesTabelas = new ArrayList<>();
			try (ResultSet tabelas = meta.getTables(
					catalogo, "PUBLIC", "%", new String[]{"TABLE"})) {
				while (tabelas.next()) {
					nomesTabelas.add(tabelas.getString("TABLE_NAME"));
				}
			}

			List<TabelaDer> tabelasDer = new ArrayList<>();
			for (String nomeTabela : nomesTabelas) {
				List<ColunaDer> colunas = new ArrayList<>();
				try (ResultSet rs = meta.getColumns(catalogo, "PUBLIC", nomeTabela, "%")) {
					while (rs.next()) {
						colunas.add(new ColunaDer(
								rs.getString("COLUMN_NAME"),
								Conexao.traduzirTipo(rs.getString("TYPE_NAME")),
								false));
					}
				}

				Set<String> chavesPrimarias = new HashSet<>();
				try (ResultSet rs = meta.getPrimaryKeys(catalogo, "PUBLIC", nomeTabela)) {
					while (rs.next()) {
						chavesPrimarias.add(rs.getString("COLUMN_NAME"));
					}
				}

				List<ColunaDer> comChaves = new ArrayList<>();
				for (ColunaDer coluna : colunas) {
					comChaves.add(new ColunaDer(
							coluna.nome(), coluna.tipo(), chavesPrimarias.contains(coluna.nome())));
				}
				tabelasDer.add(new TabelaDer(nomeTabela, List.copyOf(comChaves)));
			}

			List<RelacionamentoDer> relacionamentos = new ArrayList<>();
			for (String nomeTabela : nomesTabelas) {
				try (ResultSet rs = meta.getImportedKeys(catalogo, "PUBLIC", nomeTabela)) {
					while (rs.next()) {
						relacionamentos.add(new RelacionamentoDer(
								rs.getString("FKTABLE_NAME"),
								rs.getString("FKCOLUMN_NAME"),
								rs.getString("PKTABLE_NAME"),
								rs.getString("PKCOLUMN_NAME"),
								rs.getString("FK_NAME")));
					}
				}
			}

			return new MapaBanco(banco, List.copyOf(tabelasDer), List.copyOf(relacionamentos));
		} finally {
			if (!usarAtual) {
				conexao.close();
			}
		}
	}
}
