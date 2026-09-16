package Engine;

import java.util.List;
import Comandos.Comando;

/**
 * Teste manual do pipeline completo do PortuSQL: texto -> Lexer ->
 * Parser -> Tradutor -> SQL pronto para o H2.
 *
 * Cobre os 16 comandos que o Parser ja sabe montar. Nao inclui
 * ModificarColuna (sem palavra-chave propria) nem ALTERAR TABELA ...
 * DEFINIR (ainda nao implementado).
 */
public class TesteCompleto {

    public static void main(String[] args) {
        testar("SELECIONAR * DE alunos ONDE idade > 18 E status PARECIDO COM 'ativo'");
        testar("CRIAR TABELA alunos ( id inteiro CHAVE PRIMARIA, nome texto, idade inteiro )");
        testar("INSERIR (nome, idade) EM alunos VALORES ('Joao', 20)");
        testar("atualizar alunos DEFINIR idade = 21 ONDE nome PARECIDO COM 'Joao'");
        testar("Excluir DE alunos ONDE idade > 18");
        testar("DELETAR DE alunos ONDE idade > 18");
        testar("REMOVER TABELA alunos");
        testar("ESVAZIAR TABELA alunos");
        testar("MOSTRAR alunos");
        testar("ALTERAR TABELA alunos ADICIONAR COLUNA email texto");
        testar("ALTERAR TABELA alunos REMOVER COLUNA email");
        testar("ALTERAR TABELA alunos ALTERAR COLUNA idade decimal");
        testar("ALTERAR TABELA alunos RENOMEAR COLUNA nome PARA nome_completo");
        testar("ALTERAR TABELA alunos RENOMEAR PARA estudantes");
        testar("ALTERAR TABELA alunos ADICIONAR RESTRICAO fk_turma chave estrangeira turma_id");
        testar("ALTERAR TABELA alunos REMOVER RESTRICAO fk_turma");
        testar("ALTERAR TABLA alunos RENOMEAR RESTRICAO fk_turma PARA fk_turma_aluno");

        // Caso de erro proposital
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