package Engine;

import java.util.List;

import Comandos.Comando;

public class TesteCompleto {
	  public static void main(String[] args) {

	        // ---- SELECIONAR -----------------------------------------------
	        testar("SELECIONAR * DE alunos");

	        testar("SELECIONAR nome, idade DE alunos");

	        testar("SELECIONAR * DE alunos ONDE idade > 18 E status PARECIDO COM 'ativo'");

	        testar("SELECIONAR * DE alunos ONDE idade > 18 OU status NAO NULO");

	        testar("SELECIONAR cidade DE alunos AGRUPAR POR cidade ORDENAR POR cidade");

	        // ---- CRIAR TABELA ---------------------------------------------
	        testar("CRIAR TABELA alunos ( id inteiro CHAVE PRIMARIA, nome texto, idade inteiro )");

	        testar("CRIAR TABELA pedidos ( id inteiro, total decimal, data texto )");

	        // ---- INSERIR ---------------------------------------------------
	        testar("INSERIR (nome, idade) EM alunos VALORES ('Joao', 20)");

	        testar("INSERIR EM alunos VALORES (1, 'Joao', 20)");

	        // ---- ATUALIZAR -------------------------------------------------
	        testar("atualizar alunos DEFINIR idade = 21 ONDE nome PARECIDO COM 'Joao'");

	        testar("atualizar alunos DEFINIR idade = 10");

	        // ---- DELETAR / ESVAZIAR / EXCLUIR TABELA ----------------------
	        testar("DELETAR DE alunos ONDE idade > 18");

	        testar("ESVAZIAR TABELA alunos");

	        testar("EXCLUIR TABELA alunos");

	        // ---- MOSTRAR ---------------------------------------------------
	        testar("MOSTRAR TABELA alunos");

	        testar("MOSTRAR COLUNA idade DE alunos");

	        // ---- ALTERAR TABELA -------------------------------------------
	        testar("ALTERAR TABELA alunos ADICIONAR COLUNA email texto");

	        testar("ALTERAR TABELA alunos REMOVER COLUNA email");

	        testar("ALTERAR TABELA alunos ALTERAR COLUNA idade decimal");

	        testar("ALTERAR TABELA alunos RENOMEAR COLUNA nome PARA nome_completo");

	        testar("ALTERAR TABELA alunos RENOMEAR PARA estudantes");

	        testar("ALTERAR TABELA alunos ADICIONAR RESTRICAO pk_id id chave primaria");

	        testar("ALTERAR TABELA alunos ADICIONAR RESTRICAO fk_turma turma_id chave estrangeira REFERENCIA turmas ( id )");

	        testar("ALTERAR TABELA alunos ADICIONAR RESTRICAO ck_idade VERIFICAR idade > 0");

	        testar("ALTERAR TABELA alunos REMOVER RESTRICAO fk_turma");

	        // ---- BANCO DE DADOS -------------------------------------------
	        testar("USAR BANCO DE DADOS loja");

	        testar("EXCLUIR BANCO DE DADOS escola");

	        // ---------------------------------------------------------------
	        // Erros esperados (sintaxe e lexico)
	        // ---------------------------------------------------------------
	        testar("Excluir DE alunos ONDE idade > 18");

	        testar("REMOVER TABELA alunos");

	        testar("MOSTRAR alunos");

	        testar("ALTERAR TABELA alunos ADICIONAR RESTRICAO fk_turma chave estrangeira turma_id");

	        testar("ALTERAR TABLA alunos RENOMEAR RESTRICAO fk_turma PARA fk_turma_aluno");

	        testar("SELECIONAR nome DE alunos ONDE idade > $18");
	    }

	    private static void testar(String consultaPortuSQL) {
	        System.out.println("========================================");
	        System.out.println("PortuSQL : " + consultaPortuSQL);
	        try {
	            List<Token> tokens = new Lexer(consultaPortuSQL).tokenizar();
	            Comando comando = new Parser(tokens).parserComando();
	            String sql = new Tradutor().traducao(comando);

	            System.out.println("AST      : " + comando);
	            System.out.println("SQL (H2) : " + sql);
	        } catch (RuntimeException e) {
	            System.out.println("ERRO: " + e.getMessage());
	        }
	        System.out.println();
	    }
	}

