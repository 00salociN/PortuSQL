package Engine;

/**
 * enum com todos os tipos de token que o {@link Lexer} pode produzir.
 *
 * <p>cada token da linguagem PortuSQL pertence a exatamente um destes tipos,
 * que orientam o {@link Parser} na montagem do comando.
 *
 * @author Nicolas Gabriel Venâncio dos Santos
 * @version 1.0
 * @see Token
 * @see Lexer
 */
public enum TipoToken {
    /** palavra-chave da linguagem (CRIAR, TABELA, SELECIONAR, ...) */
    palavra_chave,
    /** operador formado por duas palavras (PARECIDO COM) */
    operador_composto,
    /** operador simples (=, +, -, *, /, comparacoes, ...) */
    operador_simples,
    /** identificador (nomes de tabela, coluna, banco, ...) */
    identificador,
    /** numero inteiro ou decimal */
    numerico,
    /** texto entre aspas simples */
    texto,
    /** palavra NULO */
    nulo,
    /** virgula (,) */
    virgula,
    /** parentese de abertura ( */
    parenteses_esquerda,
    /** parentese de fechamento ) */
    parenteses_direita,
    /** ponto e virgula (;) */
    ponto_virgula,
    /** ponto (.) */
    ponto,
    /** fim da entrada (token de encerramento) */
    fim,
    /** tipo de dado (INT, TEXTO, DATA, ...) */
    tipo_dado
}