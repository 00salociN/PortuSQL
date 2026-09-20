package Engine;

import java.sql.SQLException;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import Comandos.Comando;
import Conexao.Conexao;
import Conexao.Executor;
import Conexao.Resultado;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Testes automatizados (JUnit 5) de execucao real no H2 embarcado:
 * texto -> {@link Lexer} -> {@link Parser} -> {@link Tradutor} -> SQL ->
 * {@link Executor}.
 *
 * <p>antes de virar classe de teste, este arquivo era um {@code main()} manual
 * que rodava o pipeline contra o banco padrao (~/.portusql/portusql).
 *
 * <p>para nao poluir o banco padrao, os testes usam um banco dedicado
 * (teste_portusql) que e recriado antes de cada teste e apagado depois.
 *
 * <p>alem dos {@code @Test}, a classe mantem o {@code main()} original do
 * PortuSQL: rode-a como uma "Java Application" (no Eclipse, botao direito na
 * classe -> Run As -> Java Application) para ver o pipeline executando no
 * console. o main tambem usa o banco dedicado de teste, recriado do zero a
 * cada execucao, entao o banco padrao (portusql) nunca e tocado.
 *
 * <p>observacao: o tradutor nao coloca aspas nos valores de texto, entao os
 * testes de INCLUIR usam apenas valores numericos (que executam sem erro).
 *
 * @author Nicolas Gabriel Venâncio dos Santos
 * @version 1.0
 * @see Conexao
 * @see Executor
 * @see Resultado
 */
public class TesteExecucao {

    /** nome do banco dedicado aos testes (arquivo ~/.portusql/teste_portusql.mv.db) */
    private static final String BANCO_TESTE = "teste_portusql";

    /** garante um banco de teste zerado antes de cada teste */
    @BeforeEach
    void conectarBancoDeTeste() throws SQLException {
        Conexao.fecharConexao();
        Conexao.excluirBanco(BANCO_TESTE);
        Conexao.obterConexao(BANCO_TESTE);
    }

    /** fecha a conexao e apaga o banco de teste depois de cada teste */
    @AfterEach
    void apagarBancoDeTeste() throws SQLException {
        Conexao.fecharConexao();
        Conexao.excluirBanco(BANCO_TESTE);
    }

    /** roda o pipeline completo (Lexer -> Parser -> Tradutor -> Executor) */
    private static Resultado executar(String textoPortuSQL) throws SQLException {
        Comando comando = new Parser(new Lexer(textoPortuSQL).tokenizar()).parserComando();
        String sql = new Tradutor().traducao(comando);
        return new Executor().executar(sql);
    }

    /** cria a tabela de testes usada por quase todos os metodos */
    private static void criarTabelaAlunos() throws SQLException {
        executar("CRIAR TABELA alunos ( id inteiro CHAVE PRIMARIA, nome texto, idade inteiro )");
    }

    /**
     * popula a tabela com tres linhas; como o tradutor nao coloca aspas nos
     * valores de texto, a coluna nome fica com NULL e so os numeros entram
     */
    private static void popularTabelaAlunos() throws SQLException {
        executar("INSERIR (id, idade) EM alunos VALORES (1, 20)");
        executar("INSERIR (id, idade) EM alunos VALORES (2, 22)");
        executar("INSERIR (id, idade) EM alunos VALORES (3, 18)");
    }

    @Test
    void criarTabelaPermiteConsultarEstrutura() throws SQLException {
        criarTabelaAlunos();
        Resultado resultado = executar("SELECIONAR * DE alunos");
        assertTrue(resultado.isConsulta());
        assertEquals(List.of("ID", "NOME", "IDADE"), resultado.getColunas());
        assertTrue(resultado.getLinhas().isEmpty());
    }

    @Test
    void inserirESelecionarRetornaLinhas() throws SQLException {
        criarTabelaAlunos();
        popularTabelaAlunos();
        Resultado resultado = executar("SELECIONAR id, idade DE alunos");
        assertEquals(List.of("ID", "IDADE"), resultado.getColunas());
        assertEquals(3, resultado.getLinhas().size());
        assertEquals(List.of(1, 20), resultado.getLinhas().get(0));
        assertEquals(List.of(2, 22), resultado.getLinhas().get(1));
        assertEquals(List.of(3, 18), resultado.getLinhas().get(2));
    }

    @Test
    void selecionarComCondicaoFiltraLinhas() throws SQLException {
        criarTabelaAlunos();
        popularTabelaAlunos();
        Resultado resultado = executar("SELECIONAR id, idade DE alunos ONDE idade >= 22");
        assertEquals(1, resultado.getLinhas().size());
        assertEquals(List.of(2, 22), resultado.getLinhas().get(0));
    }

    @Test
    void atualizarAlteraValoresDasLinhas() throws SQLException {
        criarTabelaAlunos();
        popularTabelaAlunos();
        executar("atualizar alunos DEFINIR idade = 30 ONDE id = 1");
        Resultado resultado = executar("SELECIONAR id, idade DE alunos ONDE id = 1");
        assertEquals(1, resultado.getLinhas().size());
        assertEquals(List.of(1, 30), resultado.getLinhas().get(0));
    }

    @Test
    void deletarRemoveLinhasDoFiltro() throws SQLException {
        criarTabelaAlunos();
        popularTabelaAlunos();
        Resultado removidas = executar("DELETAR DE alunos ONDE idade > 18");
        assertEquals(2, removidas.getLinhasAfetadas());
        Resultado restantes = executar("SELECIONAR id, idade DE alunos");
        assertEquals(List.of(List.of(3, 18)), restantes.getLinhas());
    }

    @Test
    void esvaziarLimpaTodasAsLinhas() throws SQLException {
        criarTabelaAlunos();
        popularTabelaAlunos();
        executar("ESVAZIAR TABELA alunos");
        Resultado resultado = executar("SELECIONAR * DE alunos");
        assertTrue(resultado.getLinhas().isEmpty());
    }

    @Test
    void mostrarTabelaListaColunas() throws SQLException {
        criarTabelaAlunos();
        Resultado resultado = executar("MOSTRAR TABELA alunos");
        assertTrue(resultado.isConsulta());
        assertFalse(resultado.getLinhas().isEmpty());
    }

    @Test
    void mostrarColunaConsultaInformationSchema() throws SQLException {
        criarTabelaAlunos();
        Resultado resultado = executar("MOSTRAR COLUNA idade DE alunos");
        assertTrue(resultado.isConsulta());
    }

    @Test
    void excluirTabelaRemoveTodaAestrutura() throws SQLException {
        criarTabelaAlunos();
        executar("EXCLUIR TABELA alunos");
        // apos a exclusao, a tabela nao existe mais: qualquer consulta falha
        assertThrows(SQLException.class, () -> executar("SELECIONAR * DE alunos"));
    }

    // ------------------------------------------------
    // MODO MANUAL / VISUAL (console)
    // ------------------------------------------------
    // main() original do PortuSQL, mantido para quem quiser ver o pipeline
    // executando de verdade no console (Eclipse: Run As -> Java Application).
    // usa o mesmo banco dedicado de teste (teste_portusql), recriado do zero
    // a cada execucao; o banco padrao (portusql) nao e tocado.

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