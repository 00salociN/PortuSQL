/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Comandos;

/**
 *
 * @author Etec
 */
public class RemoverTabela implements Comando{
    private final String nomeTabela;

    public RemoverTabela(String nomeTabela) {
        this.nomeTabela = nomeTabela;
    }
    public String getNomeTabela() {
        return nomeTabela;
    }

    @Override
    public String toString() {
        return "RemoverTabela[tabela=" + nomeTabela + "]";
    }
    
}
