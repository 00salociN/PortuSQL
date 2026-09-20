package Comandos;

/**
 * define o que é uma coluna de uma tabela, com nome, tipo de dado e se é chave primária
 * 
 * <p>esta classe não é um comando, mas sim uma definição padrão para representar uma coluna
 * 
 * @author Nicolas Gabriel Venâncio dos Santos
 * @version 1.0
 */
public class DefinicaoColuna {
    /*
     * a escolha do private final permite que o objeto seja imutável
     * private: impede o acesso de outros arquivos
     * final: impede a reatribuição do valor
     */
	
	/** nome da coluna */
    private final String nome;
    
    /** tipo do dado da coluna */
    private final String tipoDado;
    
    /** verifica se a coluna é uma chave primária */
    private final boolean chavePrimaria;
    
    /** verifica se a coluna é obrigatória */
    private final boolean naoNulo;

    /**
     * construtor que inicializa os atributos
     * @param nome nome da coluna
     * @param tipoDado tipo do dado da coluna
     * @param chavePrimaria {@code true} se a coluna for chave primária
     * 		{@code false} caso contrário
     * @param naoNulo {@code true} se a coluna for obrigatória
     * 		{@code false} caso contrário
     */
    public DefinicaoColuna(String nome, String tipoDado, boolean chavePrimaria, boolean naoNulo) {
        this.nome = nome;
        this.tipoDado = tipoDado;
        this.chavePrimaria = chavePrimaria;
        this.naoNulo = naoNulo;
    }
    
    /**
     * retorna o nome da coluna
     * @return nome da coluna
     */
    public String getNome(){
        return nome;
    }
    
    /**
     * retorna o tipo do dado da coluna
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
     * retorna se é um campo obrigatório
     * @return não nulo
     */
    public boolean isNaoNulo() {
    	return naoNulo;
    }

    /**
     * retorna uma representação no formato String da classe DefinicaoColuna
     * <p>formato: {@code nome + " " + tipoDado + (chavePrimaria ? " (CHAVE PRIMARIA)" : "" + (naoNulo ? " (NAO NULO)" : ""))}
     * @return representação em forma de texto do objeto 
     */
    @Override
    public String toString() {
        return nome + " " + tipoDado + (chavePrimaria ? " (CHAVE PRIMARIA)" : "" + (naoNulo ? " (NAO NULO)" : ""));
    }
    
}
