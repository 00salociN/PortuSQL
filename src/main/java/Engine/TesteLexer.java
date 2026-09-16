package Engine;

import java.util.List;

/**
 * Programa de teste manual do Lexer do PortuSQL.
 *
 * Nao usa nenhum framework de teste (JUnit etc) de proposito -- e so
 * um main() que roda varias consultas de exemplo e imprime a lista de
 * tokens resultante, pra inspecao visual no console do Eclipse.
 *
 * Cobre: palavras-chave simples, tipos de dado, identificadores,
 * numeros, texto, operadores de comparacao (>, <, >=, <=, <>),
 * concatenacao (||), a unica composta que sobrou (PARECIDO COM),
 * CRIAR TABELA, CHAVE PRIMARIA/ESTRANGEIRA, ALTERAR TABELA, e um caso
 * de erro lexico proposital.
 */
public class TesteLexer {

    public static void main(String[] args) {

        testar("SELECIONAR nome, idade DE alunos ONDE idade > 18");

        testar("CRIAR TABELA alunos (\n" +
               "    id INTEIRO CHAVE PRIMARIA,\n" +
               "    nome TEXTO,\n" +
               "    turma_id INTEIRO CHAVE ESTRANGEIRA REFERENCIA turmas(id)\n" +
               ")");

        testar("SELECIONAR nome DE alunos ONDE nome PARECIDO COM 'Jo%'");

        testar("SELECIONAR nome || ' - ' || turma DE alunos");

        testar("SELECIONAR * DE alunos ONDE idade >= 18 E idade <= 25");

        testar("SELECIONAR * DE alunos ONDE status <> 'inativo'");

        testar("ALTERAR TABELA alunos ADICIONAR COLUNA email VARCHAR");

        testar("ALTERAR TABELA alunos RENOMEAR COLUNA nome PARA nome_completo");

        // Caso de erro proposital -- deve lancar erroLexer
        testar("SELECIONAR nome DE alunos ONDE idade > $18");
        
        testar("selecionar tabela coluna");
    }

    private static void testar(String consulta) {
        System.out.println("========================================");
        System.out.println("Consulta: " + consulta);
        System.out.println("----------------------------------------");

        try {
            Lexer lexer = new Lexer(consulta);
            List<Token> tokens = lexer.tokenizar();

            for (Token token : tokens) {
                System.out.printf("  Tipo=%-20s Lexema=%-15s Linha=%d Coluna=%d%n",
                        token.getTipo(), token.getLexema(), token.getLinha(), token.getColuna());
            }

        } catch (ErroLexer e) {
            System.out.println("  ERRO LEXICO: " + e.getMessage());
        }

        System.out.println();
    }
}