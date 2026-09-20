package Conexao;

import java.util.List;

/**
 * encapsula o resultado de um comando executado no banco.
 *
 * <p>um resultado pode ser de dois tipos:
 * <ul>
 *     <li>consulta (SELECT): guarda os nomes das colunas e as linhas retornadas;</li>
 *     <li>comando (INSERT, UPDATE, ...): guarda a quantidade de linhas afetadas.</li>
 * </ul>
 *
 * @author Nicolas Gabriel Venâncio dos Santos
 * @version 1.0
 * @see Executor
 */
public class Resultado {
    /** {@code true} se o resultado veio de uma consulta (SELECT) */
    private final boolean consulta;
    /** nomes das colunas (apenas para consulta) */
    private final List<String> colunas;
    /** linhas retornadas pela consulta */
    private final List<List<Object>> linhas;
    /** quantidade de linhas afetadas (apenas para comando) */
    private final int linhasAfetadas;
    
    /**
     * construtor privado que inicializa os atributos
     * @param consulta {@code true} para resultado de consulta
     * @param colunas nomes das colunas
     * @param linhas linhas da consulta
     * @param linhasAfetadas quantidade de linhas afetadas
     */
    private Resultado(boolean consulta, List<String> colunas, List<List<Object>> linhas, int linhasAfetadas){
        this.consulta = consulta;
        this.colunas = colunas;
        this.linhas = linhas;
        this.linhasAfetadas = linhasAfetadas;
    }

    /**
     * cria um resultado do tipo consulta
     * @param colunas nomes das colunas retornadas
     * @param linhas linhas retornadas
     * @return {@link Resultado} de consulta
     */
    public static Resultado Consulta(List<String> colunas, List<List<Object>> linhas){
        return new Resultado(true, colunas, linhas, -1);
    }

    /**
     * cria um resultado do tipo comando
     * @param linhasAfetadas quantidade de linhas afetadas
     * @return {@link Resultado} de comando
     */
    public static Resultado Comando(int linhasAfetadas){
        return new Resultado(false, null, null, linhasAfetadas);
    }

    /**
     * verifica se o resultado e do tipo consulta
     * @return {@code true} se for uma consulta (SELECT)
     */
    public boolean isConsulta(){
        return consulta;
    }

    /**
     * retorna os nomes das colunas, se for uma consulta
     * @return {@link List} com os nomes das colunas
     */
    public List<String> getColunas(){
        return colunas;
    }

    /**
     * retorna as linhas da consulta
     * @return {@link List} de linhas, cada linha sendo uma {@link List} de valores
     */
    public List<List<Object>> getLinhas(){
        return linhas;
    }

    /**
     * retorna a quantidade de linhas afetadas, se for um comando
     * @return quantidade de linhas afetadas
     */
    public int getLinhasAfetadas(){
        return linhasAfetadas;
    }

    /**
     * retorna uma representação no formato String do resultado
     * <p>formato: {@code "Resultado[consulta, colunas=" + colunas + ", " + linhas.size() + " linha(s)]"}
     * ou {@code "Resultado[comando, " + linhasAfetadas + " linha(s) afetada(s)]"}
     * @return representação em forma de texto do resultado
     */
    public String toString(){
        if(consulta){
            return "Resultado[consulta, colunas=" + colunas + ", " + linhas.size() + " linha(s)]";
        }
        return "Resultado[comando, " + linhasAfetadas + " linha(s) afetada(s)]";
    }
    
}