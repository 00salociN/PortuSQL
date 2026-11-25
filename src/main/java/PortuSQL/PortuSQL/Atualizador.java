package PortuSQL.PortuSQL;

import java.awt.Desktop;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.Optional;
import java.util.Properties;
import java.util.function.Consumer;

/**
 * verificador de atualizacoes do PortuSQL.
 *
 * <p>leitura simples de um arquivo {@code .properties} publicado no repositorio.
 * Nao ha dependencia nova: usa apenas {@link HttpClient} do JDK 17. Toda a
 * consulta acontece em segundo plano e falhas de rede sao silenciosas -- abrir
 * o programa nunca pode depender de internet.
 *
 * <p>Esta etapa apenas avisa e abre as notas. A substituicao automatica do
 * arquivo (com conferencia de checksum) ficara para uma versao futura.
 *
 * @author Nicolas Gabriel Venâncio dos Santos
 * @version 1.1.0
 */
public final class Atualizador {

    /** manifesto publicado junto com as release notes */
    private static final String MANIFESTO =
            "https://raw.githubusercontent.com/00salociN/PortuSQL/main/.github/atualizacoes/latest.properties";
    /** pagina usada quando o manifesto nao informa as notas */
    private static final String RELEASES = "https://github.com/00salociN/PortuSQL/releases";
    /** arquivo local que guarda a ultima versao avisada ao usuario */
    private static final Path AVISOS = Paths.get(
            System.getProperty("user.home"), ".portusql", "atualizacoes.properties");

    private Atualizador() {
    }

    /** dados de uma versao disponivel para atualizacao */
    public record InfoVersao(String versao, String notas, String data) {
    }

    /**
     * consulta o manifesto e devolve a versao disponivel somente quando ela
     * for mais nova que a versao em execucao.
     *
     * @return versao disponivel, ou vazio se estiver atualizado/sem rede
     */
    public static Optional<InfoVersao> verificar() {
        try {
            HttpClient cliente = HttpClient.newBuilder()
                    .connectTimeout(Duration.ofSeconds(3))
                    .build();
            HttpRequest pedido = HttpRequest.newBuilder(URI.create(MANIFESTO))
                    .timeout(Duration.ofSeconds(5))
                    .GET()
                    .build();
            HttpResponse<String> resposta = cliente.send(
                    pedido, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
            if (resposta.statusCode() != 200) {
                return Optional.empty();
            }

            Properties props = new Properties();
            try (StringReader leitor = new StringReader(resposta.body())) {
                props.load(leitor);
            }

            String versao = props.getProperty("versao", "").trim();
            String notas = props.getProperty("notas", "").trim();
            String data = props.getProperty("data", "").trim();
            if (!versaoValida(versao) || !ehMaisNova(versao, BuildInfo.versao())) {
                return Optional.empty();
            }
            return Optional.of(new InfoVersao(versao,
                    notas.isEmpty() ? RELEASES : notas, data));
        } catch (Exception ignorado) {
            // offline, manifesto invalido ou timeout: seguir normalmente
            return Optional.empty();
        }
    }

    /**
     * dispara uma consulta em segundo plano. O aviso so e entregue uma vez por
     * versao, mesmo que o programa seja aberto varias vezes.
     *
     * @param aviso callback executado quando ha versao nova (pode ser nulo)
     */
    public static void verificarEmSegundoPlano(Consumer<InfoVersao> aviso) {
        Thread consulta = new Thread(() -> {
            try {
                InfoVersao info = verificar().orElse(null);
                if (info != null && aviso != null && deveAvisar(info)) {
                    aviso.accept(info);
                }
            } catch (Exception ignorado) {
                // nunca derrubar a aplicacao por causa de atualizacao
            }
        }, "portusql-atualizador");
        consulta.setDaemon(true);
        consulta.start();
    }

    /**
     * @return {@code true} se ainda nao foi avisado sobre esta versao
     */
    private static boolean deveAvisar(InfoVersao info) {
        Properties props = new Properties();
        if (Files.isRegularFile(AVISOS)) {
            try (InputStream entrada = Files.newInputStream(AVISOS)) {
                props.load(entrada);
            } catch (IOException ignorado) {
                // arquivo ausente ou ilegivel: avisar na mesma
            }
        }
        if (info.versao().equals(props.getProperty("versaoAvisada"))) {
            return false;
        }

        props.setProperty("versaoAvisada", info.versao());
        try {
            Files.createDirectories(AVISOS.getParent());
            try (OutputStream saida = Files.newOutputStream(AVISOS)) {
                props.store(saida, "Avisos de atualizacao do PortuSQL");
            }
        } catch (IOException ignorado) {
            // sem persistencia o aviso aparece de novo; nao e grave
        }
        return true;
    }

    /** abre as notas da versao no navegador; se falhar, mostra o endereco */
    public static void abrirNotas(InfoVersao info) {
        try {
            if (Desktop.isDesktopSupported()
                    && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                Desktop.getDesktop().browse(URI.create(info.notas()));
                return;
            }
        } catch (Exception ignorado) {
            // segue para o fallback
        }
        System.out.println("Notas: " + info.notas());
    }

    /**
     * comparacao numerica: {@code 1.10.0} e mais nova que {@code 1.9.0}.
     *
     * @return {@code true} se candidata for maior que atual
     */
    public static boolean ehMaisNova(String candidata, String atual) {
        int[] novo = numeros(candidata);
        int[] antigo = numeros(atual);
        if (novo == null || antigo == null) {
            return false;
        }
        int tamanho = Math.max(novo.length, antigo.length);
        for (int i = 0; i < tamanho; i++) {
            int a = i < novo.length ? novo[i] : 0;
            int b = i < antigo.length ? antigo[i] : 0;
            if (a != b) {
                return a > b;
            }
        }
        return false;
    }

    private static boolean versaoValida(String texto) {
        return numeros(texto) != null;
    }

    private static int[] numeros(String texto) {
        if (texto == null || texto.isBlank()) {
            return null;
        }
        String[] partes = texto.trim().split("\\.", -1);
        int[] valores = new int[partes.length];
        for (int i = 0; i < partes.length; i++) {
            if (!partes[i].matches("\\d+")) {
                return null;
            }
            try {
                valores[i] = Integer.parseInt(partes[i]);
            } catch (NumberFormatException erro) {
                return null;
            }
        }
        return valores;
    }
}
