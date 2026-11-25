package Telas;

import Engine.DicasCatalogos;
import Engine.Documentacao;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.swing.JTextPane;
import javax.swing.ToolTipManager;

/**
 * editor de SQL que mostra, no tooltip, a documentacao do comando sob o mouse.
 *
 * <p>para resolver comandos completos, o editor coleta uma janela a volta da
 * palavra sob o mouse (palavras anteriores e posteriores) e entrega o foco em
 * separado para {@link DicasCatalogos#buscarComFoco(String, String...)}.
 *
 * @author Nicolas Gabriel Venâncio dos Santos
 * @version 1.1.0
 */
public class EditorComDica extends JTextPane {

    /** quantas palavras antes da palavra sob o mouse entram na janela */
    private static final int MAX_ANTERIORES = 4;
    /** quantas palavras depois da palavra sob o mouse entram na janela */
    private static final int MAX_PALAVRAS = 8;

    public EditorComDica() {
        // necessario para o Swing chamar getToolTipText(MouseEvent)
        ToolTipManager.sharedInstance().registerComponent(this);
    }

    @Override
    public String getToolTipText(MouseEvent event) {
        String texto = getText();
        int offset;
        try {
            offset = viewToModel2D(event.getPoint());
        } catch (Exception ignorado) {
            return null;
        }
        if (offset < 0 || offset > texto.length()) return null;

        int[] limites = limitesPalavra(texto, offset);
        if (limites == null) return null;
        int inicio = limites[0];
        int fim = limites[1];
        String foco = texto.substring(inicio, fim).toLowerCase();

        List<String> contexto = palavrasNoEntorno(texto, inicio, fim, foco);
        if (contexto.isEmpty()) return null;

        Optional<Documentacao> dica =
                DicasCatalogos.buscarComFoco(foco, contexto.toArray(String[]::new));
        if (dica.isEmpty()) return null;

        Documentacao d = dica.get();
        String html = "<html><b>" + escapar(d.nome()) + "</b><br>" + escapar(d.resumo());
        if (d.exemplo() != null && !d.exemplo().isBlank()) {
            html += "<br><br><i>Ex.: " + escapar(d.exemplo()) + "</i>";
        }
        return html + "</html>";
    }

    /** devolve [inicio, fim] da palavra sob o offset, ou null se nao houver. */
    private static int[] limitesPalavra(String texto, int offset) {
        int inicio = offset;
        while (inicio > 0 && ehPalavra(texto.charAt(inicio - 1))) inicio--;

        int fim = offset;
        while (fim < texto.length() && ehPalavra(texto.charAt(fim))) fim++;

        return inicio == fim ? null : new int[]{inicio, fim};
    }

    /** monta a janela: ate 4 antes + foco + ate 8 depois. */
    private static List<String> palavrasNoEntorno(String texto, int inicio, int fim, String foco) {
        List<String> saida = new ArrayList<>(palavrasAntes(texto, inicio));
        saida.add(foco);
        saida.addAll(palavrasDepois(texto, fim));
        return saida;
    }

    private static List<String> palavrasAntes(String texto, int inicio) {
        List<String> saida = new ArrayList<>();
        int p = inicio;
        while (saida.size() < MAX_ANTERIORES && p > 0) {
            while (p > 0 && Character.isWhitespace(texto.charAt(p - 1))) p--;
            if (p == 0) break;

            char c = texto.charAt(p - 1);
            if (c == ';' || c == '\n' || c == '\r' || c == '(' || c == ')' || !ehPalavra(c)) break;

            int q = p;
            while (q > 0 && ehPalavra(texto.charAt(q - 1))) q--;
            saida.add(0, texto.substring(q, p).toLowerCase());
            p = q;
        }
        return saida;
    }

    private static List<String> palavrasDepois(String texto, int fim) {
        List<String> saida = new ArrayList<>();
        int p = fim;
        while (p < texto.length() && saida.size() < MAX_PALAVRAS) {
            while (p < texto.length() && Character.isWhitespace(texto.charAt(p))) p++;
            if (p >= texto.length()) break;

            char c = texto.charAt(p);
            if (c == ';' || c == '\n' || c == '\r' || c == '(' || c == ')' || c == ',') break;
            if (!ehPalavra(c)) {
                p++;
                continue;
            }

            int q = p;
            while (q < texto.length() && ehPalavra(texto.charAt(q))) q++;
            saida.add(texto.substring(p, q).toLowerCase());
            p = q;
        }
        return saida;
    }

    private static boolean ehPalavra(char c) {
        return Character.isLetterOrDigit(c) || c == '_';
    }

    private static String escapar(String s) {
        if (s == null) return "";
        return s.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\n", "<br>");
    }
}
