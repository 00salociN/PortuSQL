package PortuSQL.PortuSQL;

import Conexao.Resultado;

/**
 * Resultado de uma execução completa do motor PortuSQL.
 *
 * <p>Além do {@link Resultado} devolvido pelo banco, guarda o SQL gerado pelo
 * {@code Engine.Tradutor} e o tempo gasto na execução — os mesmos dados que a
 * interface gráfica mostra na aba "Mensagens".
  * @version 1.1.0
 */
public final class ResultadoExecucao {

    private final Resultado resultado;
    private final String sqlH2;
    private final long milissegundos;

    public ResultadoExecucao(Resultado resultado, String sqlH2, long milissegundos) {
        this.resultado = resultado;
        this.sqlH2 = sqlH2;
        this.milissegundos = milissegundos;
    }

    /** @return resultado da consulta ou comando */
    public Resultado getResultado() {
        return resultado;
    }

    /** @return SQL já traduzido e executado no H2 */
    public String getSqlH2() {
        return sqlH2;
    }

    /** @return tempo de execução em milissegundos */
    public long getMilissegundos() {
        return milissegundos;
    }
}
