/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Comandos;

/**
 *
 * @author Etec
 */
public class Excluir implements Comando{
    private final String nomeTabela;
    private final String condicao;

    public Excluir(String nomeTabela, String condicao) {
        this.nomeTabela = nomeTabela;
        this.condicao = condicao;
    }
    public String getNomeTabela() {
        return nomeTabela;
    }
    public String condicao() {
        return condicao;
    }
    @Override
    public String toString() {
        return "Excluir[nomeTabela=" + nomeTabela + ", condicao=" + condicao + "]";
    }
}
