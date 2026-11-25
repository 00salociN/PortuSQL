package Engine;

import java.util.List;

/**
 * @version 1.1.0
 */
public record InfoComandos(
		String id,
		String nome,
		Categoria categoria,
		List<String> aliases,
		TipoToken tipoToken
		) {

}
