/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Comandos;

/**
 *
 * @author Etec
 */
public class FuncaoAgregada { //preciso encontrar um nome para essa classe
    
    private final String funcao;
    private final boolean distinguir;
    private final String argumento;
    
    public FuncaoAgregada(String funcao, boolean distinguir, String argumento){
        this.funcao = funcao;
        this.distinguir = distinguir;
        this.argumento = argumento;
        
    } 
    
    public static FuncaoAgregada coluna(String argumento){
        return new FuncaoAgregada(null, false, argumento);
    }
    
    public String getFuncao(){
        return funcao;
    }
    public boolean isDistinguir(){
        return distinguir;
    }
    public String getArgumento(){
        return argumento;
    }
    public boolean isFuncao(){
       return funcao != null;
    }
}
