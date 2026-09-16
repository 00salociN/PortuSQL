/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Comandos;

/**
 *
 * @author Etec
 */
public class RemoverRestricao implements Comando{
    private final String tabela;
    private final String nomeRestricao;

    public RemoverRestricao(String tabela, String nomeRestricao) {
        this.tabela = tabela;
        this.nomeRestricao = nomeRestricao;
    }
    public String getTabela() {
        return tabela;
    }
    public String getNomeRestricao() {
        return nomeRestricao;
    }
    @Override
    public String toString() {
        return"RemoverRestricao[tabela=" + tabela + ", nomeRestricao" + nomeRestricao + "]";
    }
}
