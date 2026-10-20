package Comandos;

import java.util.List;

/**
 * comando que seleciona colunas de uma tabela
 * 
 * <p>sintaxe:
 * <pre>{@code
 * SELECIONAR <coluna1, coluna2, ...> DE <nome_tabela>
 * 		ONDE <condicao>
 * 		AGRUPAR POR <coluna1, coluna2, ...>
 * 		ORDENAR POR <coluna1, coluna2, ...>
 * }</pre>
 * 
 * <p>todas as clausulas apos o nome da tabela sao opcionais
 * 
 * <p>implementa a interface {@link Comando}
 * 
 * @author Nicolas Gabriel Venâncio dos Santos
 * @version 1.0
 * @see Comando
 */
public class Selecionar implements Comando{
    /**
     * a escolha do private final permite que o objeto seja imutável 
     * private: impede o acesso de outros arquivos
     * final: impede a reatribuição do valor
     */
    /** nome da tabela consultada */
    private final String tabela;
    /** colunas desejadas (ou "*" para todas) */
    private final List<FuncaoAgregada> colunas;
    /** condicao da consulta (opcional) */
    private final String condicao;
    /** colunas do AGRUPAR POR (opcional) */
    private final List<String> agruparPor;
    /** colunas do ORDENAR POR (opcional) */
    private final List<String> ordenarPor;
    /** indica se será distinct */
    private boolean distinct;

    /**
     * construtor que inicializa os atributos
     * @param tabela nome da tabela consultada
     * @param colunas colunas desejadas
     * @param condicao condicao da consulta (opcional)
     * @param agruparPor lista de colunas do agrupamento (AGRUPAR POR), opcional
     * @param ordenarPor lista de colunas da ordenacao (ORDENAR POR), opcional
     */
    public Selecionar(String tabela, List<FuncaoAgregada> colunas, String condicao, List<String> agruparPor, List<String> ordenarPor) {
        this.tabela = tabela;
        this.colunas = colunas;
        this.condicao = condicao;
        this.agruparPor = agruparPor;
        this.ordenarPor = ordenarPor;
        this.distinct = false;
    }
    
    public Selecionar(String tabela, List<FuncaoAgregada> colunas, String condicao, List<String> agruparPor, List<String> ordenarPor, boolean distinct) {
        this.tabela = tabela;
        this.colunas = colunas;
        this.condicao = condicao;
        this.agruparPor = agruparPor;
        this.ordenarPor = ordenarPor;
        this.distinct = distinct;
    }

    /**
     * retorna o nome da tabela consultada
     * @return nome da tabela
     */
    public String getTabela() {
        return tabela;
    }

    /**
     * retorna as colunas desejadas
     * @return {@link List} com as colunas (podendo conter "*")
     */
    public List<FuncaoAgregada> getColunas() {
        return colunas;
    }

    /**
     * retorna a condicao da consulta, se houver
     * @return condicao da consulta
     */
    public String getCondicao() {
        return condicao;
    }
   
    /**
     * retorna as colunas que serao agrupadas, se houver
     * @return {@link List} das colunas do AGRUPAR POR
     */
    public List<String> getAgruparPor() {
    	return agruparPor;
    }
    
    /**
     * retorna as colunas da ordenacao, se houver
     * @return {@link List} das colunas do ORDENAR POR
     */
    public List<String> getOrdenarPor(){
    	return ordenarPor;
    }
    
    /**
     * retorna se será distinct
     * @return distinct
     */
    public boolean isDistinct() {
    	return distinct;
    }

    /**
     * retorna uma representação no formato String o comando Selecionar
     * <p>formato: {@code "Selecionar[tabela=" + tabela + ", colunas=" + colunas + ", condicao=" + condicao + ", agruparPor=" + agruparPor + ", ordenarPor=" + ordenarPor + ", distinct=" + distinct + "]"}
     * @return representação em forma de texto do comando
     */
    @Override
    public String toString() {
        return "Selecionar[tabela=" + tabela + ", colunas=" + colunas + ", condicao=" + condicao + ", agruparPor=" + agruparPor + ", ordenarPor=" + ordenarPor + ", distinct=" + distinct + "]";
        
    
    }
}