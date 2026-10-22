<div align="center">

# PortuSQL

**SQL em português, para o ensino de bancos de dados.**

[![Release](https://img.shields.io/badge/release-v1.0.0-3fb950?style=flat-square)](../../releases/tag/v1.0.0)
[![Java](https://img.shields.io/badge/java-17%2B-ed8b00?style=flat-square)](https://adoptium.net/)
[![Licença](https://img.shields.io/badge/licença-MIT-3fb950?style=flat-square)](LICENSE)

Um **dialeto de SQL em português**, feito para quem está aprendendo banco de dados
e precisa compartilhar a concentração entre a *lógica* da consulta e o *idioma*
em que ela é escrita.

</div>

---

## O problema

O SQL é a linguagem universal dos bancos de dados — mas foi criado em inglês.
Quem está aprendendo enfrenta dois desafios ao mesmo tempo: entender a
**lógica** da consulta e lidar com o **vocabulário** em inglês (`WHERE`,
`INSERT`, `DELETE`...).

O PortuSQL inverte a prioridade:

```text
SELECIONAR nome, idade DE alunos ONDE idade > 18
```

Por trás, o comando é traduzido para SQL de verdade e executado no **H2**:

```sql
SELECT nome, idade FROM alunos WHERE idade > 18;
```

O resultado: você aprende a **lógica certa** — que vale para qualquer SGBD.

---

## Como funciona

Todo comando passa por quatro etapas:

```
Lexer → Parser → Tradutor → Executor
```

| Etapa | O que faz | Erro típico |
|---|---|---|
| **Lexer** | quebra o texto em *tokens* (palavras, números, símbolos) | caractere inválido, aspas não fechadas |
| **Parser** | valida a estrutura e monta a árvore sintática (objeto `Comando`) | palavra fora de ordem, comando incompleto |
| **Tradutor** | converte o comando para SQL do H2 | tipo de dado sem tradução |
| **Executor** | roda o SQL e devolve o resultado | violou uma regra do banco |

A linguagem **não diferencia maiúsculas/minúsculas**: `selecionar`, `Selecionar`
e `SELECIONAR` são a mesma coisa.

---

## A linguagem

**20 comandos**, em quatro famílias:

| Família | Para quê | Comandos |
|---|---|---|
| **DDL** — estrutura | definir e mudar tabelas | `CRIAR TABELA`, `ALTERAR TABELA` (9 ações), `ESVAZIAR TABELA`, `EXCLUIR TABELA` |
| **DML** — dados | guardar e mexer nas linhas | `INSERIR`, `ATUALIZAR`, `DELETAR` |
| **DQL** — consultas | ler e inspecionar | `SELECIONAR`, `MOSTRAR TABELA`, `MOSTRAR COLUNA` |
| **Banco** | gerenciar bancos | `USAR BANCO DE DADOS`, `EXCLUIR BANCO DE DADOS` |

**Tipos de dado** — `int`/`inteiro`, `texto`, `data`, `decimal`, `hora`,
`data_hora`, `CARACTERE VARIAVEL(n)`, `CARACTERE FIXO(n)`.

**Restrições** — `CHAVE PRIMARIA`, `NAO NULO`, `CHAVE ESTRANGEIRA`
(com `REFERENCIA`), `UNICA` e `VERIFICAR` (CHECK).

**Cláusulas e operadores** — `ONDE` (com `E`, `OU`, `NAO`, `PARECIDO COM`),
`AGRUPAR POR`, `ORDENAR POR`, comparação (`=`, `>`, `<`, `>=`, `<=`, `<>`),
aritmética (`+`, `-`, `*`, `/`) e concatenação (`||`).

**Funções agregadas** — `CONTAR`, `SOMA`, `MED`, `MIN`, `MAX` — e `DISTINCT`
no `SELECIONAR`.

```text
SELECIONAR turma_id, CONTAR(*) DE alunos AGRUPAR POR turma_id
SELECIONAR DISTINCT cidade DE alunos
```

**Erros com posição** — toda mensagem de erro léxico, sintático, de tradução
ou de execução informa **linha e coluna**.

---

## A IDE

Interface gráfica em Java/Swing, incluído no mesmo projeto:

- **Homepage** com cards de acesso rápido.
- **Editor de consultas** com abas, numeração de linha e realce de sintaxe.
- Atalhos: `F5` ou `Ctrl+Enter` (executar), `F6` (executar só a linha atual),
  botão **Parar** para interromper.
- Painéis de **Resultados** (grade) e **Mensagens** (erros e avisos).
- **Navegador de bancos**: criar, abrir e excluir bancos pela interface — cada
  banco é um arquivo H2 dentro de `~/.portusql`.
- Menus de Arquivo, Editar, Consulta, Banco, Ferramentas e Ajuda, com
  histórico de execuções.
- Tema escuro.
- Ao abrir, já conecta ao banco de testes dedicado `teste_portusql` — o seu
  banco real nunca é aberto sem você pedir.

---

## Como executar

###instalador (Windows)

Baixe o `PortuSQL-1.0.0.msi` nos [anexos da release v1.0.0](../../releases/tag/v1.0.0)
e instale. O instalador já traz o Java embutido — **não é preciso instalar
nada** além dele.

### Executável (Java 17 ou superior)

Baixe o `PortuSQL.jar` nos [anexos da release v1.0.0](../../releases/tag/v1.0.0):

```bash
java -jar PortuSQL.jar
```

### A partir do código

```bash
git clone https://github.com/00salociN/PortuSQL.git
cd PortuSQL
./mvnw package
java -jar target/PortuSQL-1.0.0.jar
```

O `./mvnw` é o **Maven Wrapper**: basta ter o Java instalado, o Maven é baixado
automaticamente.

---

## Documentação

| Arquivo | Conteúdo |
|---|---|
| [`ESPECIFICACAO.md`](ESPECIFICACAO.md) | especificação formal da linguagem: gramática, semântica, tipos, restrições e tratamento de erros |
| [`COMANDOS.md`](COMANDOS.md) | referência dos 20 comandos, com o SQL (H2) gerado por trás de cada um |
| [`docs/PortuSQL-apresentacao.md`](docs/PortuSQL-apresentacao.md) · [PDF](docs/PortuSQL-apresentacao.pdf) | apresentação de curso introdutório de bancos de dados |

---

## Estrutura do projeto

```
src/main/java/
├── Engine/       Lexer, Parser, Tradutor, erros e o catálogo de palavras-chave
├── Comandos/     um objeto imutável por comando (a árvore sintática)
├── Conexao/      conexão com o H2 embarcado e execução de SQL
├── Telas/        a IDE (Swing): homepage, editor, navegador de bancos
├── PortuSQL/     ponto de entrada
└── Imagens/      logo da aplicação

.github/workflows/release.yml   build do .jar e do instalador .msi
```

---

## Limitações conhecidas

1. `USAR BANCO DE DADOS` é reconhecido, mas não gera SQL (o H2 embarcado não
   tem comando `USE`); a troca de banco é feita pela interface.
2. `EXCLUIR BANCO DE DADOS` é bloqueado de propósito pelo tradutor; a exclusão
   é feita pela interface.
3. `gint`, `cint` e `mint` são reconhecidos pelo Lexer, mas não têm equivalente
   no H2 — geram erro de tradução.
4. Ainda não existem `JOIN`, `HAVING`, subconsultas, comentários na linguagem
   nem execução de múltiplos comandos em sequência.
5. Uma sentença por comando — o Enter não continua o comando.

---

## Licença

MIT — veja [LICENSE](LICENSE).

Bancos de dados alvo: **H2 Database** (embarcado). Construído com **Java 17**
e **Maven**.
