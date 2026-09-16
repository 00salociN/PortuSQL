package Comandos;

public class Mostrar implements Comando{
  private final String entidade; // não sei se entidDE SE ENCAIXA BEM

  public Mostrar(String entidade) {
  this.entidade = entidade;
  }
  public String getEntidade() {
    return entidade;
  }

  @Override
  public String toString() {
  return "Mostrar[entidade=" + entidade + "]";
}
}