/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Comandos;

/**
 *
 * @author Etec
 */
public class RemoverColuna implements Comando{
    private final String tabela;
    private final String nomeColuna;
    public RemoverColuna(String tabela, String nomeColuna) {
        this.tabela = tabela;
        this.nomeColuna = nomeColuna;
    }
    public String getTabela() {
        return tabela;
    }
    public String getNomeColuna() {
        return nomeColuna;
    }

    @Override
    public String toString() {
        return "RemoverColuna[tabela=" + tabela + ", coluna=" + nomeColuna + "]";
    }
}
