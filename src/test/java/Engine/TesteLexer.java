package Engine;

import java.util.List;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Testes automatizados (JUnit 5) do analisador lexico ({@link Lexer}).
 *
 * <p>antes de virar classe de teste, este arquivo era um {@code main()} manual
 * que imprimia os tokens no console; agora cada consulta virou um
 * {@code @Test} que confere a sequencia de tokens (tipo e lexema) e, em
 * alguns casos, a posicao (linha/coluna) de tokens em consultas multilinha.
 *
 * <p>cobre: palavras-chave, tipos de dado, identificadores, numeros, textos,
 * operadores de comparacao (&gt;=, &lt;=, &lt;&gt;), concatenacao (||), a
 * palavra NULO, palavras nao reservadas (PARECIDO e COM) e erros lexicos.
 *
 * <p>alem dos {@code @Test}, a classe mantem o {@code main()} original do
 * PortuSQL: rode-a como uma "Java Application" (no Eclipse, botao direito na
 * classe -> Run As -> Java Application) para ver os tokens impressos no
 * console, exatamente como antes.
 *
 * @author Nicolas Gabriel Venâncio dos Santos
 * @version 1.0
 * @see Lexer
 */
public class TesteLexer {

    /**
     * tokeniza um texto PortuSQL
     * @param texto texto do comando
     * @return lista de tokens produzida pelo {@link Lexer}
     */
    private static List<Token> tokenizar(String texto) {
        return new Lexer(texto).tokenizar();
    }

    /**
     * confere a sequencia de tipos e lexemas de uma lista de tokens
     * @param tokens tokens produzidos
     * @param esperados matriz {@code {tipo, lexema}} na ordem esperada
     */
    private static void assertTokens(List<Token> tokens, String[][] esperados) {
        assertEquals(esperados.length, tokens.size(), "quantidade de tokens");
        for (int i = 0; i < esperados.length; i++) {
            assertEquals(TipoToken.valueOf(esperados[i][0]), tokens.get(i).getTipo(),
                    "tipo do token " + i);
            assertEquals(esperados[i][1], tokens.get(i).getLexema(),
                    "lexema do token " + i);
        }
    }

    /**
     * SELECIONAR com identificadores, virgula, numero e comparacao
     */
    @Test
    void selecionarCompletoProduzSequenciaDeTokens() {
        List<Token> tokens = tokenizar("SELECIONAR nome, idade DE alunos ONDE idade > 18");
        assertTokens(tokens, new String[][]{
            {"palavra_chave", "SELECIONAR"},
            {"identificador", "nome"},
            {"virgula", ","},
            {"identificador", "idade"},
            {"palavra_chave", "DE"},
            {"identificador", "alunos"},
            {"palavra_chave", "ONDE"},
            {"identificador", "idade"},
            {"operador_simples", ">"},
            {"numerico", "18"},
            {"fim", ""}
        });
    }

    /**
     * CRIAR TABELA multilinha: tipos de dado, CHAVE PRIMARIA e
     * rastreamento de linha (tokens das linhas 2, 3 e 4)
     */
    @Test
    void criarTabelaMultilinhaClassificaTiposDeDado() {
        List<Token> tokens = tokenizar("CRIAR TABELA alunos (\n"
                + "    id INTEIRO CHAVE PRIMARIA,\n"
                + "    nome TEXTO,\n"
                + "    turma_id INTEIRO CHAVE ESTRANGEIRA REFERENCIA turmas(id)\n"
                + ")");
        assertTokens(tokens, new String[][]{
            {"palavra_chave", "CRIAR"},
            {"palavra_chave", "TABELA"},
            {"identificador", "alunos"},
            {"parenteses_esquerda", "("},
            {"identificador", "id"},
            {"tipo_dado", "INTEIRO"},
            {"palavra_chave", "CHAVE"},
            {"palavra_chave", "PRIMARIA"},
            {"virgula", ","},
            {"identificador", "nome"},
            {"tipo_dado", "TEXTO"},
            {"virgula", ","},
            {"identificador", "turma_id"},
            {"tipo_dado", "INTEIRO"},
            {"palavra_chave", "CHAVE"},
            {"palavra_chave", "ESTRANGEIRA"},
            {"palavra_chave", "REFERENCIA"},
            {"identificador", "turmas"},
            {"parenteses_esquerda", "("},
            {"identificador", "id"},
            {"parenteses_direita", ")"},
            {"parenteses_direita", ")"},
            {"fim", ""}
        });

        // rastreamento de linha e coluna
        assertEquals(1, tokens.get(0).getLinha());
        assertEquals(2, tokens.get(4).getLinha());   // id dentro da lista de colunas
        assertEquals(3, tokens.get(9).getLinha());   // nome
        assertEquals(4, tokens.get(12).getLinha());  // turma_id
        assertEquals(5, tokens.get(21).getLinha());  // parentese de fechamento
    }

    /**
     * texto entre aspas simples perde as aspas, mas mantem o conteudo
     */
    @Test
    void textoEntreAspasPerdeAspas() {
        List<Token> tokens = tokenizar("'Joao da Silva'");
        assertTokens(tokens, new String[][]{
            {"texto", "Joao da Silva"},
            {"fim", ""}
        });
    }

    /**
     * PARECIDO e COM nao sao palavras reservadas: viram identificadores
     */
    @Test
    void palavrasParecidoEComSaoIdentificadores() {
        List<Token> tokens = tokenizar("SELECIONAR nome DE alunos ONDE nome PARECIDO COM 'Jo%'");
        assertTokens(tokens, new String[][]{
            {"palavra_chave", "SELECIONAR"},
            {"identificador", "nome"},
            {"palavra_chave", "DE"},
            {"identificador", "alunos"},
            {"palavra_chave", "ONDE"},
            {"identificador", "nome"},
            {"identificador", "PARECIDO"},
            {"identificador", "COM"},
            {"texto", "Jo%"},
            {"fim", ""}
        });
    }

    /**
     * NULO tem tipo proprio e NAO e palavra-chave reservada
     */
    @Test
    void nuloETipoDeTokenProprio() {
        List<Token> tokens = tokenizar("SELECIONAR * DE alunos ONDE status NAO NULO");
        assertTokens(tokens, new String[][]{
            {"palavra_chave", "SELECIONAR"},
            {"operador_simples", "*"},
            {"palavra_chave", "DE"},
            {"identificador", "alunos"},
            {"palavra_chave", "ONDE"},
            {"identificador", "status"},
            {"palavra_chave", "NAO"},
            {"nulo", "NULO"},
            {"fim", ""}
        });
    }

    /**
     * concatenacao (||) vira operador simples
     */
    @Test
    void concatenacaoComDuasBarras() {
        List<Token> tokens = tokenizar("SELECIONAR nome || ' - ' || turma DE alunos");
        assertTokens(tokens, new String[][]{
            {"palavra_chave", "SELECIONAR"},
            {"identificador", "nome"},
            {"operador_simples", "||"},
            {"texto", " - "},
            {"operador_simples", "||"},
            {"identificador", "turma"},
            {"palavra_chave", "DE"},
            {"identificador", "alunos"},
            {"fim", ""}
        });
    }

    /**
     * operadores de comparacao formados por dois caracteres
     */
    @Test
    void operadoresDeComparacao() {
        List<Token> tokens = tokenizar("SELECIONAR * DE alunos ONDE idade >= 18 E idade <= 25");
        assertTokens(tokens, new String[][]{
            {"palavra_chave", "SELECIONAR"},
            {"operador_simples", "*"},
            {"palavra_chave", "DE"},
            {"identificador", "alunos"},
            {"palavra_chave", "ONDE"},
            {"identificador", "idade"},
            {"operador_simples", ">="},
            {"numerico", "18"},
            {"palavra_chave", "E"},
            {"identificador", "idade"},
            {"operador_simples", "<="},
            {"numerico", "25"},
            {"fim", ""}
        });
    }

    /**
     * operador de diferenca &lt;&gt; e texto
     */
    @Test
    void diferencaEntreDoisCaracteres() {
        List<Token> tokens = tokenizar("SELECIONAR * DE alunos ONDE status <> 'inativo'");
        assertTokens(tokens, new String[][]{
            {"palavra_chave", "SELECIONAR"},
            {"operador_simples", "*"},
            {"palavra_chave", "DE"},
            {"identificador", "alunos"},
            {"palavra_chave", "ONDE"},
            {"identificador", "status"},
            {"operador_simples", "<>"},
            {"texto", "inativo"},
            {"fim", ""}
        });
    }

    /**
     * VARCHAR nao e tipo de dado conhecido: cai como identificador
     */
    @Test
    void varcharEIdentificador() {
        List<Token> tokens = tokenizar("ALTERAR TABELA alunos ADICIONAR COLUNA email VARCHAR");
        assertTokens(tokens, new String[][]{
            {"palavra_chave", "ALTERAR"},
            {"palavra_chave", "TABELA"},
            {"identificador", "alunos"},
            {"palavra_chave", "ADICIONAR"},
            {"palavra_chave", "COLUNA"},
            {"identificador", "email"},
            {"identificador", "VARCHAR"},
            {"fim", ""}
        });
    }

    /**
     * palavras-chave em minusculo sao classificadas por igual
     */
    @Test
    void palavrasEmMinusculoSaoPalavrasChave() {
        List<Token> tokens = tokenizar("selecionar tabela coluna");
        assertTokens(tokens, new String[][]{
            {"palavra_chave", "selecionar"},
            {"palavra_chave", "tabela"},
            {"palavra_chave", "coluna"},
            {"fim", ""}
        });
    }

    /**
     * caractere invalido ($) dispara {ErroLexer}
     */
    @Test
    void caractereInvalidoLancaErroLexer() {
        assertThrows(ErroLexer.class,
                () -> tokenizar("SELECIONAR nome DE alunos ONDE idade > $18"));
    }

    /**
     * texto nao fechado com aspas dispara {ErroLexer}
     */
    @Test
    void textoNaoFechadoLancaErroLexer() {
        assertThrows(ErroLexer.class, () -> tokenizar("'texto sem fechar"));
    }

    /**
     * ponto decimal sem digitos depois dele dispara {ErroLexer}
     */
    @Test
    void pontoDecimalIsoladoLancaErroLexer() {
        assertThrows(ErroLexer.class, () -> tokenizar("valor = 5."));
    }

    /**
     * numero com mais de um ponto dispara {ErroLexer}
     */
    @Test
    void maisDeUmPontoDecimalLancaErroLexer() {
        assertThrows(ErroLexer.class, () -> tokenizar("valor = 1.2.3"));
    }

    /**
     * barra vertical isolada (sem outra |) dispara {ErroLexer}
     */
    @Test
    void barraVerticalIsoladaLancaErroLexer() {
        assertThrows(ErroLexer.class, () -> tokenizar("nome | tecido"));
    }

    // ------------------------------------------------
    // MODO MANUAL / VISUAL (console)
    // ------------------------------------------------
    // main() original do PortuSQL, mantido para quem quiser ver os tokens
    // no console (Eclipse: Run As -> Java Application).

    public static void main(String[] args) {

        // ---- SELECIONAR: colunas, operadores e condicoes -----------------
        testar("SELECIONAR nome, idade DE alunos ONDE idade > 18");

        testar("SELECIONAR * DE alunos");

        testar("SELECIONAR * DE alunos ONDE idade >= 18 E idade <= 25");

        testar("SELECIONAR * DE alunos ONDE status <> 'inativo'");

        testar("SELECIONAR * DE alunos ONDE status = 'ativo'");

        testar("SELECIONAR * DE alunos ONDE status NAO NULO");

        testar("SELECIONAR nome DE alunos ONDE nome PARECIDO COM 'Jo%'");

        testar("SELECIONAR nome || ' - ' || turma DE alunos");

        // numero decimal e operadores aritmeticos (+, -, *, /)
        testar("SELECIONAR valor DE vendas ONDE valor > 18.5");

        testar("SELECIONAR 1 + 2 * 3 - 4 / 5 DE tabela");

        // ---- CRIAR TABELA: tipos de dado e restricoes -------------------
        testar("CRIAR TABELA alunos (\n" +
               "    id INTEIRO CHAVE PRIMARIA,\n" +
               "    nome TEXTO NAO NULO,\n" +
               "    turma_id INTEIRO CHAVE ESTRANGEIRA REFERENCIA turmas(id)\n" +
               ")");

        testar("CRIAR TABELA vendas ( id inteiro, valor decimal, situacao texto )");

        // ---- INSERIR / ATUALIZAR / DELETAR ------------------------------
        testar("INSERIR (nome, idade) EM alunos VALORES ('Joao', 20)");

        testar("INSERIR EM alunos VALORES (1, 'Joao', 20)");

        testar("atualizar alunos DEFINIR idade = 21 ONDE nome PARECIDO COM 'Joao'");

        testar("DELETAR DE alunos ONDE idade > 18");

        // ---- ESVAZIAR / EXCLUIR / MOSTRAR -------------------------------
        testar("ESVAZIAR TABELA alunos");

        testar("EXCLUIR TABELA alunos");

        testar("MOSTRAR TABELA alunos");

        testar("MOSTRAR COLUNA idade DE alunos");

        // ---- ALTERAR TABELA ---------------------------------------------
        testar("ALTERAR TABELA alunos ADICIONAR COLUNA email VARCHAR");

        testar("ALTERAR TABELA alunos REMOVER COLUNA email");

        testar("ALTERAR TABELA alunos ALTERAR COLUNA idade decimal");

        testar("ALTERAR TABELA alunos RENOMEAR COLUNA nome PARA nome_completo");

        testar("ALTERAR TABELA alunos RENOMEAR PARA estudantes");

        testar("ALTERAR TABELA alunos ADICIONAR RESTRICAO pk_id id chave primaria");

        testar("ALTERAR TABELA alunos ADICIONAR RESTRICAO fk_turma turma_id chave estrangeira REFERENCIA turmas ( id )");

        testar("ALTERAR TABELA alunos ADICIONAR RESTRICAO ck_idade VERIFICAR idade > 0");

        testar("ALTERAR TABELA alunos REMOVER RESTRICAO fk_turma");

        // ---- BANCO DE DADOS ---------------------------------------------
        testar("USAR BANCO DE DADOS loja");

        testar("EXCLUIR BANCO DE DADOS escola");

        // ---- palavras-chave em minusculo --------------------------------
        testar("selecionar tabela coluna");

        // -------------------------------------------------------------
        // Casos de erro -- devem lancar ErroLexer
        // -------------------------------------------------------------
        testar("SELECIONAR nome DE alunos ONDE idade > $18");

        testar("SELECIONAR nome DE alunos ONDE nome = 'texto sem fechar");

        testar("SELECIONAR valor DE vendas ONDE valor = 5.");

        testar("SELECIONAR valor DE vendas ONDE valor = 1.2.3");

        testar("SELECIONAR nome | turma DE alunos");
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