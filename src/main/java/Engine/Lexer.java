package Engine;

import java.util.ArrayList;
import java.util.List;

public class Lexer {
    private final String codigo;
    private int posicao = 0;
    private int linha = 1;
    private int coluna = 1;

    public Lexer(String codigo) {
        this.codigo = codigo == null ? "" : codigo;
    }

    public List<Token> tokenizar() {
        List<Token> tokenSimples = primeiraVarredura();
        return segundaVarredura(tokenSimples);
    }

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

    private Token ProcessarConcatenar(int inicioLinha, int inicioColuna) {
        avancar();

        if (!fimEntrada() && caracterAtual() == '|') {
            avancar();
            return new Token(TipoToken.operador_simples, "||", inicioLinha, inicioColuna);
        }

        throw new ErroLexer("'|' isolado não é válido. Use '||' para concatenar textos", inicioLinha, inicioColuna);
    }

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

    private boolean fimEntrada() {
        return posicao >= codigo.length();
    }

    private char caracterAtual() {
        return codigo.charAt(posicao);
    }

    private void avancar() {
        posicao++;
        coluna++;
    }
}
