<div align="center">

# PortuSQL

**SQL em português, para o ensino de bancos de dados.**

[![Release](https://img.shields.io/badge/release-v1.0.0-3fb950?style=flat-square)](../../releases/tag/v1.0.0)
[![Java](https://img.shields.io/badge/java-17%2B-ed8b00?style=flat-square)](https://adoptium.net/)
[![Banco](https://img.shields.io/badge/banco-H2-4479a1?style=flat-square)](https://www.h2database.com/)
[![Licença](https://img.shields.io/badge/licença-MIT-3fb950?style=flat-square)](LICENSE)

Um **dialeto de SQL escrito em português**, com uma IDE própria, feito para
quem está aprendendo banco de dados e precisa compartilhar a atenção entre a
*lógica* da consulta e o *idioma* em que ela é escrita.

</div>

---

## O projeto

O SQL é a linguagem universal dos bancos de dados — mas foi criado em inglês.
Quem está aprendendo enfrenta dois desafios ao mesmo tempo: entender a
**lógica** da consulta e lidar com o **vocabulário** (`WHERE`, `INSERT`,
`DELETE`...).

O PortuSQL inverte a prioridade:

```text
SELECIONAR nome, idade DE alunos ONDE idade > 18
```

Por trás, o comando é traduzido para SQL de verdade e executado no **H2**:

```sql
SELECT nome, idade FROM alunos WHERE idade > 18;
```

O resultado: você aprende a **lógica certa** — que vale para qualquer SGBD.

**Para quem é**

- Quem está dando ou fazendo o primeiro curso de banco de dados
- Quem quer a sintaxe em português para poder focar no raciocínio
- Quem quer um exemplo legível, pequeno e completo de como funciona um
  interpretador de linguagem

**O que o projeto é**

| Aspecto | Descrição |
|---|---|
| **Linguagem** | 20 comandos, 4 famílias (DDL, DML, DQL, banco de dados) |
| **Motor** | interpretador próprio: `Lexer → Parser → Tradutor → Executor` |
| **IDE** | interface Swing com editor de consultas, navegador de bancos e tema escuro |
| **Alvo** | banco H2 embarcado — nada para instalar |
| **Licença** | MIT |

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

Cada etapa é uma classe própria (`Engine/Lexer`, `Engine/Parser`,
`Engine/Tradutor`, `Conexao/Executor`), o que permite trocar qualquer uma
sem tocar nas outras — inclusive trocar o banco de destino.

A linguagem **não diferencia maiúsculas/minúsculas**: `selecionar`, `Selecionar`
e `SELECIONAR` são a mesma coisa.

**Erros que apontam o problema** — toda mensagem, seja léxica, sintática, de
tradução ou de execução, informa **linha e coluna**:

```text
Esperava 'VALORES', mas encontrei '(' (linha 1, coluna 19)
```

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

**Cláusulas e operadores** — `ONDE` (com `E`, `OU`, `NAO`, `NAO NULO`,
`PARECIDO COM`), `AGRUPAR POR`, `ORDENAR POR`, comparação (`=`, `>`, `<`, `>=`,
`<=`, `<>`), aritmética (`+`, `-`, `*`, `/`) e concatenação (`||`).

**Funções agregadas** — `CONTAR`, `SOMA`, `MED`, `MIN`, `MAX` — e `DISTINCT`
no `SELECIONAR`.

```text
SELECIONAR turma_id, CONTAR(*) DE alunos AGRUPAR POR turma_id
SELECIONAR DISTINCT cidade DE alunos
```

**`INSERIR` com várias linhas** — separe as tuplas por vírgula:

```text
INSERIR (id, nome, idade) EM alunos VALORES (1, 'João', 20), (2, 'Maria', 22)
```

Uma sessão completa, do começo ao fim:

```text
CRIAR TABELA alunos (id inteiro CHAVE PRIMARIA, nome texto NAO NULO, idade inteiro, cidade texto)

INSERIR (id, nome, idade, cidade) EM alunos VALORES
  (1, 'João', 20, 'Campinas'), (2, 'Maria', 22, 'Sumaré'), (3, 'Ana', 19, 'Campinas')

SELECIONAR nome, idade DE alunos ONDE idade >= 18 ORDER BY nome
SELECIONAR cidade, CONTAR(*) DE alunos AGRUPAR POR cidade
DELETAR DE alunos ONDE nome = 'Ana'
```

### Limitações atuais

- `USAR BANCO DE DADOS` é reconhecido, mas não gera SQL — o H2 embarcado não
  tem comando `USE`. A troca de banco é feita pela interface.
- `EXCLUIR BANCO DE DADOS` é bloqueado de propósito pelo tradutor; a exclusão
  é feita pela interface.
- `gint`, `cint` e `mint` são reconhecidos pelo Lexer, mas não têm equivalente
  no H2 — geram erro de tradução.
- `LIMIT` é aceito e **ignorado silenciosamente** — a cláusula some na
  tradução, sem aviso.
- Ainda não existem `JOIN`, `HAVING`, subconsultas nem comentários.
- Uma sentença por comando — o Enter não continua o comando.

---

## A IDE

Interface gráfica em Java/Swing, incluída no mesmo projeto:

**Homepage** com cards de acesso rápido: novo banco, configurações, sugestões,
dúvidas, comandos e "quem somos".

**Editor de consultas** com abas, realce de sintaxe e numeração de linhas:

| Atalho | Ação |
|---|---|
| `F5` | executar |
| `F6` | executar só a linha atual |
| `Ctrl+Enter` | executar |
| `Ctrl+N` | novo arquivo |
| `Ctrl+W` | fechar aba |
| `Ctrl+S` | salvar |
| `Ctrl+H` | histórico de execuções |
| `Ctrl+Shift+N` | novo banco de dados |

Menus de **Arquivo**, **Editar**, **Consulta**, **Banco**, **Ferramentas** e
**Ajuda**; barra lateral com acesso à homepage, aos bancos e às configurações;
painel inferior com as abas **Resultados**, **Mensagens**, **Plano de
Execução** e **Histórico**.

> **Em construção:** salvar arquivo em disco, histórico de execuções,
> configurações e o botão **Parar** ainda são placeholders — exibem um aviso
> na área de mensagens em vez de executar a ação.

**Bancos** — cada banco é um arquivo H2 dentro de `~/.portusql`, criado e
excluído pela própria interface (navegador de bancos). Ao abrir, a IDE conecta
ao banco de testes dedicado `teste_portusql` — o seu banco real nunca é
aberto sem você pedir.

Tema escuro em toda a interface.

---

## Começando

### Requisitos

Java 17 ou superior. Nada além disso — o H2 é embarcado.

### Instalador (Windows)

Baixe o `PortuSQL-1.0.0.msi` nos [anexos da release](../../releases) e
instale. O instalador já traz o Java embutido: **não é preciso instalar
nada** além dele.

### Executável

Baixe o `PortuSQL.jar` e rode:

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

## Estrutura do projeto

```
src/main/java/
├── Engine/       Lexer, Parser, Tradutor, catálogo de palavras-chave, erros
├── Comandos/     um objeto imutável por comando (a árvore sintática)
├── Conexao/      conexão com o H2 embarcado, execução de SQL e resultado
├── Telas/        a IDE: homepage, editor, navegador de bancos, tema
├── PortuSQL/     ponto de entrada
└── Imagens/      logo da aplicação

src/main/resources/    ícone da aplicação
tools/                 auditoria da documentação contra o código
.github/workflows/     build do .jar e do instalador .msi
.github/release-notes/ descrição de cada release
```

| Pacote | Responsabilidade |
|---|---|
| `Engine` | a linguagem: tokenização, parsing, tradução e os erros de cada etapa |
| `Comandos` | a árvore sintática — uma classe imutável por comando, sem comportamento de execução |
| `Conexao` | a ponte com o banco: URL, `Statement`, resultado e contagem de linhas |
| `Telas` | a IDE Swing — não sabe nada de SQL, só consome `Comando` e `Resultado` |

A separação é proposital: a **linguagem** não sabe que o banco é H2, e a
**IDE** não sabe traduzir SQL. Trocar o banco de destino ou a interface
gráfica não exige mexer no motor.

---

## Documentação

| Arquivo | Conteúdo |
|---|---|
| [`ESPECIFICACAO.md`](ESPECIFICACAO.md) | especificação formal da linguagem: gramática, semântica, tipos, restrições e tratamento de erros |
| [`COMANDOS.md`](COMANDOS.md) | referência dos 20 comandos, com o SQL (H2) gerado por trás de cada um |
| [`docs/PortuSQL-apresentacao.md`](docs/PortuSQL-apresentacao.md) · [PDF](docs/PortuSQL-apresentacao.pdf) | apresentação de curso introdutório de bancos de dados, com prática guiada |

---

## Desenvolvimento

```bash
./mvnw package          # compila e gera o .jar shaded
./mvnw clean package    # do zero
```

O `pom.xml` usa o **shade plugin** para embutir o H2 no executável — o
`PortuSQL.jar` sai pronto para rodar, sem classpath externo.

**Artefatos de release** — o workflow `.github/workflows/release.yml` gera o
`.jar` (Linux) e o instalador `.msi` (Windows, via `jpackage` com um runtime
enxuto montado com `jlink`). O workflow **apenas gera os artefatos**: a
publicação da release é feita à mão, o que mantém o controle de quando cada
versão aparece.

**Auditoria da documentação** — a especificação e a referência de comandos são
verificadas contra o código-fonte, para que não diverjam:

```bash
python3 tools/auditar_docs.py
```

**Testes** — ainda não há suíte automatizada. A validação hoje é feita contra
um H2 real (comandos de DDL, DML, DQL, agregadas, `DISTINCT` e os caminhos de
erro) e pelo auditor de documentação.

---

## Roadmap

Coisas que ainda não existem, em ordem de importance para quem está
aprendendo:

1. `JOIN` — a lacuna mais sentida de uma linguagem de consulta
2. `HAVING` — filtrar grupos depois de `AGRUPAR POR`
3. Subconsultas e `IN`
4. `AUTO_INCREMENT` — hoje `CHAVE PRIMARIA` não gera id automático
5. `LIMIT` e `OFFSET` — hoje a cláusula é aceita e descartada em silêncio
6. Comentários na linguagem (`--` e `/* */`)
7. Executar vários comandos separados por `;` na mesma sessão
8. Funções de texto e data (`UPPER`, `LOWER`, `LENGTH`, `SUBSTRING`, `COALESCE`

Na IDE:

9. Salvar arquivo em disco, histórico de execuções e tela de configurações
10. Interromper de fato uma execução em andamento (o botão **Parar**)
11. Aba **Plano de Execução** com o SQL gerado passo a passo
12. Suíte de testes automatizados (JUnit já está reservado no `pom.xml`)

---

## Contribuindo

O projeto é aberto a ideias, correções e material de aula.

- **Dúvidas**: [Discussões](https://github.com/00salociN/PortuSQL/discussions)
- **Sugestões e bugs**: [Issues](https://github.com/00salociN/PortuSQL/issues)
- **Contribuir**: fork, um branch, um pull request — mas antes, abra uma issue
  para alinharmos o escopo

Ao mexer no motor, mantenha a documentação junto: `ESPECIFICACAO.md` e
`COMANDOS.md` descrevem a linguagem, e o auditor verifica se continuam
batendo com o código.

---

## Licença

MIT — veja [LICENSE](LICENSE).

Desenvolvido por **Nicolas Gabriel Venâncio dos Santos** como projeto de
ensino de bancos de dados.

Banco de dados alvo: **H2 Database** (embarcado). Construído com **Java 17**
e **Maven**.
