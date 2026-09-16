/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Comandos;

/**
 *
 * @author Etec
 */
public class RenomearTabela implements Comando{
	private final String nomeAntigo;
	private final String nomeNovo;
	
	public RenomearTabela (String nomeAntigo, String nomeNovo) {
		this.nomeAntigo = nomeAntigo;
		this.nomeNovo = nomeNovo;
	}
	
	public String getNomeAntigo() {
		return nomeAntigo;
	}
	public String getNomeNovo() {
		return nomeNovo;
	}
	@Override
	public String toString() {
		return ("RenomearTabela[nomeAntigo=" + nomeAntigo + "nomeNovo=" + nomeNovo);
	}
    
}
