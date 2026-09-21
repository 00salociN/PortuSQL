package Engine;

import java.util.List;

import org.junit.jupiter.api.Test;

import Comandos.AdicionarRestricaoVerificar;
import Comandos.Comando;
import Comandos.CriarTabela;
import Comandos.ExcluirBancoDados;
import Comandos.Inserir;
import Comandos.UsarBancoDados;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Testes automatizados (JUnit 5) do pipeline completo do PortuSQL:
 * texto -> {@link Lexer} -> {@link Parser} -> {@link Tradutor} -> SQL (H2).
 *
 * <p>antes de virar classe de teste, este arquivo era um {@code main()} manual
 * que imprimia AST e SQL no console; agora cada comando virou um {@code Test}
 * que confere o SQL gerado (ou o erro esperado).
 *
 * <p>alem dos {@code @Test}, a classe mantem o {@code main()} original do
 * PortuSQL: rode-a como uma "Java Application" (no Eclipse, botao direito na
 * classe -> Run As -> Java Application) para ver o AST e o SQL de cada
 * comando impressos no console, exatamente como antes.
 *
 * <p>observacao: apos as correcoes do motor, os testes passam a conferir o SQL
 * correto (valores de texto com aspas, espacos entre palavras e comandos
 * que antes falhavam: RENOMEAR RESTRICAO e ADICIONAR RESTRICAO ... VERIFICAR).
 *
 * @author Nicolas Gabriel Venâncio dos Santos
 * @version 1.0
 * @see Lexer
 * @see Parser
 * @see Tradutor
 */
public class TesteCompleto {

    /** monta o comando a partir do texto PortuSQL */
    private static Comando comando(String textoPortuSQL) {
        List<Token> tokens = new Lexer(textoPortuSQL).tokenizar();
        return new Parser(tokens).parserComando();
    }

    /** traduz o texto PortuSQL para o SQL (H2) */
    private static String sql(String textoPortuSQL) {
        return new Tradutor().traducao(comando(textoPortuSQL));
    }

    // ------------------------------------------------
    // SELECIONAR
    // ------------------------------------------------

    @Test
    void selecionarTudoDeTabela() {
        assertEquals("Select * from alunos;", sql("SELECIONAR * DE alunos"));
    }

    @Test
    void selecionarComCondicaoTraduzEAndLike() {
        assertEquals("Select * from alunos where idade > 18 AND status LIKE 'ativo';",
                sql("SELECIONAR * DE alunos ONDE idade > 18 E status PARECIDO COM 'ativo'"));
    }

    @Test
    void selecionarComOuENao() {
        assertEquals("Select * from alunos where idade > 18 OR status NOT NULO;",
                sql("SELECIONAR * DE alunos ONDE idade > 18 OU status NAO NULO"));
    }

    @Test
    void condicaoNaoTraduzPalavrasDentroDeTexto() {
        assertEquals("Select * from alunos where nome = 'Pedro E Ana';",
                sql("SELECIONAR * DE alunos ONDE nome = 'Pedro E Ana'"));
    }

    @Test
    void selecionarAgrupandoEOrdenando() {
        assertEquals("Select cidade from alunos group by cidade order by cidade;",
                sql("SELECIONAR cidade DE alunos AGRUPAR POR cidade ORDENAR POR cidade"));
    }

    @Test
    void selecionarComListaDeColunas() {
        assertEquals("Select nome, idade from alunos;",
                sql("SELECIONAR nome, idade DE alunos"));
    }

    // ------------------------------------------------
    // CRIAR TABELA
    // ------------------------------------------------

    @Test
    void criarTabelaComChavePrimaria() {
        assertEquals("Create Table alunos (\n"
                + "    id int Primary Key,\n"
                + "    nome text,\n"
                + "    idade int\n"
                + ");",
                sql("CRIAR TABELA alunos ( id inteiro CHAVE PRIMARIA, nome texto, idade inteiro )"));
    }

    @Test
    void criarTabelaComColunaNaoNulo() {
        assertEquals("Create Table alunos (\n"
                + "    id int Primary Key,\n"
                + "    nome text Not Null\n"
                + ");",
                sql("CRIAR TABELA alunos ( id inteiro CHAVE PRIMARIA, nome texto nao nulo )"));
    }

    @Test
    void criarTabelaMontaDefinicoesDasColunas() {
        CriarTabela criar = (CriarTabela) comando(
                "CRIAR TABELA alunos ( id inteiro CHAVE PRIMARIA, nome texto )");
        assertEquals("alunos", criar.getNomeTabela());
        assertEquals(2, criar.getColunas().size());
        assertEquals("id", criar.getColunas().get(0).getNome());
        assertEquals("inteiro", criar.getColunas().get(0).getTipoDado());
        assertTrue(criar.getColunas().get(0).isChavePrimaria());
        assertEquals("texto", criar.getColunas().get(1).getTipoDado());
        assertFalse(criar.getColunas().get(1).isChavePrimaria());
    }

    // ------------------------------------------------
    // INSERIR
    // ------------------------------------------------

    @Test
    void inserirComListaDeColunas() {
        assertEquals("Insert Into alunos (nome, idade) Values ('Joao', 20);",
                sql("INSERIR (nome, idade) EM alunos VALORES ('Joao', 20)"));
    }

    @Test
    void inserirSemListaDeColunas() {
        assertEquals("Insert Into alunos Values (1, 'Joao');",
                sql("INSERIR EM alunos VALORES (1, 'Joao')"));
    }

    @Test
    void inserirMontaValores() {
        Inserir inserir = (Inserir) comando("INSERIR (id, idade) EM alunos VALORES (1, 20)");
        assertEquals("alunos", inserir.getTabela());
        assertEquals(List.of("id", "idade"), inserir.getColunas());
        assertEquals(List.of("1", "20"), inserir.getValores());
    }

    @Test
    void inserirComValorNulo() {
        assertEquals("Insert Into alunos (nome, telefone) Values ('Joao', null);",
                sql("INSERIR (nome, telefone) EM alunos VALORES ('Joao', nulo)"));
    }

    // ------------------------------------------------
    // ATUALIZAR
    // ------------------------------------------------

    @Test
    void atualizarComCondicao() {
        assertEquals("Update alunos Set idade = 21 where nome LIKE 'Joao';",
                sql("atualizar alunos DEFINIR idade = 21 ONDE nome PARECIDO COM 'Joao'"));
    }

    @Test
    void atualizarTabelaInteiraSemCondicao() {
        assertEquals("Update alunos Set idade = 10;",
                sql("atualizar alunos DEFINIR idade = 10"));
    }

    @Test
    void atualizarComValorNulo() {
        assertEquals("Update alunos Set telefone = null;",
                sql("atualizar alunos DEFINIR telefone = nulo"));
    }

    // ------------------------------------------------
    // DELETAR / ESVAZIAR / EXCLUIR TABELA
    // ------------------------------------------------

    @Test
    void deletarComCondicao() {
        assertEquals("Delete From alunos where idade > 18;",
                sql("DELETAR DE alunos ONDE idade > 18"));
    }

    @Test
    void esvaziarTabela() {
        assertEquals("Truncate Table alunos;", sql("ESVAZIAR TABELA alunos"));
    }

    @Test
    void excluirTabela() {
        assertEquals("Drop Table alunos;", sql("EXCLUIR TABELA alunos"));
    }

    // ------------------------------------------------
    // MOSTRAR
    // ------------------------------------------------

    @Test
    void mostrarTabela() {
        assertEquals("Show Columns From alunos;", sql("MOSTRAR TABELA alunos"));
    }

    @Test
    void mostrarColuna() {
        assertEquals("Select * From Information_Schema.Columns Where Table_Name = 'ALUNOS' And Column_Name = 'IDADE';",
                sql("MOSTRAR COLUNA idade DE alunos"));
    }

    // ------------------------------------------------
    // ALTERAR TABELA
    // ------------------------------------------------

    @Test
    void adicionarColuna() {
        assertEquals("Alter Table alunos Add Column email text;",
                sql("ALTERAR TABELA alunos ADICIONAR COLUNA email texto"));
    }

    @Test
    void removerColuna() {
        assertEquals("Alter Table alunos Drop Column email;",
                sql("ALTERAR TABELA alunos REMOVER COLUNA email"));
    }

    @Test
    void alterarColuna() {
        assertEquals("Alter Table alunos Alter Column idade decimal;",
                sql("ALTERAR TABELA alunos ALTERAR COLUNA idade decimal"));
    }

    @Test
    void renomearColuna() {
        assertEquals("Alter table alunos Rename Column nome To nome_completo;",
                sql("ALTERAR TABELA alunos RENOMEAR COLUNA nome PARA nome_completo"));
    }

    @Test
    void renomearTabela() {
        assertEquals("Alter Table alunos Rename To estudantes;",
                sql("ALTERAR TABELA alunos RENOMEAR PARA estudantes"));
    }

    @Test
    void adicionarRestricaoChavePrimaria() {
        assertEquals("Alter Table alunos Add Constraint pk_id primary key (id);",
                sql("ALTERAR TABELA alunos ADICIONAR RESTRICAO pk_id id chave primaria"));
    }

    @Test
    void adicionarRestricaoChaveEstrangeira() {
        assertEquals("Alter Table alunos Add Constraint fk_turma foreign key (turma_id) References turmas(id);",
                sql("ALTERAR TABELA alunos ADICIONAR RESTRICAO fk_turma turma_id chave estrangeira REFERENCIA turmas ( id )"));
    }

    @Test
    void removerRestricao() {
        assertEquals("Alter Table alunos Drop Constraint fk_turma;",
                sql("ALTERAR TABELA alunos REMOVER RESTRICAO fk_turma"));
    }

    // ------------------------------------------------
    // BANCO DE DADOS (montam comando, mas nao sao traduzidos)
    // ------------------------------------------------

    @Test
    void usarBancoDeDadosMontaComandoSemTraducao() {
        UsarBancoDados usar = (UsarBancoDados) comando("USAR BANCO DE DADOS loja");
        assertEquals("loja", usar.getNomeBanco());
        IllegalArgumentException erro = assertThrows(IllegalArgumentException.class,
                () -> sql("USAR BANCO DE DADOS loja"));
        assertTrue(erro.getMessage().contains("UsarBancoDados"));
    }

    @Test
    void excluirBancoDeDadosMontaComandoSemTraducao() {
        ExcluirBancoDados excluir = (ExcluirBancoDados) comando("EXCLUIR BANCO DE DADOS escola");
        assertEquals("escola", excluir.getNomeBanco());
        IllegalStateException erro = assertThrows(IllegalStateException.class,
                () -> sql("EXCLUIR BANCO DE DADOS escola"));
        assertEquals("Esse comando não pode ser executado", erro.getMessage());
    }

    @Test
    void adicionarRestricaoVerificar() {
        AdicionarRestricaoVerificar verificar = (AdicionarRestricaoVerificar) comando(
                "ALTERAR TABELA alunos ADICIONAR RESTRICAO ck_idade VERIFICAR idade > 0");
        assertEquals("idade > 0", verificar.condicao());
        assertEquals("Alter Table alunos Add Constraint ck_idade Check (idade > 0);",
                sql("ALTERAR TABELA alunos ADICIONAR RESTRICAO ck_idade VERIFICAR idade > 0"));
    }

    // ------------------------------------------------
    // ERROS DE SINTAXE (ErroParser) E LEXICOS (ErroLexer)
    // ------------------------------------------------

    @Test
    void excluirExigeTabelaOuBancoDeDados() {
        ErroParser erro = assertThrows(ErroParser.class,
                () -> comando("Excluir DE alunos ONDE idade > 18"));
        assertTrue(erro.getMessage().contains("TABELA"));
    }

    @Test
    void removerTabelaNaoExisteNoParser() {
        ErroParser erro = assertThrows(ErroParser.class,
                () -> comando("REMOVER TABELA alunos"));
        assertTrue(erro.getMessage().contains("REMOVER"));
    }

    @Test
    void mostrarExigeTabelaOuColuna() {
        ErroParser erro = assertThrows(ErroParser.class,
                () -> comando("MOSTRAR alunos"));
        assertTrue(erro.getMessage().contains("TABELA"));
    }

    @Test
    void restricaoExigeColunaAntesDaDefinicao() {
        ErroParser erro = assertThrows(ErroParser.class,
                () -> comando("ALTERAR TABELA alunos ADICIONAR RESTRICAO fk_turma chave estrangeira turma_id"));
        assertTrue(erro.getMessage().contains("chave"));
    }

    @Test
    void renomearRestricao() {
        assertEquals("Alter Table alunos Rename Constraint fk_turma To fk_turma_aluno;",
                sql("ALTERAR TABELA alunos RENOMEAR RESTRICAO fk_turma PARA fk_turma_aluno"));
    }

    @Test
    void caractereInvalidoDisparaErroLexer() {
        assertThrows(ErroLexer.class,
                () -> sql("SELECIONAR nome DE alunos ONDE idade > $18"));
    }

    // ------------------------------------------------
    // MODO MANUAL / VISUAL (console)
    // ------------------------------------------------
    // main() original do PortuSQL, mantido para quem quiser ver o AST e o
    // SQL no console (Eclipse: Run As -> Java Application).

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