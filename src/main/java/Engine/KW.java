package Engine;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * catalogo (tabela de simbolos) das palavras reservadas e tipos de dado
 * do PortuSQL.
 *
 * <p>o {@link Lexer} consulta esta classe para decidir se uma palavra lida e
 * palavra-chave, tipo de dado ou apenas um identificador.
 *
 * @author Nicolas Gabriel Venâncio dos Santos
 * @version 1.1.0
 * @see Lexer
 */
public class KW {
    /** dicionario de palavras-chave simples (palavra -> tipo de token) */
    private static final Map<String, TipoToken> palavras_simples = new HashMap<>();
    /** dicionario de tipos de dado (palavra -> tipo de token) */
    private static final Map<String, TipoToken> tipo_dados = new HashMap<>();
    /** mapa de palavras compostas (primeira palavra -> possiveis segundas palavras) */
    private static final Map<String, Set<String>> palavras_compostas = new HashMap<>();
    
    private static final Map<String, TipoToken> funcoes_agregadas = new HashMap<>();

    static {
        // uma unica fonte de verdade: o catalogo usado pelo Lexer e pela IDE
        for (InfoComandos info : CatalogoComandos.todos()) {
            switch (info.categoria()) {
                case PALAVRA_CHAVE ->
                        palavras_simples.put(info.nome(), info.tipoToken());
                case TIPO_DADO ->
                        tipo_dados.put(info.nome(), info.tipoToken());
                case FUNCAO ->
                        funcoes_agregadas.put(info.nome(), info.tipoToken());
                default -> { /* clausulas e comandos nao sao tokens do lexer */ }
            }
        }

//        adicionarComposta("ordenar", "por");
//        adicionarComposta("agrupar", "por");
//        adicionarComposta("chave", "primaria");
//        adicionarComposta("chave", "estrangeira");
    }

    /**
     * registra uma palavra composta (duas palavras que formam um unico token)
     * @param primeira primeira palavra da composta
     * @param segunda segunda palavra da composta
     */
    private static void adicionarComposta(String primeira, String segunda) {
        palavras_compostas.computeIfAbsent(primeira, k -> new HashSet<>()).add(segunda);
    }

    /**
     * retorna o tipo de token de uma palavra-chave simples, se existir
     * @param palavra palavra a consultar
     * @return {@link TipoToken} correspondente ou {@code null} se nao for palavra-chave
     */
    public static TipoToken TipoKWSimples(String palavra) {
        return palavras_simples.get(palavra.toLowerCase());
    }

    /**
     * retorna o tipo de token de um tipo de dado, se existir
     * @param palavra palavra a consultar
     * @return {@link TipoToken#tipo_dado} correspondente ou {@code null} se nao for tipo de dado
     */
    public static TipoToken TipoDado(String palavra) {
        return tipo_dados.get(palavra.toLowerCase());
    }

    /**
     * verifica se a palavra pode iniciar uma palavra composta
     * @param palavra primeira palavra candidata
     * @return {@code true} se a palavra inicia alguma composta
     */
    public static boolean IniciarComposta(String palavra) {
        return palavras_compostas.containsKey(palavra.toLowerCase());
    }

    /**
     * verifica se duas palavras formam uma palavra composta conhecida
     * @param primeira primeira palavra
     * @param segunda segunda palavra
     * @return {@code true} se a sequencia for uma composta cadastrada
     */
    public static boolean Composta(String primeira, String segunda) {
        Set<String> seguintes = palavras_compostas.get(primeira.toLowerCase());
        return seguintes != null && seguintes.contains(segunda.toLowerCase());
    }
    public static TipoToken getFuncaoAgregada(String palavra) {
    	if(palavra == null)
    		return null;
    	return funcoes_agregadas.get(palavra.toLowerCase());
    }
}

