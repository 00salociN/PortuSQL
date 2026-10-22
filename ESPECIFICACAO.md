# Especificação da Linguagem PortuSQL

**Versão:** 1.0 (Draft)
**Data:** 2026-09-20
**Autor:** Nicolas Gabriel Venâncio dos Santos

---

## 1. Escopo

Esta especificação define a sintaxe e a semântica da linguagem **PortuSQL**, um
dialeto de SQL em português destinado ao ensino de banco de dados. Um programa
PortuSQL é um **comando** — uma única sentença que descreve uma operação sobre um
banco de dados relacional.

O objetivo desta especificação é descrever, de forma precisa e formal, o
comportamento do implemento de referência (o motor `Lexer → Parser → Tradutor →
Executor`), permitindo que implementações alternativas sejam construídas e
verificadas.

Esta especificação **não** descreve a interface gráfica (IDE), a configuração de
banco de dados nem os detalhes de conexão. Ela trata exclusivamente da
linguagem.

---

## 2. Conformidade

Um **programa conforme** é aquele produzido pela gramática desta especificação e
interpretado de acordo com as regras semânticas aqui definidas.

Um **implemento conforme** deve:

1. Suportar a totalidade da gramática definida na Seção 7.
2. Realizar a tradução dos comandos conforme a Seção 9.
3. Sinalizar os erros definidos na Seção 10, com posição (linha e coluna).

Um implemento conforme pode:

- Estender a linguagem com palavras-chave e comandos adicionais, desde que não
  altere o comportamento dos construtos definidos nesta especificação.
- Fornecer mecanismos de extensão para expressões não cobertas nesta
  especificação.

Um implemento **não** pode:

- Silenciosamente ignorar um comando sintaticamente válido e semanticamente
  definido nesta especificação, sem sinalizar o erro correspondente.

---

## 3. Referências normativas

As seguintes referências são normativas para esta especificação:

| Referência | Descrição |
|---|---|
| **ISO/IEC 9075 (SQL)** | Padrão da linguagem SQL, base da semântica dos comandos. |
| **H2 Database** | Sistema gerenciador de banco de dados alvo da tradução (versão 2.x). |
| **Unicode** | Conjunto de caracteres usado na representação de identificadores e valores de texto. |

---

## 4. Visão geral

### 4.1 Organização do documento

Esta especificação está organizada da seguinte forma:

- **Seção 5** define a notação utilizada.
- **Seção 6** define os elementos léxicos (tokens) da linguagem.
- **Seção 7** define a gramática dos comandos.
- **Seção 8** define as classes de objetos (`Comando`) resultantes da análise.
- **Seção 9** define a semântica — a tradução de cada comando para SQL (H2).
- **Seção 10** define os tipos de dado.
- **Seção 11** define as restrições.
- **Seção 12** define as cláusulas de consulta.
- **Seção 13** define o tratamento de erros.
- **Anexo A** lista as palavras reservadas.
- **Anexo B** traz exemplos informativos.

### 4.2 Modelo de processamento

Um programa PortuSQL é processado em **quatro etapas**:

```
Lexer → Parser → Tradutor → Executor
```

- **Lexer** — converte a sequência de caracteres em uma sequência de tokens
  (Seção 6).
- **Parser** — consome os tokens e produz uma **árvore sintática** (um objeto da
  classe `Comando`) (Seções 7 e 8).
- **Tradutor** — transforma o `Comando` em uma string SQL válida para o H2
  (Seção 9).
- **Executor** — submete o SQL ao banco e devolve um resultado (grade de
  consulta ou contagem de linhas afetadas).

---

## 5. Notação

### 5.1 Notação da gramática

A gramática é apresentada em forma BNF, com as seguintes convenções:

- Terminais são escritos em **MAIÚSCULAS** (`CRIAR`, `TABELA`, `,`, `(`).
- Não terminais são escritos em itálico (*NomeDoComando*).
- `::=` separa o não terminal de sua definição.
- `|` separa alternativas.
- `A B` significa **A seguido de B**.
- `[A]` significa que **A é opcional**.
- `A*` significa zero ou mais repetições de A.
- `A+` significa uma ou mais repetições de A.
- `A , B*` significa uma lista de elementos A separados por vírgula.

### 5.2 Termos

- **Comando** — uma sentença PortuSQL completa, delimitada pelo fim da entrada.
- **Token** — a menor unidade lexical com significado (Seção 6).
- **Identificador** — nome de tabela, coluna, banco ou restrição.
- **Valor** — um literal numérico ou textual.
- **Condição** — expressão booleana usada na cláusula `ONDE`.

---

## 6. Elementos léxicos

### 6.1 Formato dos caracteres

Um programa PortuSQL é uma sequência de caracteres. Os caracteres de espaço
(`U+0020`), tabulação (`U+0009`) e retorno de carro (`U+000D`) são
**espaços em branco** e são ignorados fora de valores de texto. Quebras de linha
(`U+000A`) finalizam a linha corrente — são usadas apenas para a contagem de
linha/coluna em relatórios de erro.

### 6.2 Comentários

A linguagem **não define** comentários no estágio atual.

### 6.3 Tokens

A linguagem define os seguintes tipos de token:

| Token | Descrição |
|---|---|
| `palavra_chave` | palavra reservada da linguagem (Anexo A) |
| `tipo_dado` | nome de um tipo de dado (Seção 10) |
| `identificador` | nome definido pelo usuário |
| `numerico` | literal numérico |
| `texto` | literal textual entre aspas simples |
| `nulo` | a palavra `NULO` |
| `virgula` | `,` |
| `parenteses_esquerda` | `(` |
| `parenteses_direita` | `)` |
| `ponto_virgula` | `;` |
| `ponto` | `.` |
| `operador_simples` | `=`, `+`, `-`, `*`, `/`, `>`, `<`, `>=`, `<=`, `<>`, `\|\|` |
| `fim` | marcador de fim da entrada |

### 6.4 Identificadores

*Identificador* ::= *(letra | `_`)* *(letra | dígito | `_`)*

Identificadores são comparados **sem distinção de maiúsculas** apenas para fins
de reconhecimento de palavras reservadas; o valor do identificador é preservado
conforme digitado.

### 6.5 Literais numéricos

*Número* ::= *dígito*+ (`.` *dígito*+)?

É um **Erro Léxico** se um literal numérico contiver mais de um ponto decimal,
ou terminar com um ponto sem dígitos após ele.

### 6.6 Literais de texto

*Texto* ::= `'` *caracteres* `'`

Um literal de texto é delimitado por aspas simples (`U+0027`). Pode conter
quebras de linha. É um **Erro Léxico** se o literal não for fechado com aspas
simples antes do fim da entrada.

### 6.7 Operadores

| Sequência | Significado |
|---|---|
| `=` | igualdade / atribuição |
| `>` | maior que |
| `<` | menor que |
| `>=` | maior ou igual |
| `<=` | menor ou igual |
| `<>` | diferente |
| `+`, `-`, `*`, `/` | aritmética |
| `\|\|` | concatenação de texto |

É um **Erro Léxico** se uma barra vertical isolada (`|`) for encontrada fora do
operador `||`.

### 6.8 Caracteres não reconhecidos

É um **Erro Léxico** se qualquer caractere fora dos conjuntos acima for
encontrado fora de um literal de texto.

---

## 7. Gramática dos comandos

*Comando* ::= *CriarTabela*
            | *AlterarTabela*
            | *ExcluirTabela*
            | *EsvaziarTabela*
            | *Inserir*
            | *Atualizar*
            | *Deletar*
            | *Selecionar*
            | *MostrarTabela*
            | *MostrarColuna*
            | *UsarBancoDados*
            | *ExcluirBancoDados*

### 7.1 CRIAR TABELA

*CriarTabela* ::= `CRIAR` `TABELA` *Identificador* `(` *DefinicaoColuna*
                  (`,` *DefinicaoColuna*)* `)`

*DefinicaoColuna* ::= *Identificador* *TipoDado* (*RestricaoColuna*)*

*RestricaoColuna* ::= `CHAVE` `PRIMARIA` | `NAO` `NULO`

### 7.2 ALTERAR TABELA

*AlterarTabela* ::= `ALTERAR` `TABELA` *Identificador* *AcaoAlteracao*

*AcaoAlteracao* ::= *AdicionarColuna*
                  | *AdicionarRestricao*
                  | *RemoverColuna*
                  | *RemoverRestricao*
                  | *AlterarColuna*
                  | *RenomearColuna*
                  | *RenomearRestricao*
                  | *RenomearTabela*

*AdicionarColuna* ::= `ADICIONAR` `COLUNA` *DefinicaoColuna*

*AdicionarRestricao* ::= `ADICIONAR` `RESTRICAO` *Identificador*
                        (*Verificar* | *RestricaoTabela*)

*Verificar* ::= `VERIFICAR` *Condicao*

*RestricaoTabela* ::= *Identificador* *TipoRestricao*
                      [ `REFERENCIA` *Identificador* `(` *Identificador* `)` ]

*RemoverColuna* ::= `REMOVER` `COLUNA` *Identificador*

*RemoverRestricao* ::= `REMOVER` `RESTRICAO` *Identificador*

*AlterarColuna* ::= `ALTERAR` `COLUNA` *Identificador* *TipoDado*

*RenomearColuna* ::= `RENOMEAR` `COLUNA` *Identificador* `PARA` *Identificador*

*RenomearRestricao* ::= `RENOMEAR` `RESTRICAO` *Identificador* `PARA` *Identificador*

*RenomearTabela* ::= `RENOMEAR` `PARA` *Identificador*

### 7.3 EXCLUIR TABELA

*ExcluirTabela* ::= `EXCLUIR` `TABELA` *Identificador*

### 7.4 ESVAZIAR TABELA

*EsvaziarTabela* ::= `ESVAZIAR` `TABELA` *Identificador*

### 7.5 INSERIR

*Inserir* ::= `INSERIR` [`(` *ListaIdentificadores* `)`] `EM` *Identificador*
              `VALORES` *TuplaValores* (`,` *TuplaValores*)*

*ListaIdentificadores* ::= *Identificador* (`,` *Identificador*)*

*TuplaValores* ::= `(` *ListaValores* `)`

*ListaValores* ::= *Valor* (`,` *Valor*)*

### 7.6 ATUALIZAR

*Atualizar* ::= `ATUALIZAR` *Identificador* `DEFINIR` *ListaAtribuicoes*
                [ `ONDE` *Condicao* ]

*ListaAtribuicoes* ::= *Atribuicao* (`,` *Atribuicao*)*

*Atribuicao* ::= *Identificador* `=` *Valor*

### 7.7 DELETAR

*Deletar* ::= `DELETAR` `DE` *Identificador* [ `ONDE` *Condicao* ]

### 7.8 SELECIONAR

*Selecionar* ::= `SELECIONAR` [ `DISTINCT` ] *ListaColunas* `DE` *Identificador*
                [ `ONDE` *Condicao* ]
                [ `AGRUPAR` `POR` *ListaColunas* ]
                [ `ORDENAR` `POR` *ListaColunas* ]

*ListaColunas* ::= (*Asterisco* | *Identificador* | *FuncaoAgregada*)
                   (`,` (*Asterisco* | *Identificador* | *FuncaoAgregada*))*

*Asterisco* ::= `*`

*FuncaoAgregada* ::= (`CONTAR` | `SOMA` | `MED` | `MIN` | `MAX`)
                    [ `DISTINCT` ] `(` (*Asterisco* | *Identificador*) `)`

### 7.9 MOSTRAR TABELA

*MostrarTabela* ::= `MOSTRAR` `TABELA` *Identificador*

### 7.10 MOSTRAR COLUNA

*MostrarColuna* ::= `MOSTRAR` `COLUNA` *Identificador* `DE` *Identificador*

### 7.11 USAR BANCO DE DADOS

*UsarBancoDados* ::= `USAR` `BANCO` `DE` `DADOS` *Identificador*

### 7.12 EXCLUIR BANCO DE DADOS

*ExcluirBancoDados* ::= `EXCLUIR` `BANCO` `DE` `DADOS` *Identificador*

### 7.13 Palavras clausais

*Condicao* ::= (qualquer sequência de tokens que não contenha a palavra
reservada inicial do próximo comando)

*Valor* ::= *Número* | *Texto* | `NULO`

---

## 8. Objetos Comando

O resultado da análise sintática é um **objeto comando**, uma representação
estrutural imutável do comando. Cada produção da Seção 7 corresponde a uma
classe de objeto:

| Produção | Classe |
|---|---|
| *CriarTabela* | `CriarTabela` |
| *AdicionarColuna* | `AdicionarColuna` |
| *AdicionarRestricao* | `AdicionarRestricao` ou `AdicionarRestricaoVerificar` |
| *RemoverColuna* | `RemoverColuna` |
| *RemoverRestricao* | `RemoverRestricao` |
| *AlterarColuna* | `AlterarColuna` |
| *RenomearColuna* | `RenomearColuna` |
| *RenomearRestricao* | `RenomearRestricao` |
| *RenomearTabela* | `RenomearTabela` |
| *EsvaziarTabela* | `EsvaziarDadosTabela` |
| *ExcluirTabela* | `ExcluirTabela` |
| *Inserir* | `Inserir` |
| *Atualizar* | `Atualizar` |
| *Deletar* | `Deletar` |
| *Selecionar* | `Selecionar` |
| *MostrarTabela* | `MostrarTabela` |
| *MostrarColuna* | `MostrarColuna` |
| *UsarBancoDados* | `UsarBancoDados` |
| *ExcluirBancoDados* | `ExcluirBancoDados` |

A estrutura de cada classe preserva os componentes sintáticos do comando, na
ordem em que aparecem (por exemplo, `Selecionar` mantém tabela, colunas,
condição, agrupamento e ordenação).

---

## 9. Semântica — tradução para SQL

A tradução de um *Comando* para SQL (H2) segue as regras desta seção.

### 9.1 CRIAR TABELA

```
CREATE TABLE <nome> (
    <coluna> <tipo> [PRIMARY KEY] [NOT NULL],
    ...
);
```

- Cada *DefinicaoColuna* é traduzida conforme a Seção 10 (tipos) e 11 (restrições).

### 9.2 ALTERAR TABELA — ADICIONAR COLUNA

```
ALTER TABLE <nome> ADD COLUMN <coluna> <tipo> [PRIMARY KEY] [NOT NULL];
```

### 9.3 ALTERAR TABELA — ADICIONAR RESTRICÃO

```
ALTER TABLE <nome> ADD CONSTRAINT <nome_restricao> <tipo> (<coluna>) [REFERENCES <tabela>(<coluna>)];
```

- *TipoRestricao* é traduzido conforme a Seção 11.

### 9.4 ALTERAR TABELA — ADICIONAR RESTRICÃO VERIFICAR

```
ALTER TABLE <tabela> ADD CONSTRAINT <nome_restricao> CHECK (<condição>);
```

- A condição (`VERIFICAR`) é traduzida conforme a Seção 12.2.

### 9.5 ALTERAR TABELA — REMOVER COLUNA

```
ALTER TABLE <nome> DROP COLUMN <coluna>;
```

### 9.6 ALTERAR TABELA — REMOVER RESTRICÃO

```
ALTER TABLE <nome> DROP CONSTRAINT <nome_restricao>;
```

### 9.7 ALTERAR TABELA — ALTERAR COLUNA

```
ALTER TABLE <nome> ALTER COLUMN <coluna> <tipo>;
```

### 9.8 ALTERAR TABELA — RENOMEAR COLUNA

```
ALTER TABLE <nome> RENAME COLUMN <antigo> TO <novo>;
```

### 9.9 ALTERAR TABELA — RENOMEAR TABELA

```
ALTER TABLE <antigo> RENAME TO <novo>;
```

### 9.10 ALTERAR TABELA — RENOMEAR RESTRICÃO

```
ALTER TABLE <tabela> RENAME CONSTRAINT <antigo> TO <novo>;
```

### 9.11 EXCLUIR TABELA

```
DROP TABLE <nome>;
```

### 9.12 ESVAZIAR TABELA

```
TRUNCATE TABLE <nome>;
```

### 9.13 INSERIR

```
INSERT INTO <tabela> [(<coluna>, ...)] VALUES (<valor>, ...);
```

- Valores de texto são gerados entre aspas simples.
- Várias linhas podem ser inseridas de uma vez, separando as tuplas por
  vírgula: `VALUES (<valor>, ...), (<valor>, ...);`.

### 9.14 ATUALIZAR

```
UPDATE <tabela> SET <coluna> = <valor> [, <coluna> = <valor> ...] [WHERE <condição>];
```

- *Condição* é traduzida conforme a Seção 12.2.
- Valores de texto são gerados entre aspas simples.

### 9.15 DELETAR

```
DELETE FROM <tabela> [WHERE <condição>];
```

### 9.16 SELECIONAR

```
SELECT <colunas> FROM <tabela> [WHERE <condição>] [GROUP BY <colunas>] [ORDER BY <colunas>];
```

### 9.17 MOSTRAR TABELA

```
SHOW COLUMNS FROM <tabela>;
```

### 9.18 MOSTRAR COLUNA

```
SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = '<tabela>' AND COLUMN_NAME = '<coluna>';
```

### 9.19 USAR BANCO DE DADOS

O comando é reconhecido e produz o objeto `UsarBancoDados`, porém **não possui
tradução definida** (ver Seção 13.4).

### 9.20 EXCLUIR BANCO DE DADOS

O comando é reconhecido, porém a tradução lança deliberadamente um erro de
comando não executável (ver Seção 13.4). A exclusão de banco não é suportada
pelo tradutor.

---

## 10. Tipos de dado

### 10.1 Reconhecimento

```
TipoDado ::= "int" | "inteiro" | "texto" | "data" | "decimal"
           | "hora" | "data_hora" | "gint" | "cint" | "mint"
           | "caractere variavel" "(" numero ")"
           | "caractere fixo" "(" numero ")"
```

### 10.2 Mapeamento para H2

| TipoDado PortuSQL | SQL (H2) |
|---|---|
| `int`, `inteiro` | `int` |
| `texto` | `text` |
| `data` | `date` |
| `decimal` | `decimal` |
| `hora` | `time` |
| `data_hora` | `timestamp` |
| `caractere variavel(n)` | `varchar(n)` |
| `caractere fixo(n)` | `char(n)` |
| `gint`, `cint`, `mint` | *(sem mapeamento — erro de tradução)* |

É um **Erro de Tradução** se um `TipoDado` sem correspondência no H2 for usado.

---

## 11. Restrições

### 11.1 Na definição de coluna

```
RestricaoColuna ::= "CHAVE PRIMARIA" | "NAO NULO"
```

As duas restrições são comutativas e podem se repetir. Tradução:

| Restrição | SQL |
|---|---|
| `CHAVE PRIMARIA` | `PRIMARY KEY` |
| `NAO NULO` | `NOT NULL` |

### 11.2 Restrição nomeada (ADICIONAR RESTRICÃO)

```
TipoRestricao ::= "CHAVE PRIMARIA" | "CHAVE ESTRANGEIRA" | "UNICA"
```

| Tipo | SQL |
|---|---|
| `chave primaria` | `PRIMARY KEY` |
| `chave estrangeira` | `FOREIGN KEY` |
| `unica` | `UNIQUE` |

É um **Erro de Tradução** se uma definição de restrição desconhecida for usada.

---

## 12. Cláusulas

### 12.1 ONDE

A cláusula `ONDE` (*Condicao*) introduz uma expressão de filtro. A condição é
capturada como texto e traduzida por substituição lexical (Seção 12.2).

A cláusula é opcional em *Selecionar*, *Atualizar* e *Deletar*. Sua ausência
significa "sem filtro" — no caso de *Atualizar* e *Deletar*, *todas* as linhas
são afetadas.

### 12.2 Substituições na condição

Na tradução de uma *Condicao*, as seguintes substituições são aplicadas:

| PortuSQL | SQL |
|---|---|
| `E` | `AND` |
| `OU` | `OR` |
| `NAO` | `NOT` |
| `NAO NULO` | `IS NOT NULL` |
| `PARECIDO COM` | `LIKE` |

### 12.3 AGRUPAR POR e ORDENAR POR

Em *Selecionar*:

- `AGRUPAR POR <colunas>` — agrupamento do resultado; traduz para `GROUP BY`.
- `ORDENAR POR <colunas>` — ordenação do resultado; traduz para `ORDER BY`.

As cláusulas são opcionais e, quando presentes, devem aparecer na ordem
`AGRUPAR POR` antes de `ORDENAR POR`.

---

## 13. Tratamento de erros

### 13.1 Erro Léxico

Ocorre durante a etapa *Lexer* quando:

- um caractere não reconhecido é encontrado;
- um literal numérico é mal formado;
- um literal de texto não é fechado;
- uma barra vertical isolada é encontrada.

O erro reporta a posição (linha e coluna) e uma mensagem descritiva.

### 13.2 Erro Sintático

Ocorre durante a etapa *Parser* quando a sequência de tokens não satisfaz a
gramática da Seção 7, ou quando o primeiro token não inicia um comando
conhecido.

O erro reporta a posição (linha e coluna) e o token inesperado.

### 13.3 Erro de Tradução

Ocorre durante a etapa *Tradutor* quando:

- a classe do comando não possui função de tradução registrada;
- um tipo de dado não possui mapeamento para o H2;
- um tipo de restrição é desconhecido.

### 13.4 Comandos não traduzidos / bloqueados

| Comando | Comportamento |
|---|---|
| `USAR BANCO DE DADOS` | reconhecido; erro de tradução (comando não encontrado) |
| `EXCLUIR BANCO DE DADOS` | reconhecido; erro deliberado "Esse comando não pode ser executado" |

### 13.5 Erro de Execução

Ocorre durante a etapa *Executor* quando o SQL gerado é rejeitado pelo banco H2.
O erro é propagado ao chamador com a mensagem do banco.

---

## Anexo A (normativo) — Palavras reservadas

As seguintes palavras são **reservadas** e não podem ser usadas como
identificadores:

```
adicionar     agrupar       alterar       apagar        banco
chave         coluna        criar         dados         de
definir       deletar       em            e             esvaziar
estrangeira   excluir       inserir       mostrar       nao
nulo          onde          ordenar       ou            para
por           primaria      referencia    remover       renomear
restricao     selecionar    tabela        unica         usar
usar          valores       verificar     apagar
distinct
```

A palavra `NULO` é reconhecida como o valor nulo (token `nulo`).

## Anexo B (informativo) — Exemplos

| PortuSQL | SQL gerado (H2) |
|---|---|
| `criar tabela alunos (id int chave primaria, nome texto, idade int)` | `CREATE TABLE alunos (id int PRIMARY KEY, nome text, idade int);` |
| `inserir (nome, idade) em alunos valores ("João", 20)` | `INSERT INTO alunos (nome, idade) VALUES ('João', 20);` |
| `selecionar nome, idade de alunos onde idade > 18` | `SELECT nome, idade FROM alunos WHERE idade > 18;` |
| `atualizar alunos definir idade = 21 onde nome = "João"` | `UPDATE alunos SET idade = 21 WHERE nome = 'João';` |
| `deletar de alunos onde idade < 18` | `DELETE FROM alunos WHERE idade < 18;` |
| `esvaziar tabela alunos` | `TRUNCATE TABLE alunos;` |
| `excluir tabela alunos` | `DROP TABLE alunos;` |
| `mostrar tabela alunos` | `SHOW COLUMNS FROM alunos;` |