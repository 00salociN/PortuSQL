package Engine;

import java.sql.SQLException;
import java.util.List;

import Comandos.Comando;
import Conexao.Conexao;
import Conexao.Executor;
import Conexao.Resultado;

public class TesteExecucao {

    private static final String BANCO_TESTE = "teste_portusql";
	  public static void main(String[] args) {
	        try {
	            // comeca sempre do zero: fecha qualquer conexao sobrando e apaga
	            // o banco de teste antes de recria-lo
	            try {
	                Conexao.fecharConexao();
	            } catch (SQLException ignorado) {
	                // nao havia conexao ativa, nada a fazer
	            }
	            Conexao.excluirBanco(BANCO_TESTE);
	            Conexao.obterConexao(BANCO_TESTE);
	        } catch (SQLException e) {
	            System.out.println("ERRO ao preparar o banco de teste: " + e.getMessage());
	            return;
	        }

	        try {
	            // ---- CRIAR TABELA ------------------------------------------
	            testar("CRIAR TABELA alunos ( id inteiro CHAVE PRIMARIA, nome texto, idade inteiro )");

	            // INSERIR com valor de texto: falha conhecida do tradutor
	            // (nao coloca aspas no valor, entao o H2 rejeita)
	            testar("INSERIR (id, nome, idade) EM alunos VALORES (1, 'Joao', 20)");

	            // INSERIR com apenas valores numericos: funciona normalmente
	            testar("INSERIR (id, idade) EM alunos VALORES (1, 20)");
	            testar("INSERIR (id, idade) EM alunos VALORES (2, 22)");
	            testar("INSERIR (id, idade) EM alunos VALORES (3, 18)");

	            // ---- SELECIONAR ---------------------------------------------
	            testar("SELECIONAR * DE alunos");
	            testar("SELECIONAR id, idade DE alunos ONDE idade > 18");

	            // ---- ATUALIZAR ----------------------------------------------
	            testar("atualizar alunos DEFINIR idade = 30 ONDE id = 1");
	            testar("SELECIONAR id, idade DE alunos ONDE id = 1");

	            // ---- DELETAR -------------------------------------------------
	            testar("DELETAR DE alunos ONDE idade > 18");
	            testar("SELECIONAR id, idade DE alunos");

	            // ---- ESVAZIAR ------------------------------------------------
	            testar("ESVAZIAR TABELA alunos");
	            testar("SELECIONAR * DE alunos");

	            // ---- MOSTRAR -------------------------------------------------
	            testar("MOSTRAR TABELA alunos");
	            testar("MOSTRAR COLUNA idade DE alunos");

	            // ---- EXCLUIR TABELA -----------------------------------------
	            testar("EXCLUIR TABELA alunos");

	            // ---- erro proposital (comando nao reconhecido) --------------
	            testar("remover tabela alunos");
	        } finally {
	            try {
	                Conexao.fecharConexao();
	            } catch (SQLException e) {
	                System.out.println("Erro ao fechar conexao: " + e.getMessage());
	            }
	            try {
	                // nao deixa o banco de teste para tras
	                Conexao.excluirBanco(BANCO_TESTE);
	            } catch (SQLException e) {
	                System.out.println("Erro ao apagar o banco de teste: " + e.getMessage());
	            }
	        }
	    }

	    private static void testar(String consultaPortuSQL) {
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
