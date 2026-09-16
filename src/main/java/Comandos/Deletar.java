/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Comandos;

/**
 *
 * @author Etec
 */
public class Deletar implements Comando{
    private final String tabela;
    private final String condicao;

    public Deletar(String tabela, String condicao) {
        this.tabela = tabela;
        this.condicao = condicao;
    }
    public String getTabela() {
        return tabela;
    }
    public String getCondicao(){
        return condicao;
    }
    @Override
    public String toString() {
        return "Deletar[tabela=" + tabela + ", condicao=" + condicao + "]";
    }
    
}
