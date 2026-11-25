package PortuSQL.PortuSQL;

import java.util.List;

import Conexao.Resultado;

/**
 * Formata resultados do banco para a interface gráfica.
 *
 * <p>{@link #consulta(Resultado)} mantém o formato original da aba
 * "Resultados" (com {@code |} e {@code +---+}).
  * @version 1.1.0
 */
public final class Formatador {

    /** limite de linhas exibidas, igual ao usado atualmente pela GUI */
    private static final int LIMITE_LINHAS = 1000;

    private Formatador() {
    }

    /** formato clássico da interface gráfica */
    public static String consulta(Resultado resultado) {
        List<String> colunas = resultado.getColunas();
        List<List<Object>> linhas = resultado.getLinhas();
        if (colunas == null || colunas.isEmpty()) {
            return "(consulta sem colunas)";
        }

        String[][] grade = grade(resultado);
        int exibidas = quantidadeExibida(resultado);
        int[] larguras = larguras(grade);

        StringBuilder sb = new StringBuilder();
        adicionarLinhaGui(sb, grade[0], larguras);
        adicionarSeparadorGui(sb, larguras);
        for (int i = 1; i <= exibidas; i++) {
            adicionarLinhaGui(sb, grade[i], larguras);
        }
        if (linhas.size() > exibidas) {
            sb.append("... (").append(linhas.size() - exibidas)
                    .append(" linhas n\u00e3o exibidas)\n");
        }
        return sb.toString();
    }

    /** @return cabeçalho + linhas como texto, limitado a {@link #LIMITE_LINHAS} */
    private static String[][] grade(Resultado resultado) {
        List<String> colunas = resultado.getColunas();
        List<List<Object>> linhas = resultado.getLinhas();
        int exibidas = quantidadeExibida(resultado);

        String[][] grade = new String[exibidas + 1][colunas.size()];
        grade[0] = colunas.toArray(new String[0]);
        for (int i = 0; i < exibidas; i++) {
            List<Object> linha = linhas.get(i);
            for (int c = 0; c < colunas.size(); c++) {
                Object valor = c < linha.size() ? linha.get(c) : null;
                grade[i + 1][c] = valor == null ? "NULL" : String.valueOf(valor);
            }
        }
        return grade;
    }

    private static int quantidadeExibida(Resultado resultado) {
        return Math.min(resultado.getLinhas().size(), LIMITE_LINHAS);
    }

    private static int[] larguras(String[][] grade) {
        int[] larguras = new int[grade[0].length];
        for (String[] linha : grade) {
            for (int c = 0; c < larguras.length; c++) {
                larguras[c] = Math.max(larguras[c], linha[c].length());
            }
        }
        return larguras;
    }

    private static void adicionarLinhaGui(StringBuilder sb, String[] linha, int[] larguras) {
        for (int c = 0; c < linha.length; c++) {
            sb.append("| ").append(completar(linha[c], larguras[c])).append(' ');
        }
        sb.append("|\n");
    }

    private static void adicionarSeparadorGui(StringBuilder sb, int[] larguras) {
        for (int largura : larguras) {
            sb.append('+');
            sb.append("-".repeat(largura + 2));
        }
        sb.append("+\n");
    }

    private static String completar(String texto, int largura) {
        StringBuilder sb = new StringBuilder(texto);
        while (sb.length() < largura) {
            sb.append(' ');
        }
        return sb.toString();
    }
}
