package Conexao;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Executor {

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
