package Engine;

import java.util.ArrayList;
import java.util.List;

/**
 * analisador lexico (tokenizador) do PortuSQL.
 *
 * <p>percorre o texto do comando PortuSQL e o transforma em uma lista de
 * {@link Token}, identificando palavras-chave, tipos de dado, identificadores,
 * numeros, textos, operadores e pontuacao.
 *
 * <p>o processo acontece em duas varreduras:
 * <ol>
 *     <li>{@link #primeiraVarredura()} separa os tokens simples;</li>
 *     <li>{@link #segundaVarredura(List)} junta palavras-chave compostas
 *     (ex.: PARECIDO COM) em um unico token.</li>
 * </ol>
 *
 * <p>caso encontre um caractere invalido ou um token mal formado, lanca
 * {@link ErroLexer}.
 *
 * @author Nicolas Gabriel Venâncio dos Santos
 * @version 1.0
 * @see Token
 * @see TipoToken
 * @see KW
 */
public class Lexer {
    /** codigo fonte (texto do comando PortuSQL) que sera tokenizado */
    private final String codigo;
    /** posicao atual de leitura no codigo */
    private int posicao = 0;
    /** linha atual de leitura (inicia em 1) */
    private int linha = 1;
    /** coluna atual de leitura (inicia em 1) */
    private int coluna = 1;

    /**
     * construtor que inicializa o codigo a ser tokenizado
     * @param codigo texto do comando PortuSQL (se {@code null}, vira string vazia)
     */
    public Lexer(String codigo) {
        this.codigo = codigo == null ? "" : codigo;
    }

    /**
     * tokeniza o codigo, executando as duas varreduras
     * @return lista de {@link Token} do codigo, terminada com o token
     *         {@link TipoToken#fim}
     */
    public List<Token> tokenizar() {
        List<Token> tokenSimples = primeiraVarredura();
        return segundaVarredura(tokenSimples);
    }

    /**
     * primeira varredura: le caractere a caractere e produz os tokens simples
     * @return lista de tokens simples (sem uniao de palavras compostas)
     */
    private List<Token> primeiraVarredura() {
        List<Token> tokens = new ArrayList<>();

        while (!fimEntrada()) {
            char atual = caracterAtual();

            if (atual == ' ' || atual == '\t' || atual == '\r') {
                avancar();
                continue;
            }

            if (atual == '\n') {
                avancar();
                linha++;
                coluna = 1;
                continue;
            }

            int inicioLinha = linha;
            int inicioColuna = coluna;

            if (Character.isDigit(atual)) {
                tokens.add(ProcessarNumero(inicioLinha, inicioColuna));
                continue;
            }

            if (atual == '\'') {
                tokens.add(ProcessarTexto(inicioLinha, inicioColuna));
                continue;
            }

            if (Character.isLetter(atual) || atual == '_') {
                tokens.add(ProcessarPalavra(inicioLinha, inicioColuna));
                continue;
            }

            switch (atual) {
                case ',':
                    tokens.add(new Token(TipoToken.virgula, ",", inicioLinha, inicioColuna));
                    avancar();
                    break;
                case '(':
                    tokens.add(new Token(TipoToken.parenteses_esquerda, "(", inicioLinha, inicioColuna));
                    avancar();
                    break;
                case ')':
                    tokens.add(new Token(TipoToken.parenteses_direita, ")", inicioLinha, inicioColuna));
                    avancar();
                    break;
                case ';':
                    tokens.add(new Token(TipoToken.ponto_virgula, ";", inicioLinha, inicioColuna));
                    avancar();
                    break;
                case '.':
                    tokens.add(new Token(TipoToken.ponto, ".", inicioLinha, inicioColuna));
                    avancar();
                    break;
                case '=':
                case '+':
                case '-':
                case '*':
                case '/':
                    tokens.add(new Token(TipoToken.operador_simples, String.valueOf(atual), inicioLinha, inicioColuna));
                    avancar();
                    break;
                case '>':
                case '<':
                    tokens.add(ProcessarComparacao(inicioLinha, inicioColuna));
                    break;
                case '|':
                    tokens.add(ProcessarConcatenar(inicioLinha, inicioColuna));
                    break;
                default:
                    throw new ErroLexer("Caractere inesperado: '" + atual + "'", inicioLinha, inicioColuna);
            }
        }

        tokens.add(new Token(TipoToken.fim, "", linha, coluna));
        return tokens;
    }

    /**
     * le um numero inteiro ou decimal e devolve o token correspondente
     * @param inicioLinha linha onde o numero comeca
     * @param inicioColuna coluna onde o numero comeca
     * @return {@link Token} numerico
     * @throws ErroLexer se houver mais de um ponto decimal ou ponto no final
     */
    private Token ProcessarNumero(int inicioLinha, int inicioColuna) {
        StringBuilder sg = new StringBuilder();
        boolean visualizarPonto = false;

        while (!fimEntrada() && (Character.isDigit(caracterAtual()) || caracterAtual() == '.')) {
            if (caracterAtual() == '.') {
                if (visualizarPonto) {
                    throw new ErroLexer("Número inválido: mais de um ponto decimal", linha, coluna);
                }
                visualizarPonto = true;
            }
            sg.append(caracterAtual());
            avancar();
        }

        if (sg.length() > 0 && sg.charAt(sg.length() - 1) == '.') {
            throw new ErroLexer("Número inválido: ponto decimal sem dígitos depois dele", inicioLinha, inicioColuna);
        }

        return new Token(TipoToken.numerico, sg.toString(), inicioLinha, inicioColuna);
    }

    /**
     * le um texto delimitado por aspas simples e devolve o token correspondente
     * @param inicioLinha linha onde o texto comeca
     * @param inicioColuna coluna onde o texto comeca
     * @return {@link Token} texto
     * @throws ErroLexer se o texto nao for fechado com aspas simples
     */
    private Token ProcessarTexto(int inicioLinha, int inicioColuna) {
        StringBuilder sg = new StringBuilder();
        avancar();

        while (!fimEntrada() && caracterAtual() != '\'') {
            if (caracterAtual() == '\n') {
                sg.append(caracterAtual());
                avancar();
                linha++;
                coluna = 1;
            } else {
                sg.append(caracterAtual());
                avancar();
            }
        }

        if (fimEntrada()) {
            throw new ErroLexer("Texto não foi fechado com aspas", inicioLinha, inicioColuna);
        }

        avancar();
        return new Token(TipoToken.texto, sg.toString(), inicioLinha, inicioColuna);
    }

    /**
     * le uma palavra e classifica como palavra-chave, tipo de dado, NULO
     * ou identificador
     * @param inicioLinha linha onde a palavra comeca
     * @param inicioColuna coluna onde a palavra comeca
     * @return {@link Token} correspondente a classificacao da palavra
     */
    private Token ProcessarPalavra(int inicioLinha, int inicioColuna) {
        StringBuilder sg = new StringBuilder();

        while (!fimEntrada() && (Character.isLetterOrDigit(caracterAtual()) || caracterAtual() == '_')) {
            sg.append(caracterAtual());
            avancar();
        }

        String palavra = sg.toString();
        String palavraNormalizada = palavra.toLowerCase();

        if (palavraNormalizada.equals("nulo")) {
            return new Token(TipoToken.nulo, palavra, inicioLinha, inicioColuna);
        }

        TipoToken tipoKW = KW.TipoKWSimples(palavraNormalizada);
        if (tipoKW != null) {
            return new Token(tipoKW, palavra, inicioLinha, inicioColuna);
        }

        TipoToken tipoDado = KW.TipoDado(palavraNormalizada);
        if (tipoDado != null) {
            return new Token(tipoDado, palavra, inicioLinha, inicioColuna);
        }

        return new Token(TipoToken.identificador, palavra, inicioLinha, inicioColuna);
    }

    /**
     * le um operador de comparacao (que pode ser composto por dois caracteres)
     * @param inicioLinha linha onde o operador comeca
     * @param inicioColuna coluna onde o operador comeca
     * @return {@link Token} operador simples (=, &gt;=, &lt;=, &lt;&gt;, ...)
     */
    private Token ProcessarComparacao(int inicioLinha, int inicioColuna) {
        char primeiro = caracterAtual();
        avancar();

        if (!fimEntrada() && caracterAtual() == '=') {
            avancar();
            return new Token(TipoToken.operador_simples, primeiro + "=", inicioLinha, inicioColuna);
        }

        if (primeiro == '<' && !fimEntrada() && caracterAtual() == '>') {
            avancar();
            return new Token(TipoToken.operador_simples, "<>", inicioLinha, inicioColuna);
        }

        return new Token(TipoToken.operador_simples, String.valueOf(primeiro), inicioLinha, inicioColuna);
    }

    /**
     * le o operador de concatenacao (||)
     * @param inicioLinha linha onde o operador comeca
     * @param inicioColuna coluna onde o operador comeca
     * @return {@link Token} operador simples "||"
     * @throws ErroLexer se a barra vertical nao for seguida de outra
     */
    private Token ProcessarConcatenar(int inicioLinha, int inicioColuna) {
        avancar();

        if (!fimEntrada() && caracterAtual() == '|') {
            avancar();
            return new Token(TipoToken.operador_simples, "||", inicioLinha, inicioColuna);
        }

        throw new ErroLexer("'|' isolado não é válido. Use '||' para concatenar textos", inicioLinha, inicioColuna);
    }

    /**
     * segunda varredura: une palavras-chave compostas em um unico token
     * @param tokenSimples lista de tokens da primeira varredura
     * @return lista final de tokens, com as compostas unidas
     */
    private List<Token> segundaVarredura(List<Token> tokenSimples) {
        List<Token> resultado = new ArrayList<>();
        int i = 0;

        while (i < tokenSimples.size()) {
            Token atual = tokenSimples.get(i);
            Token proximo = (i + 1 < tokenSimples.size()) ? tokenSimples.get(i + 1) : null;

            boolean possivelComposta = atual.getTipo() == TipoToken.palavra_chave
                    && KW.IniciarComposta(atual.getLexema());

            if (possivelComposta
                    && proximo != null
                    && proximo.getTipo() == TipoToken.palavra_chave
                    && KW.Composta(atual.getLexema(), proximo.getLexema())) {
                String lexemaComposto = atual.getLexema() + " " + proximo.getLexema();
                resultado.add(new Token(
                        TipoToken.operador_composto,
                        lexemaComposto,
                        atual.getLinha(),
                        atual.getColuna()));
                i += 2;
                continue;
            }

            resultado.add(atual);
            i++;
        }

        return resultado;
    }

    /**
     * verifica se a leitura do codigo chegou ao fim
     * @return {@code true} se nao ha mais caracteres para ler
     */
    private boolean fimEntrada() {
        return posicao >= codigo.length();
    }

    /**
     * retorna o caractere na posicao atual
     * @return caractere atual do codigo
     */
    private char caracterAtual() {
        return codigo.charAt(posicao);
    }

    /**
     * avanca uma posicao no codigo e incrementa a coluna
     */
    private void avancar() {
        posicao++;
        coluna++;
    }
}