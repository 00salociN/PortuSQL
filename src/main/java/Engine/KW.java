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
 * @version 1.0
 * @see Lexer
 */
public class KW {
    /** dicionario de palavras-chave simples (palavra -> tipo de token) */
    private static final Map<String, TipoToken> palavras_simples = new HashMap<>();
    /** dicionario de tipos de dado (palavra -> tipo de token) */
    private static final Map<String, TipoToken> tipo_dados = new HashMap<>();
    /** mapa de palavras compostas (primeira palavra -> possiveis segundas palavras) */
    private static final Map<String, Set<String>> palavras_compostas = new HashMap<>();

    static {
        palavras_simples.put("criar", TipoToken.palavra_chave);
        palavras_simples.put("tabela", TipoToken.palavra_chave);
        palavras_simples.put("selecionar", TipoToken.palavra_chave);
        palavras_simples.put("de", TipoToken.palavra_chave);
        palavras_simples.put("onde", TipoToken.palavra_chave);
        palavras_simples.put("ordenar", TipoToken.palavra_chave);
        palavras_simples.put("por", TipoToken.palavra_chave);
        palavras_simples.put("agrupar", TipoToken.palavra_chave);
        palavras_simples.put("inserir", TipoToken.palavra_chave);
        palavras_simples.put("valores", TipoToken.palavra_chave);
        palavras_simples.put("atualizar", TipoToken.palavra_chave);
        palavras_simples.put("deletar", TipoToken.palavra_chave);
        palavras_simples.put("apagar", TipoToken.palavra_chave);
        palavras_simples.put("e", TipoToken.palavra_chave);
        palavras_simples.put("ou", TipoToken.palavra_chave);
        palavras_simples.put("nao", TipoToken.palavra_chave);
        palavras_simples.put("nulo", TipoToken.palavra_chave);
        palavras_simples.put("chave", TipoToken.palavra_chave);
        palavras_simples.put("primaria", TipoToken.palavra_chave);
        palavras_simples.put("estrangeira", TipoToken.palavra_chave);
        palavras_simples.put("alterar", TipoToken.palavra_chave);
        palavras_simples.put("adicionar", TipoToken.palavra_chave);
        palavras_simples.put("coluna", TipoToken.palavra_chave);
        palavras_simples.put("renomear", TipoToken.palavra_chave);
        palavras_simples.put("restricao", TipoToken.palavra_chave);
        palavras_simples.put("definir", TipoToken.palavra_chave);
        palavras_simples.put("remover", TipoToken.palavra_chave);
        palavras_simples.put("excluir", TipoToken.palavra_chave);
        palavras_simples.put("mostrar", TipoToken.palavra_chave);
        palavras_simples.put("esvaziar", TipoToken.palavra_chave);
        palavras_simples.put("em", TipoToken.palavra_chave);
        palavras_simples.put("para", TipoToken.palavra_chave);
        palavras_simples.put("referencia", TipoToken.palavra_chave);
        palavras_simples.put("unica", TipoToken.palavra_chave);
        palavras_simples.put("verificar", TipoToken.palavra_chave);
        palavras_simples.put("banco", TipoToken.palavra_chave);
        palavras_simples.put("dados", TipoToken.palavra_chave);
        palavras_simples.put("usar", TipoToken.palavra_chave);

        tipo_dados.put("int", TipoToken.tipo_dado);
        tipo_dados.put("inteiro", TipoToken.tipo_dado);
        tipo_dados.put("texto", TipoToken.tipo_dado);
        tipo_dados.put("data", TipoToken.tipo_dado);
        tipo_dados.put("decimal", TipoToken.tipo_dado);
        tipo_dados.put("gint", TipoToken.tipo_dado);
        tipo_dados.put("cint", TipoToken.tipo_dado);
        tipo_dados.put("mint", TipoToken.tipo_dado);
        tipo_dados.put("hora", TipoToken.tipo_dado);
        tipo_dados.put("data_hora", TipoToken.tipo_dado);

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
}