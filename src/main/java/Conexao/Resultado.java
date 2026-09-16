/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Conexao;

import java.util.List;

/**
 *
 * @author Etec
 */
public class Resultado {
    private final boolean consulta;
    private final List<String> colunas;
    private final List<List<Object>> linhas;
    private final int linhasAfetadas;
    
    private Resultado(boolean consulta, List<String> colunas, List<List<Object>> linhas, int linhasAfetadas){
        this.consulta = consulta;
        this.colunas = colunas;
        this.linhas = linhas;
        this.linhasAfetadas = linhasAfetadas;
    }
    public static Resultado Consulta(List<String> colunas, List<List<Object>> linhas){
        return new Resultado(true, colunas, linhas, -1);
    }
    public static Resultado Comando(int linhasAfetadas){
        return new Resultado(false, null, null, linhasAfetadas);
    }
    public boolean isConsulta(){
        return consulta;
    }
    public List<String> getColunas(){
        return colunas;
    }
    public List<List<Object>> getLinhas(){
        return linhas;
    }
    public int getLinhasAfetadas(){
        return linhasAfetadas;
    }
    public String toString(){
        if(consulta){
            return "Resultado[consulta, colunas=" + colunas + ", " + linhas.size() + " linha(s)]";
        }
        return "Resultado[comando, " + linhasAfetadas + " linha(s) afetada(s)]";
    }
    
}
