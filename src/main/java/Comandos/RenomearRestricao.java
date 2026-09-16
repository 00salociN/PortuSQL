/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Comandos;

/**
 *
 * @author Etec
 */
public class RenomearRestricao implements Comando{
    private final String tabela;
    private final String nomeAntigo;
    private final String nomeNovo;

    public RenomearRestricao(String tabela, String nomeAntigo, String nomeNovo) {
        this.tabela = tabela;
        this.nomeAntigo = nomeAntigo;
        this.nomeNovo = nomeNovo;
    }
    public String getTabela(){
        return tabela;
    }
    public String getNomeAntigo(){
        return nomeAntigo;
    }
    public String getNomeNovo() {
        return nomeNovo;
    }
    @Override
    public String toString(){
        return"RenomearRestricao[tabela=" + tabela + ", nomeAntigo" + nomeAntigo + ", nomeNovo" + nomeNovo + "]";
    }    
}
