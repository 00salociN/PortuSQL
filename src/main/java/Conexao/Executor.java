package Conexao;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * executa um SQL ja traduzido contra o banco H2 conectado
 * ({@link Conexao#obterConexaoAtual()}).
 *
 * <p>responsavel por separar o resultado em dois casos:
 * <ul>
 *     <li>quando o SQL e uma consulta (SELECT), devolve um {@link Resultado}
 *     com colunas e linhas;</li>
 *     <li>caso contrario, devolve um {@link Resultado} com a quantidade de
 *     linhas afetadas.</li>
 * </ul>
 *
 * @author Nicolas Gabriel Venâncio dos Santos
 * @version 1.0
 * @see Resultado
 * @see Conexao
 */
public class Executor {

    /**
     * executa um comando SQL no banco atual e devolve o resultado
     * @param sql comando SQL (H2) pronto para execucao
     * @return {@link Resultado} de consulta ou de comando
     * @throws SQLException se o SQL falhar no banco
     */
    public Resultado executar(String sql) throws SQLException {
        try (Statement comando = Conexao.obterConexaoAtual().createStatement()) {
            boolean result = comando.execute(sql);
            if (result) {
                try (ResultSet resultSet = comando.getResultSet()) {
                    return ResultadoConsulta(resultSet);
                }
            }
            int linhasAfetadas = comando.getUpdateCount();
            return Resultado.Comando(linhasAfetadas);
        }
    }

    /**
     * monta um {@link Resultado} de consulta a partir do {@link ResultSet}
     * @param resultSet resultados retornados pelo banco
     * @return {@link Resultado} com colunas e linhas
     * @throws SQLException se houver falha ao ler os metadados ou as linhas
     */
    private Resultado ResultadoConsulta(ResultSet resultSet) throws SQLException {
        ResultSetMetaData metadados = resultSet.getMetaData();
        int quantidadeColunas = metadados.getColumnCount();
        List<String> colunas = new ArrayList<>();
        for (int i = 1; i <= quantidadeColunas; i++) {
            colunas.add(metadados.getColumnName(i));
        }
        List<List<Object>> linhas = new ArrayList<>();
        while (resultSet.next()) {
            List<Object> linha = new ArrayList<>();
            for (int i = 1; i <= quantidadeColunas; i++) {
                linha.add(resultSet.getObject(i));
            }
            linhas.add(linha);
        }
        return Resultado.Consulta(colunas, linhas);
    }
}