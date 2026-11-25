package Engine;

import java.util.List;

/**
 * texto curto usado no tooltip da IDE.
 *
 * @version 1.1.0
 * @param id identificador unico da entrada
 * @param nome nome exibido em negrito no tooltip
 * @param resumo explicacao curta exibida abaixo do nome
 * @param exemplo exemplo de uso; vazio quando nao ha exemplo
 * @param aliases formas de escrita que identificam esta entrada
 */
public record Documentacao(
		String id,
		String nome,
		String resumo,
		String exemplo,
		List<String> aliases
) {
}
