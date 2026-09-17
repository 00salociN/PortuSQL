package Engine;

import java.sql.SQLException;
import java.util.List;
import Comandos.Comando;
import Conexao.Conexao;
import Conexao.Executor;
import Conexao.Resultado;

/**
 * Teste manual do pipeline completo, agora ate a execucao real no H2:
 * texto -> Lexer -> Parser -> Tradutor -> SQL -> ExecutorPortuSQL -> resultado.
 */
public class TesteExecucao {

    public static void main(String[] args) {
        try {
            executar("CRIAR TABELA alunos ( id inteiro CHAVE PRIMARIA, nome texto, idade inteiro )");
            executar("INSERIR (id, nome, idade) EM alunos VALORES (1, 'Joao', 20)");
            executar("INSERIR (id, nome, idade) EM alunos VALORES (2, 'Maria', 22)");
            executar("SELECIONAR * DE alunos");
            executar("SELECIONAR nome DE alunos ONDE idade > 20");
            executar("remover tabela alunos");
        } finally {
            try {
                Conexao.fecharConexao();
            } catch (SQLException e) {
                System.out.println("Erro ao fechar conexao: " + e.getMessage());
            }
        }
    }

    private static void executar(String consultaPortuSQL) {
        System.out.println("========================================");
        System.out.println("PortuSQL : " + consultaPortuSQL);
        try {
            List<Token> tokens = new Lexer(consultaPortuSQL).tokenizar();
            Comando comando = new Parser(tokens).parserComando();
            String sql = new Tradutor().traducao(comando);
            System.out.println("SQL (H2) : " + sql);

            Resultado resultado = new Executor().executar(sql);

            if (resultado.isConsulta()) {
                System.out.println("Colunas  : " + resultado.getColunas());
                for (List<Object> linha : resultado.getLinhas()) {
                    System.out.println("Linha    : " + linha);
                }
            } else {
                System.out.println("Linhas afetadas: " + resultado.getLinhasAfetadas());
            }
        } catch (RuntimeException | SQLException e) {
            System.out.println("ERRO: " + e.getMessage());
        }
        System.out.println();
    }
}