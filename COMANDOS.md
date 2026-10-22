# Referência de Comandos do PortuSQL

> Guia de referência da linguagem **PortuSQL**: sintaxe de cada comando,
> quando usar e o SQL (H2) que é gerado por trás dos panos.

## Sumário

- [Como ler este guia](#como-ler-este-guia)
- [Visão geral](#visão-geral)
- [Comandos de definição de dados (DDL)](#comandos-de-definição-de-dados-ddl)
  - [CRIAR TABELA](#1-criar-tabela)
  - [ALTERAR TABELA — ADICIONAR COLUNA](#2-alterar-tabela--adicionar-coluna)
  - [ALTERAR TABELA — ADICIONAR RESTRICAO](#3-alterar-tabela--adicionar-restricao)
  - [ALTERAR TABELA — ADICIONAR RESTRICAO ... VERIFICAR](#4-alterar-tabela--adicionar-restricao--verificar)
  - [ALTERAR TABELA — REMOVER COLUNA](#5-alterar-tabela--remover-coluna)
  - [ALTERAR TABELA — REMOVER RESTRICAO](#6-alterar-tabela--remover-restricao)
  - [ALTERAR TABELA — ALTERAR COLUNA](#7-alterar-tabela--alterar-coluna)
  - [ALTERAR TABELA — RENOMEAR COLUNA](#8-alterar-tabela--renomear-coluna)
  - [ALTERAR TABELA — RENOMEAR RESTRICAO](#9-alterar-tabela--renomear-restricao)
  - [ALTERAR TABELA — RENOMEAR PARA (tabela)](#10-alterar-tabela--renomear-para-tabela)
  - [ESVAZIAR TABELA](#11-esvaziar-tabela)
  - [EXCLUIR TABELA](#12-excluir-tabela)
- [Comandos de manipulação de dados (DML)](#comandos-de-manipulação-de-dados-dml)
  - [INSERIR](#13-inserir)
  - [ATUALIZAR](#14-atualizar)
  - [DELETAR](#15-deletar)
- [Comandos de consulta (DQL)](#comandos-de-consulta-dql)
  - [SELECIONAR](#16-selecionar)
  - [MOSTRAR TABELA](#17-mostrar-tabela)
  - [MOSTRAR COLUNA](#18-mostrar-coluna)
- [Comandos de banco de dados](#comandos-de-banco-de-dados)
  - [USAR BANCO DE DADOS](#19-usar-banco-de-dados)
  - [EXCLUIR BANCO DE DADOS](#20-excluir-banco-de-dados)
- [Tipos de dados](#tipos-de-dados)
- [Restrições](#restrições)
- [Cláusulas e operadores](#cláusulas-e-operadores)
- [Limitações conhecidas](#limitações-conhecidas)

---

## Como ler este guia

Para cada comando você encontra:

| Item | Descrição |
|---|---|
| **Sintaxe** | a "receita" do comando. `<algo>` é um espaço que você preenche; `[algo]` é opcional; `, ...` significa que pode repetir separado por vírgula. |
| **Quando usar** | a situação em que o comando faz sentido. |
| **Explicação** | o que o comando faz, parte por parte. |
| **Exemplo** | um comando PortuSQL seguido do SQL que o PortuSQL gera (coluna H2). |

> A linguagem **não é sensível a maiúsculas/minúsculas**: `selecionar`, `Selecionar` e `SELECIONAR` são a mesma coisa.

---

## Visão geral

Todo comando digitado passa por 4 etapas:

```
Lexer → Parser → Tradutor → Executor
```

1. **Lexer** — transforma o texto em tokens (palavras-chave, identificadores, números, texto, símbolos).
2. **Parser** — valida a estrutura e monta um objeto `Comando`.
3. **Tradutor** — converte para SQL do banco H2.
4. **Executor** — roda o SQL e devolve o resultado (grade de consulta ou linhas afetadas).

Comandos que o Parser entende mas que ainda **não têm tradução** (como `USAR BANCO DE DADOS`) são reconhecidos, porém geram erro na etapa de tradução.

---

## Comandos de definição de dados (DDL)

### 1. CRIAR TABELA

**Sintaxe**

```
CRIAR TABELA <nome_tabela> (
    <nome_coluna> <tipo_dado> [CHAVE PRIMARIA] [NAO NULO],
    <nome_coluna> <tipo_dado> [CHAVE PRIMARIA] [NAO NULO],
    ...
)
```

**Quando usar**

Quando você quer criar uma nova tabela — ou seja, definir a estrutura de armazenamento de um conjunto de dados (colunas, tipos e restrições).

**Explicação**

- `CRIAR TABELA` inicia o comando.
- Os parênteses abrigam a lista de colunas, separadas por vírgula.
- Cada coluna tem um **nome**, um **tipo de dado** e, opcionalmente, as restrições `CHAVE PRIMARIA` e `NAO NULO` (em qualquer ordem).
- Não é possível criar a tabela sem nenhuma coluna.

**Exemplo**

```text
PortuSQL: CRIAR TABELA alunos ( id inteiro CHAVE PRIMARIA, nome texto, idade inteiro )
SQL H2  : Create Table alunos (
              id int Primary Key,
              nome text,
              idade int
          );
```

---

### 2. ALTERAR TABELA — ADICIONAR COLUNA

**Sintaxe**

```
ALTERAR TABELA <nome_tabela> ADICIONAR COLUNA <nome_coluna> <tipo_dado> [CHAVE PRIMARIA] [NAO NULO]
```

**Quando usar**

Quando você precisa incluir uma coluna nova em uma tabela que já existe.

**Explicação**

- `ALTERAR TABELA <tabela>` aponta para a tabela que será modificada.
- `ADICIONAR COLUNA` seguido da definição da coluna nova (mesma forma usada no `CRIAR TABELA`).

**Exemplo**

```text
PortuSQL: ALTERAR TABELA alunos ADICIONAR COLUNA email texto
SQL H2  : Alter Table alunos Add Column email text;
```

---

### 3. ALTERAR TABELA — ADICIONAR RESTRICAO

**Sintaxe**

```
ALTERAR TABELA <nome_tabela> ADICIONAR RESTRICAO <nome_restricao> <coluna> <tipo_restricao>
ALTERAR TABELA <nome_tabela> ADICIONAR RESTRICAO <nome_restricao> <coluna> CHAVE ESTRANGEIRA REFERENCIA <tabela_referenciada> ( <coluna_referenciada> )
```

**Quando usar**

Quando você quer adicionar uma restrição **nomeada** a uma coluna de uma tabela que já existe, sem refazer a tabela.

**Explicação**

- `ADICIONAR RESTRICAO <nome>` dá um nome à restrição (ex.: `pk_id`, `fk_aluno`).
- Em seguida vêm a **coluna** afetada e o **tipo de restrição**: `CHAVE PRIMARIA`, `CHAVE ESTRANGEIRA` ou `UNICA`.
- Para chave estrangeira, `REFERENCIA <tabela> ( <coluna> )` indica de onde vem a referência.
- Restrições aceitas: `chave primaria`, `chave estrangeira`, `unica`.

**Exemplo**

```text
PortuSQL: ALTERAR TABELA alunos ADICIONAR RESTRICAO pk_id id chave primaria
SQL H2  : Alter Table alunos Add Constraint pk_id primary key (id);
```

```text
PortuSQL: ALTERAR TABELA alunos ADICIONAR RESTRICAO fk_turma turma_id chave estrangeira REFERENCIA turmas ( id )
SQL H2  : Alter Table alunos Add Constraint fk_turma foreign key (turma_id) References turmas(id);
```

---

### 4. ALTERAR TABELA — ADICIONAR RESTRICAO ... VERIFICAR

**Sintaxe**

```
ALTERAR TABELA <nome_tabela> ADICIONAR RESTRICAO <nome_restricao> VERIFICAR <condicao>
```

**Quando usar**

Quando você quer uma restrição de **verificação** (CHECK), que valida uma condição sobre as linhas (ex.: idade sempre positiva).

**Explicação**

- Diferente das outras restrições, o `VERIFICAR` não se aplica a uma coluna específica, e sim a uma **condição/expressão** (ex.: `idade > 0`).
- O PortuSQL traduz essa condição para a restrição `CHECK` do SQL.

**Exemplo**

```text
PortuSQL: ALTERAR TABELA alunos ADICIONAR RESTRICAO ck_idade VERIFICAR idade > 0
SQL H2  : Alter Table alunos Add Constraint ck_idade Check (idade > 0);
```

---

### 5. ALTERAR TABELA — REMOVER COLUNA

**Sintaxe**

```
ALTERAR TABELA <nome_tabela> REMOVER COLUNA <nome_coluna>
```

**Quando usar**

Quando você quer apagar uma coluna (e todos os dados dela) de uma tabela existente.

**Exemplo**

```text
PortuSQL: ALTERAR TABELA alunos REMOVER COLUNA email
SQL H2  : Alter Table alunos Drop Column email;
```

---

### 6. ALTERAR TABELA — REMOVER RESTRICAO

**Sintaxe**

```
ALTERAR TABELA <nome_tabela> REMOVER RESTRICAO <nome_restricao>
```

**Quando usar**

Quando você quer tirar uma restrição nomeada (chave, unicidade, etc.) de uma tabela.

**Exemplo**

```text
PortuSQL: ALTERAR TABELA alunos REMOVER RESTRICAO fk_turma
SQL H2  : Alter Table alunos Drop Constraint fk_turma;
```

---

### 7. ALTERAR TABELA — ALTERAR COLUNA

**Sintaxe**

```
ALTERAR TABELA <nome_tabela> ALTERAR COLUNA <nome_coluna> <tipo_dado>
```

**Quando usar**

Quando você quer mudar o **tipo de dado** de uma coluna existente (ex.: `inteiro` → `decimal`).

**Exemplo**

```text
PortuSQL: ALTERAR TABELA alunos ALTERAR COLUNA idade decimal
SQL H2  : Alter Table alunos Alter Column idade decimal;
```

---

### 8. ALTERAR TABELA — RENOMEAR COLUNA

**Sintaxe**

```
ALTERAR TABELA <nome_tabela> RENOMEAR COLUNA <nome_atual> PARA <nome_novo>
```

**Quando usar**

Quando você quer dar um novo nome a uma coluna existente (ex.: `nome` → `nome_completo`).

**Exemplo**

```text
PortuSQL: ALTERAR TABELA alunos RENOMEAR COLUNA nome PARA nome_completo
SQL H2  : Alter table alunos Rename Column nome To nome_completo;
```

---

### 9. ALTERAR TABELA — RENOMEAR RESTRICAO

**Sintaxe**

```
ALTERAR TABELA <nome_tabela> RENOMEAR RESTRICAO <nome_atual> PARA <nome_novo>
```

**Quando usar**

Quando você quer renomear uma restrição nomeada.

**Exemplo**

```text
PortuSQL: ALTERAR TABELA alunos RENOMEAR RESTRICAO fk_turma PARA fk_turma_aluno
SQL H2  : Alter Table alunos Rename Constraint fk_turma To fk_turma_aluno;
```

---

### 10. ALTERAR TABELA — RENOMEAR PARA (tabela)

**Sintaxe**

```
ALTERAR TABELA <nome_tabela> RENOMEAR PARA <nome_novo>
```

**Quando usar**

Quando você quer renomear a própria tabela (ex.: `alunos` → `estudantes`).

**Explicação**

- Aqui não se escreve "TABELA" depois de `RENOMEAR`: a forma é `RENOMEAR PARA <novo_nome>`.

**Exemplo**

```text
PortuSQL: ALTERAR TABELA alunos RENOMEAR PARA estudantes
SQL H2  : Alter Table alunos Rename To estudantes;
```

---

### 11. ESVAZIAR TABELA

**Sintaxe**

```
ESVAZIAR TABELA <nome_tabela>
```

**Quando usar**

Quando você quer **apagar todas as linhas** da tabela, mas **manter a estrutura** (colunas) para uso futuro.

**Explicação**

- Equivale ao `TRUNCATE TABLE` do SQL: remove todos os registros de uma vez, sem filtro.

**Exemplo**

```text
PortuSQL: ESVAZIAR TABELA alunos
SQL H2  : Truncate Table alunos;
```

---

### 12. EXCLUIR TABELA

**Sintaxe**

```
EXCLUIR TABELA <nome_tabela>
```

**Quando usar**

Quando você quer **remover a tabela inteira** — estrutura e dados — do banco.

**Explicação**

- Equivale ao `DROP TABLE` do SQL. Depois de excluída, consultar a tabela gera erro.

**Exemplo**

```text
PortuSQL: EXCLUIR TABELA alunos
SQL H2  : Drop Table alunos;
```

---

## Comandos de manipulação de dados (DML)

### 13. INSERIR

**Sintaxe**

```
INSERIR EM <nome_tabela> VALORES ( <valor>, <valor>, ... )
INSERIR ( <coluna>, <coluna>, ... ) EM <nome_tabela> VALORES ( <valor>, <valor>, ... )
INSERIR ( <coluna>, <coluna>, ... ) EM <nome_tabela> VALORES ( <valor>, ... ), ( <valor>, ... )
```

**Quando usar**

Quando você quer **adicionar uma linha nova** com dados em uma tabela.

**Explicação**

- Sem lista de colunas: os valores devem seguir a ordem das colunas da tabela.
- Com lista de colunas: você escolhe quais colunas receberão os valores (as outras ficam com valor padrão/NULL).
- Valores aceitos: **números** (`20`, `3.14`) e **textos entre aspas simples** (`'João'`).
- Várias linhas podem ser inseridas de uma vez: separe as tuplas por **vírgula**.

**Exemplo**

```text
PortuSQL: INSERIR (nome, idade) EM alunos VALORES ('Joao', 20)
SQL H2  : Insert Into alunos (nome, idade) Values ('Joao', 20);
```

```text
PortuSQL: INSERIR EM alunos VALORES (1, 'Joao', 20)
SQL H2  : Insert Into alunos Values (1, 'Joao', 20);
```

```text
PortuSQL: INSERIR (id, nome) EM alunos VALORES (2, 'Maria'), (3, 'Ana')
SQL H2  : Insert Into alunos (id, nome) Values (2, 'Maria'), (3, 'Ana');
```

> ⚠️ **Atenção:** a sintaxe **não** inclui um `INSERIR VALORES` sem o `EM` — a ordem obrigatória é `... EM <tabela> VALORES (...)`. O `VALORES` isolado de `EM` não é aceito pelo Parser.

---

### 14. ATUALIZAR

**Sintaxe**

```
ATUALIZAR <nome_tabela> DEFINIR <coluna> = <valor> [, <coluna> = <valor> ...] [ONDE <condicao>]
```

**Quando usar**

Quando você quer **modificar valores de linhas** que já existem na tabela.

**Explicação**

- `DEFINIR` lista as atribuições `coluna = valor` (uma ou várias, separadas por vírgula).
- `ONDE <condicao>` é **opcional**: com condição, só as linhas que a satisfazem mudam; sem condição, **todas as linhas** são atualizadas.

**Exemplo**

```text
PortuSQL: atualizar alunos DEFINIR idade = 21 ONDE nome PARECIDO COM 'Joao'
SQL H2  : Update alunos Set idade = 21 where nome LIKE 'Joao';
```

```text
PortuSQL: atualizar alunos DEFINIR idade = 10
SQL H2  : Update alunos Set idade = 10;
```

---

### 15. DELETAR

**Sintaxe**

```
DELETAR DE <nome_tabela> [ONDE <condicao>]
```

**Quando usar**

Quando você quer **remover linhas** de uma tabela, mantendo a estrutura.

**Explicação**

- `DELETAR DE <tabela>` apaga as linhas da tabela.
- `ONDE <condicao>` é opcional: com condição, só as linhas que a satisfazem são apagadas; sem condição, **todas** as linhas são apagadas.

**Exemplo**

```text
PortuSQL: DELETAR DE alunos ONDE idade > 18
SQL H2  : Delete From alunos where idade > 18;
```

---

## Comandos de consulta (DQL)

### 16. SELECIONAR

**Sintaxe**

```
SELECIONAR [DISTINCT] <coluna1, coluna2, ... | *> DE <nome_tabela>
         [ONDE <condicao>]
         [AGRUPAR POR <coluna1, coluna2, ...>]
         [ORDENAR POR <coluna1, coluna2, ...>]
```

**Quando usar**

Quando você quer **consultar/ler dados** do banco — é o comando mais usado do dia a dia.

**Explicação**

- A lista de colunas pode usar `*` (todas as colunas) ou nomes separados por vírgula.
- `DISTINCT` remove linhas repetidas do resultado.
- Também aceita **funções agregadas**, traduzidas para o equivalente do H2:

  | PortuSQL | SQL (H2) | O que calcula |
  |---|---|---|
  | `CONTAR(...)` | `COUNT(...)` | quantidade de linhas ou de valores |
  | `SOMA(...)` | `SUM(...)` | soma dos valores |
  | `MED(...)` | `AVG(...)` | média aritmética |
  | `MIN(...)` | `MIN(...)` | menor valor |
  | `MAX(...)` | `MAX(...)` | maior valor |

  Dentro delas o `DISTINCT` também vale: `CONTAR(Distinct cidade)`.
- `ONDE <condicao>` filtra as linhas.
- `AGRUPAR POR` agrupa o resultado por coluna(s).
- `ORDENAR POR` ordena o resultado por coluna(s).
- Todas as cláusulas depois do nome da tabela são opcionais e nessa ordem.

**Exemplo**

```text
PortuSQL: SELECIONAR * DE alunos
SQL H2  : Select * from alunos;
```

```text
PortuSQL: SELECIONAR nome, idade DE alunos ONDE idade > 18 E status PARECIDO COM 'ativo'
SQL H2  : Select nome, idade from alunos where idade > 18 AND status LIKE 'ativo';
```

```text
PortuSQL: SELECIONAR cidade DE alunos AGRUPAR POR cidade ORDENAR POR cidade
SQL H2  : Select cidade from alunos group by cidade order by cidade;
```

```text
PortuSQL: SELECIONAR turma_id, CONTAR(*) DE alunos AGRUPAR POR turma_id
SQL H2  : Select turma_id, COUNT(*) from alunos group by turma_id;
```

```text
PortuSQL: SELECIONAR DISTINCT cidade DE alunos
SQL H2  : Select Distinct cidade from alunos;
```

---

### 17. MOSTRAR TABELA

**Sintaxe**

```
MOSTRAR TABELA <nome_tabela>
```

**Quando usar**

Quando você quer **ver a estrutura de uma tabela** (suas colunas e seus metadados).

**Explicação**

- Equivale ao `SHOW COLUMNS FROM` do H2; o resultado vem como uma consulta.

**Exemplo**

```text
PortuSQL: MOSTRAR TABELA alunos
SQL H2  : Show Columns From alunos;
```

---

### 18. MOSTRAR COLUNA

**Sintaxe**

```
MOSTRAR COLUNA <nome_coluna> DE <nome_tabela>
```

**Quando usar**

Quando você quer **ver os detalhes de uma coluna específica** de uma tabela.

**Explicação**

- Consulta o `INFORMATION_SCHEMA.COLUMNS` do banco filtrando a tabela e a coluna.

**Exemplo**

```text
PortuSQL: MOSTRAR COLUNA idade DE alunos
SQL H2  : Select * From Information_Schema.Columns Where Table_Name = 'ALUNOS' And Column_Name = 'IDADE';
```

---

## Comandos de banco de dados

### 19. USAR BANCO DE DADOS

**Sintaxe**

```
USAR BANCO DE DADOS <nome_banco>
```

**Quando usar**

Quando você quer definir **qual banco de dados** será usado pelo PortuSQL.

**Explicação**

- O PortuSQL reconhece e monta o comando, mas **ainda não gera SQL** para ele — a execução falha na etapa de tradução (`Comando não encontrado: UsarBancoDados`).

**Exemplo**

```text
PortuSQL: USAR BANCO DE DADOS loja
SQL H2  : (comando reconhecido, porém ainda sem tradução — erro na etapa Tradutor)
```

---

### 20. EXCLUIR BANCO DE DADOS

**Sintaxe**

```
EXCLUIR BANCO DE DADOS <nome_banco>
```

**Quando usar**

Quando você quer apagar um banco de dados inteiro.

**Explicação**

- O PortuSQL reconhece a sintaxe, porém o tradutor **lança erro de propósito**: `Esse comando não pode ser executado`. A exclusão de banco de dados não é permitida pelo tradutor atual.

**Exemplo**

```text
PortuSQL: EXCLUIR BANCO DE DADOS escola
SQL H2  : (comando bloqueado pelo tradutor — erro: "Esse comando não pode ser executado")
```

---

## Tipos de dados

Tipos reconhecidos pelo analisador (podem ser usados no `CRIAR TABELA` e em colunas):

| PortuSQL | SQL (H2) | Descrição |
|---|---|---|
| `int` / `inteiro` | `int` | número inteiro |
| `texto` | `text` | texto (cadeia de caracteres) |
| `data` | `date` | data |
| `decimal` | `decimal` | número decimal (precisão definida pelo banco) |
| `hora` | `time` | hora |
| `data_hora` | `timestamp` | data e hora juntas |
| `CARACTERE VARIAVEL(n)` | `varchar(n)` | texto com limite de tamanho |
| `CARACTERE FIXO(n)` | `char(n)` | texto de tamanho fixo |
| `gint`, `cint`, `mint` | — | reconhecidos como tipo, mas **sem tradução** no H2 (erro na tradução) |

Outros nomes (`flutuante`, `booleano`, `binario`, `enum`, `real`, `numerico`) existem no dicionário do Tradutor, mas o Lexer ainda não os reconhece como tipo de dado — na prática eles são tratados como identificadores.

> ⚠️ **Limitação conhecida:** tipos que não estão na tabela acima (nem no Lexer, nem no mapeamento do H2) geram erro de tradução `Tipo de dado não encontrado`.

---

## Restrições

Restrições que podem acompanhar colunas ou serem adicionadas depois:

| PortuSQL | SQL (H2) | Onde se aplica |
|---|---|---|
| `CHAVE PRIMARIA` | `Primary Key` | na definição da coluna ou via `ADICIONAR RESTRICÃO` |
| `NAO NULO` | `Not Null` | apenas na definição da coluna |
| `CHAVE ESTRANGEIRA` | `Foreign Key` | apenas via `ADICIONAR RESTRICÃO` |
| `UNICA` | `Unique` | apenas via `ADICIONAR RESTRICÃO` |
| `VERIFICAR` (CHECK) | `Check` | via `ADICIONAR RESTRICÃO` |

---

## Cláusulas e operadores

**Filtro `ONDE`** (opcional em `SELECIONAR`, `ATUALIZAR` e `DELETAR`) aceita:

| PortuSQL | SQL |
|---|---|
| `E` | `AND` |
| `OU` | `OR` |
| `NAO` | `NOT` |
| `PARECIDO COM` | `LIKE` |
| `NAO NULO` | `IS NOT NULL` |

**Operadores de comparação/atribuição:** `=`, `>`, `<`, `>=`, `<=`, `<>` (diferente).

**Operadores aritméticos:** `+`, `-`, `*`, `/`.

**Concatenação de texto:** `||` (a barra isolada `|` é um erro léxico).

**Valores:** números inteiros (`20`) ou decimais (`3.14`) e textos entre aspas simples (`'João'`). A palavra `nulo` é reconhecida como valor nulo.

---

## Limitações conhecidas

Resumo do que hoje **ainda não funciona** (documentado no código-fonte e nos testes):

1. **`USAR BANCO DE DADOS`:** o comando é reconhecido, mas **não gera SQL** — a troca de banco acontece pela interface (navegador de bancos), pois o H2 não possui comando `USE` via SQL.
2. **`EXCLUIR BANCO DE DADOS`:** o comando é reconhecido, porém o tradutor **bloqueia de propósito** (`Esse comando não pode ser executado`). A exclusão de banco é feita pela interface gráfica.
3. **Tipos `gint`, `cint`, `mint`:** reconhecidos pelo Lexer, mas sem tipo correspondente no H2 (geram erro de tradução).
4. **Tipos extras do Tradutor (`flutuante`, `booleano`, `binario`, `enum`, `real`, `numerico`):** mapeados para SQL, mas o Lexer ainda não os reconhece como tipo de dado — na prática viram identificadores.

Esses pontos (principalmente o item 4) são ótimos candidatos para a próxima rodada de melhorias no motor (`Engine`).