package PortuSQL.PortuSQL;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * versao de execucao do PortuSQL.
 *
 * <p>o numero vem de {@code src/main/resources/version.properties}, gerado
 * pelo Maven a partir de {@code pom.xml}. Assim o verificador de atualizacoes
 * compara sempre a versao do artefato que esta rodando, sem precisar de
 * codigo hardcoded.
 *
 * @author Nicolas Gabriel Venâncio dos Santos
 * @version 1.1.0
 */
public final class BuildInfo {

    /** arquivo filtrado pelo Maven durante o build */
    private static final String ARQUIVO = "/version.properties";
    /** chave lida dentro do arquivo */
    private static final String CHAVE = "versao";
    /** valor usado quando o recurso nao esta disponivel (execucao direta de .class) */
    private static final String DESCONHECIDA = "0.0.0";

    private BuildInfo() {
    }

    /**
     * @return versao do artefato em execucao, ou {@code 0.0.0} se o recurso
     *         de versao nao tiver sido gerado
     */
    public static String versao() {
        try (InputStream entrada = BuildInfo.class.getResourceAsStream(ARQUIVO)) {
            if (entrada != null) {
                Properties props = new Properties();
                props.load(entrada);
                String valor = props.getProperty(CHAVE, "").trim();
                if (!valor.isEmpty() && !valor.startsWith("${")) {
                    return valor;
                }
            }
        } catch (IOException ignorado) {
            // segue para o fallback
        }

        String manifesto = BuildInfo.class.getPackage().getImplementationVersion();
        return manifesto == null || manifesto.isBlank() ? DESCONHECIDA : manifesto.trim();
    }
}
