package PortuSQL.PortuSQL;

import java.sql.SQLException;
import java.util.List;

import Comandos.Comando;
import Conexao.Executor;
import Conexao.Resultado;
import Engine.Lexer;
import Engine.Parser;
import Engine.Token;
import Engine.Tradutor;

/**
 * Ponto único de entrada do motor PortuSQL.
 *
 * <p>Reúne as quatro etapas usadas pela interface gráfica:
 * {@link Lexer} → {@link Parser} → {@link Tradutor} → {@link Executor}.
  * @version 1.1.0
 */
public final class Interpretador {

    private Interpretador() {
    }

    /**
     * traduz e executa um comando em PortuSQL no banco atual.
     *
     * @param comandoPortusql texto em PortuSQL, sem o {@code ;} final
     * @return {@link ResultadoExecucao} com resultado, SQL gerado e tempo
     * @throws SQLException se o H2 rejeitar o comando
     * @throws RuntimeException se o texto falhar no Lexer, Parser ou Tradutor
     */
    public static ResultadoExecucao executar(String comandoPortusql) throws SQLException {
        if (comandoPortusql == null || comandoPortusql.isBlank()) {
            throw new IllegalArgumentException("Comando vazio.");
        }

        List<Token> tokens = new Lexer(comandoPortusql).tokenizar();
        Comando comando = new Parser(tokens).parserComando();
        String sqlH2 = new Tradutor().traducao(comando);

        long inicio = System.nanoTime();
        Resultado resultado = new Executor().executar(sqlH2);
        long milissegundos = (System.nanoTime() - inicio) / 1_000_000;

        return new ResultadoExecucao(resultado, sqlH2, milissegundos);
    }
}
