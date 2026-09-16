/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Comandos;

import java.util.List;

/**
 *
 * @author Etec
 */
public class Selecionar implements Comando{
    private final String tabela;
    private final List<String> colunas;
    private final String condicao;

    public Selecionar(String tabela, List<String> colunas, String condicao) {
        this.tabela = tabela;
        this.colunas = colunas;
        this.condicao = condicao;
    }
    public String getTabela() {
        return tabela;
    }
    public List<String> getColunas() {
        return colunas;
    }
    public String getCondicao() {
        return condicao;
    }

    @Override
    public String toString() {
        return "Selecionar[tabela=" + tabela + ", colunas=" + colunas + ", condicao=" + condicao + "]";
        
    
    }
}
