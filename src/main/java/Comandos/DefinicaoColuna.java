package Comandos;


public class DefinicaoColuna {
    private final String nome;
    private final String tipoDado;
    private final boolean chavePrimaria;

    /**
     * consteutor de inicializa os atributos
     * @param nome nome da coluna
     * @param tipoDado tipo do dado da coluna
     * @param chavePrimaria tipo de dado boolean. se tiver pk chavePrimaria = true, por padrão é false
     */
    public DefinicaoColuna(String nome, String tipoDado, boolean chavePrimaria) {
        this.nome = nome;
        this.tipoDado = tipoDado;
        this.chavePrimaria = chavePrimaria;
    }
    
    /**
     * retorna o nome da coluna
     * @return nome da coluna
     */
    public String getNome(){
        return nome;
    }
    
    /**
     * retorna o tipo do dado
     * @return tipo do dado
     */
    public String getTipoDado() {
        return tipoDado;
    }
    
    /**
     * retorna se é uma chave primaria
     * @return chave primaria
     */
    public boolean isChavePrimaria() {
        return chavePrimaria;
    }

    /**
     * retorna uma representação no formatdo String do objeto final do DefinicaoColuna
     * formato: nome + " " + tipoDado + (chavePrimaria ? " (CHAVE PRIMARIA)" : ""
     * @return representação em forma de texto do objeto 
     */
    @Override
    public String toString() {
        return nome + " " + tipoDado + (chavePrimaria ? " (CHAVE PRIMARIA)" : "");
    }
    
}
