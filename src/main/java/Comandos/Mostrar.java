package Comandos;

/**
 * @author Nicolas Gabriel Venâncio dos Santos
 * @version 1.0
 * 
 * sintaxe¹: MOSTRAR <nome_tabela> || <nome_database>
 * sintaxe²: MOSTRAR <nome_coluna> de <nome_tabela> // ainda não feita
 * este comando serve para mostrar todas as tabelas ou todas as databases (sintaxe¹)
 * 		mostrar as colunas de uma determinada tabela (sintaxe²)
 * 
 * implementa a interface {@link Comando}
 */
public class Mostrar implements Comando{
	/* 
	 * a escolha do private final permite que o objeto seja imutável 
	 * private: impede o acesso de outros arquivos
	 * final: impede a reatribuição do valor
	 */
  private final String entidade; // não sei se entidade se encaixa bem 

  /**
   * construtor que inicializa os atributos
   * @param entidade nome da entidade
   */
  public Mostrar(String entidade) {
  this.entidade = entidade;
  }
  
  /**
   * retorna o nome da entidade
   * @return nome da entidade
   */
  public String getEntidade() {
    return entidade;
  }

  /**
   * retorna uma representação no formatdo String do objeto final do Mostrar
   * formato: "Mostrar[entidade=" + entidade + "]"
   * 
   * @return representação em forma de texto do objeto
   */
  @Override
  public String toString() {
  return "Mostrar[entidade=" + entidade + "]";
}
}