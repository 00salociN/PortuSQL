package Telas;

import Conexao.DiffDer;
import Conexao.MapaBanco;
import Conexao.MapaBanco.ColunaDer;
import Conexao.MapaBanco.RelacionamentoDer;
import Conexao.MapaBanco.TabelaDer;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.QuadCurve2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.ToolTipManager;

/**
 * desenho do DER (modelo entidade-relacionamento) do banco conectado.
 *
 * <p>quando recebe um {@link DiffDer}, destaca o que o ultimo comando
 * alterou: tabelas e colunas novas em verde, itens removidos em vermelho
 * riscado e tipos modificados como {@code antigo -> novo}.
  * @version 1.1.0
 */
public class PainelDer extends JPanel {

    private static final int LARGURA_TABELA = 210;
    private static final int ALTURA_TITULO = 30;
    private static final int ALTURA_LINHA = 22;
    private static final int MARGEM = 28;
    private static final int ESPACO_X = 64;
    private static final int ESPACO_Y = 56;
    private static final int COLUNAS = 2;
    private static final Color COR_VERMELHO = new Color(0xE7, 0x4C, 0x3C);

    private enum Estado {
        NORMAL, ADICIONADA, REMOVIDA, MODIFICADA
    }

    private record LinhaDer(
            ColunaDer coluna, Estado estado, String tipoAntigo, String tipoNovo) {
    }

    private MapaBanco mapa = MapaBanco.vazio("—");
    private DiffDer destaque;
    private final Map<String, Rectangle> caixas = new LinkedHashMap<>();
    private final Map<String, Map<String, Integer>> linhasColuna = new LinkedHashMap<>();
    private Dimension tamanhoConteudo = new Dimension(MARGEM * 2, MARGEM * 2);

    private double zoom = 1.0;
    private Point deslocamento = new Point(0, 0);
    private Point inicioArrasto;
    private Point origemArrasto;

    public PainelDer() {
        setBackground(Tema.FUNDO_EDITOR);
        ToolTipManager.sharedInstance().registerComponent(this);
        configurarInteracao();
        recalcular();
    }

    /** troca o mapa e redesenha. */
    public void setMapa(MapaBanco novo) {
        this.mapa = novo == null ? MapaBanco.vazio("—") : novo;
        deslocamento = new Point(0, 0);
        recalcular();
        revalidate();
        repaint();
    }

    /** aplica (ou limpa, com null) os destaques do ultimo comando. */
    public void setDestaque(DiffDer novo) {
        this.destaque = novo;
        recalcular();
        revalidate();
        repaint();
    }

    public MapaBanco getMapa() {
        return mapa;
    }

    private void configurarInteracao() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    inicioArrasto = e.getPoint();
                    origemArrasto = new Point(deslocamento);
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                inicioArrasto = null;
            }
        });

        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (inicioArrasto == null) return;
                int dx = e.getX() - inicioArrasto.x;
                int dy = e.getY() - inicioArrasto.y;
                deslocamento.setLocation(origemArrasto.x + dx, origemArrasto.y + dy);
                repaint();
            }
        });

        addMouseWheelListener((MouseWheelEvent e) -> {
            double fator = e.getWheelRotation() < 0 ? 1.1 : 0.9;
            double novoZoom = Math.max(0.5, Math.min(2.5, zoom * fator));
            if (novoZoom == zoom) return;

            double mx = e.getX();
            double my = e.getY();
            double tx = (mx - deslocamento.x) / zoom;
            double ty = (my - deslocamento.y) / zoom;
            zoom = novoZoom;
            deslocamento.setLocation(
                    (int) Math.round(mx - tx * zoom),
                    (int) Math.round(my - ty * zoom));
            atualizarPreferido();
            revalidate();
            repaint();
        });
    }

    @Override
    public String getToolTipText(MouseEvent event) {
        double x = (event.getX() - deslocamento.x) / zoom;
        double y = (event.getY() - deslocamento.y) / zoom;

        for (TabelaDer tabela : tabelasParaLayout()) {
            Rectangle caixa = caixas.get(tabela.nome());
            if (caixa == null || !caixa.contains(x, y)) continue;

            int indice = (int) ((y - caixa.y - ALTURA_TITULO) / ALTURA_LINHA);
            List<LinhaDer> linhas = linhasExibidas(tabela);
            if (indice >= 0 && indice < linhas.size()) {
                LinhaDer linha = linhas.get(indice);
                ColunaDer coluna = linha.coluna();
                String papel = coluna.chavePrimaria() ? "chave primária"
                        : (eColunaFk(tabela.nome(), coluna.nome()) ? "chave estrangeira" : "coluna");
                String extra = switch (linha.estado()) {
                    case ADICIONADA -> " · adicionada";
                    case REMOVIDA -> " · removida";
                    case MODIFICADA -> " · era " + linha.tipoAntigo();
                    default -> "";
                };
                return "<html><b>" + tabela.nome() + "." + coluna.nome() + "</b><br>"
                        + papel + " · tipo: " + coluna.tipo() + extra + "</html>";
            }
            return "<html><b>" + tabela.nome() + "</b><br>"
                    + tabela.colunas().size() + " colunas</html>";
        }
        return null;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.translate(deslocamento.x, deslocamento.y);
        g2.scale(zoom, zoom);
        desenharConteudo(g2);
        g2.dispose();
    }

    /** salva o diagrama atual em PNG (util para exemplos e documentacao). */
    public void salvarPng(File arquivo) throws IOException {
        BufferedImage imagem = new BufferedImage(
                tamanhoConteudo.width, tamanhoConteudo.height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = imagem.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Tema.FUNDO_EDITOR);
        g2.fillRect(0, 0, imagem.getWidth(), imagem.getHeight());
        desenharConteudo(g2);
        g2.dispose();
        ImageIO.write(imagem, "png", arquivo);
    }

    private void desenharConteudo(Graphics2D g2) {
        if (mapa.tabelas().isEmpty() && (destaque == null || destaque.tabelasRemovidas().isEmpty())) {
            g2.setColor(Tema.TEXTO_SECUNDARIO);
            g2.setFont(new Font("SansSerif", Font.ITALIC, 13));
            g2.drawString("Nenhuma tabela encontrada no banco " + mapa.banco() + ".",
                    MARGEM, MARGEM + 20);
            return;
        }

        for (RelacionamentoDer relacao : mapa.relacionamentos()) {
            desenharRelacao(g2, relacao, false);
        }
        if (destaque != null) {
            for (RelacionamentoDer relacao : destaque.fksRemovidas()) {
                desenharRelacao(g2, relacao, true);
            }
        }
        for (TabelaDer tabela : tabelasParaLayout()) {
            desenharTabela(g2, tabela);
        }
    }

    private void desenharTabela(Graphics2D g2, TabelaDer tabela) {
        Rectangle caixa = caixas.get(tabela.nome());
        if (caixa == null) return;

        boolean nova = destaque != null && destaque.ehTabelaAdicionada(tabela.nome());
        boolean removida = destaque != null && destaque.ehTabelaRemovida(tabela.nome());

        g2.setColor(Tema.FUNDO_PAINEL);
        g2.fillRoundRect(caixa.x, caixa.y, caixa.width, caixa.height, 10, 10);
        g2.setColor(removida ? COR_VERMELHO : nova ? Tema.VERDE : Tema.BORDA);
        g2.setStroke(new BasicStroke(removida || nova ? 2f : 1f));
        g2.drawRoundRect(caixa.x, caixa.y, caixa.width, caixa.height, 10, 10);
        g2.setStroke(new BasicStroke());

        g2.setColor(Tema.SELECAO);
        g2.fillRect(caixa.x + 1, caixa.y + 1, caixa.width - 2, ALTURA_TITULO - 1);
        g2.setColor(removida ? COR_VERMELHO : Tema.TEXTO);
        g2.setFont(new Font("SansSerif", Font.BOLD, 12));
        String titulo = (nova ? "+ " : removida ? "− " : "") + tabela.nome();
        g2.drawString(titulo, caixa.x + 10, caixa.y + 20);
        if (removida) {
            riscar(g2, titulo, caixa.x + 10, caixa.y + 20);
        }

        g2.setFont(new Font("SansSerif", Font.PLAIN, 11));
        FontMetrics fm = g2.getFontMetrics();
        int i = 0;
        for (LinhaDer linha : linhasExibidas(tabela)) {
            int y = caixa.y + ALTURA_TITULO + i * ALTURA_LINHA + 15;
            int x = caixa.x + 10;
            ColunaDer coluna = linha.coluna();
            String texto = coluna.nome() + " : " + coluna.tipo();

            switch (linha.estado()) {
                case ADICIONADA -> {
                    g2.setColor(Tema.VERDE);
                    g2.drawString("+ " + texto, x, y);
                }
                case REMOVIDA -> {
                    g2.setColor(COR_VERMELHO);
                    g2.drawString("− " + texto, x, y);
                    riscar(g2, "− " + texto, x, y);
                }
                case MODIFICADA -> {
                    g2.setColor(Tema.TEXTO);
                    g2.drawString(coluna.nome() + " : ", x, y);
                    int px = x + fm.stringWidth(coluna.nome() + " : ");
                    g2.setColor(COR_VERMELHO);
                    g2.drawString(linha.tipoAntigo(), px, y);
                    riscar(g2, linha.tipoAntigo(), px, y);
                    px += fm.stringWidth(linha.tipoAntigo()) + 4;
                    g2.setColor(Tema.TEXTO_SECUNDARIO);
                    g2.drawString("→", px, y);
                    px += fm.stringWidth("→") + 4;
                    g2.setColor(Tema.VERDE);
                    g2.drawString(linha.tipoNovo(), px, y);
                }
                default -> {
                    g2.setColor(Tema.TEXTO);
                    g2.drawString(texto, x, y);
                }
            }

            if (linha.estado() != Estado.REMOVIDA) {
                if (coluna.chavePrimaria()) {
                    g2.setColor(Tema.VERDE);
                    g2.drawString("PK", caixa.x + caixa.width - 30, y);
                } else if (eColunaFk(tabela.nome(), coluna.nome())) {
                    g2.setColor(Tema.AZUL);
                    g2.drawString("FK", caixa.x + caixa.width - 30, y);
                }
            }
            i++;
        }
    }

    private void desenharRelacao(Graphics2D g2, RelacionamentoDer relacao, boolean removida) {
        Rectangle origem = caixas.get(relacao.tabelaOrigem());
        Rectangle destino = caixas.get(relacao.tabelaDestino());
        if (origem == null || destino == null) return;

        int yo = yColuna(relacao.tabelaOrigem(), relacao.colunaOrigem(), origem);
        int yd = yColuna(relacao.tabelaDestino(), relacao.colunaDestino(), destino);
        int x1 = origem.x <= destino.x ? origem.x + origem.width : origem.x;
        int x2 = origem.x <= destino.x ? destino.x : destino.x + destino.width;

        boolean nova = !removida && destaque != null && destaque.ehFkAdicionada(relacao);
        Color cor = removida ? COR_VERMELHO : nova ? Tema.VERDE : Tema.AZUL;
        g2.setColor(cor);
        g2.setStroke(removida
                ? new BasicStroke(1.5f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER,
                        10f, new float[]{5f, 3f}, 0f)
                : new BasicStroke(nova ? 2f : 1.6f));
        g2.draw(new QuadCurve2D.Double(
                x1, yo, (x1 + x2) / 2.0, (yo + yd) / 2.0 - 18, x2, yd));
        g2.fillOval(x1 - 3, yo - 3, 6, 6);
        g2.fillOval(x2 - 3, yd - 3, 6, 6);

        String nome = relacao.nomeRestricao() == null || relacao.nomeRestricao().isBlank()
                ? "FK" : relacao.nomeRestricao();
        String rotulo = (removida ? "− " : nova ? "+ " : "")
                + nome + " → " + relacao.tabelaDestino() + "(" + relacao.colunaDestino() + ")";
        g2.setFont(new Font("SansSerif", Font.PLAIN, 10));
        int mx = (x1 + x2) / 2;
        int my = (yo + yd) / 2 - 18;
        g2.drawString(rotulo, mx - g2.getFontMetrics().stringWidth(rotulo) / 2, my - 4);
        g2.setStroke(new BasicStroke());
    }

    private void riscar(Graphics2D g2, String texto, int x, int y) {
        FontMetrics fm = g2.getFontMetrics();
        int meio = y - (fm.getAscent() - fm.getDescent()) / 2;
        g2.setStroke(new BasicStroke(1.5f));
        g2.drawLine(x, meio, x + fm.stringWidth(texto), meio);
        g2.setStroke(new BasicStroke());
    }

    private int yColuna(String tabela, String coluna, Rectangle fallback) {
        Map<String, Integer> linhas = linhasColuna.get(tabela);
        if (linhas == null) return fallback.y + fallback.height / 2;
        Integer y = linhas.get(coluna == null ? "" : coluna.toUpperCase());
        return y == null ? fallback.y + fallback.height / 2 : y;
    }

    private boolean eColunaFk(String tabela, String coluna) {
        for (RelacionamentoDer relacao : mapa.relacionamentos()) {
            if (relacao.tabelaOrigem().equalsIgnoreCase(tabela)
                    && relacao.colunaOrigem().equalsIgnoreCase(coluna)) {
                return true;
            }
        }
        return false;
    }

    private List<TabelaDer> tabelasParaLayout() {
        List<TabelaDer> resultado = new ArrayList<>(mapa.tabelas());
        if (destaque != null) {
            Set<String> existentes = new HashSet<>();
            for (TabelaDer tabela : resultado) {
                existentes.add(tabela.nome().toUpperCase());
            }
            for (TabelaDer removida : destaque.tabelasRemovidas()) {
                String chave = removida.nome().toUpperCase();
                if (existentes.add(chave)) {
                    resultado.add(removida);
                }
            }
        }
        return resultado;
    }

    /** linhas exibidas de uma tabela, incluindo colunas removidas (fantasma). */
    private List<LinhaDer> linhasExibidas(TabelaDer tabela) {
        List<LinhaDer> linhas = new ArrayList<>();
        if (destaque == null) {
            for (ColunaDer coluna : tabela.colunas()) {
                linhas.add(new LinhaDer(coluna, Estado.NORMAL, null, null));
            }
            return linhas;
        }

        TabelaDer anterior = tabelaEm(destaque.anterior(), tabela.nome());
        if (anterior == null) {
            for (ColunaDer coluna : tabela.colunas()) {
                linhas.add(new LinhaDer(coluna, Estado.ADICIONADA, null, null));
            }
            return linhas;
        }

        Set<String> vistas = new HashSet<>();
        for (ColunaDer antiga : anterior.colunas()) {
            ColunaDer atual = colunaEm(tabela, antiga.nome());
            vistas.add(antiga.nome().toUpperCase());
            if (atual == null) {
                linhas.add(new LinhaDer(antiga, Estado.REMOVIDA, null, null));
            } else if (!antiga.tipo().equalsIgnoreCase(atual.tipo())) {
                linhas.add(new LinhaDer(atual, Estado.MODIFICADA, antiga.tipo(), atual.tipo()));
            } else {
                linhas.add(new LinhaDer(atual, Estado.NORMAL, null, null));
            }
        }
        for (ColunaDer coluna : tabela.colunas()) {
            if (!vistas.contains(coluna.nome().toUpperCase())) {
                linhas.add(new LinhaDer(coluna, Estado.ADICIONADA, null, null));
            }
        }
        return linhas;
    }

    private static TabelaDer tabelaEm(MapaBanco mapa, String nome) {
        for (TabelaDer tabela : mapa.tabelas()) {
            if (tabela.nome().equalsIgnoreCase(nome)) return tabela;
        }
        return null;
    }

    private static ColunaDer colunaEm(TabelaDer tabela, String nome) {
        for (ColunaDer coluna : tabela.colunas()) {
            if (coluna.nome().equalsIgnoreCase(nome)) return coluna;
        }
        return null;
    }

    private void recalcular() {
        caixas.clear();
        linhasColuna.clear();

        List<TabelaDer> tabelas = tabelasParaLayout();
        if (tabelas.isEmpty()) {
            tamanhoConteudo = new Dimension(520, 160);
            atualizarPreferido();
            return;
        }

        int totalLinhas = (tabelas.size() + COLUNAS - 1) / COLUNAS;
        int[] alturas = new int[totalLinhas];
        for (int i = 0; i < tabelas.size(); i++) {
            int linha = i / COLUNAS;
            int altura = ALTURA_TITULO + linhasExibidas(tabelas.get(i)).size() * ALTURA_LINHA + 8;
            alturas[linha] = Math.max(alturas[linha], altura);
        }

        int y = MARGEM;
        for (int i = 0; i < tabelas.size(); i++) {
            TabelaDer tabela = tabelas.get(i);
            int linha = i / COLUNAS;
            int coluna = i % COLUNAS;
            int x = MARGEM + coluna * (LARGURA_TABELA + ESPACO_X);
            int altura = alturas[linha];

            caixas.put(tabela.nome(), new Rectangle(x, y, LARGURA_TABELA, altura));
            Map<String, Integer> linhas = new LinkedHashMap<>();
            List<LinhaDer> exibidas = linhasExibidas(tabela);
            for (int j = 0; j < exibidas.size(); j++) {
                linhas.put(exibidas.get(j).coluna().nome().toUpperCase(),
                        y + ALTURA_TITULO + j * ALTURA_LINHA + 15);
            }
            linhasColuna.put(tabela.nome(), linhas);

            if (coluna == COLUNAS - 1 || i == tabelas.size() - 1) {
                y += altura + ESPACO_Y;
            }
        }

        int largura = MARGEM * 2 + COLUNAS * LARGURA_TABELA + (COLUNAS - 1) * ESPACO_X;
        tamanhoConteudo = new Dimension(largura, y + MARGEM);
        atualizarPreferido();
    }

    private void atualizarPreferido() {
        setPreferredSize(new Dimension(
                (int) Math.round(tamanhoConteudo.width * zoom),
                (int) Math.round(tamanhoConteudo.height * zoom)));
    }
}
