# Arquitetura

Este arquivo detalha como o PortuSQL executa um comando. Para uma visão curta, veja [Como funciona](../README.md#como-funciona).

## Fluxo de execução

```text
Comando escrito pelo usuário
          |
          v
        Lexer
          |
          v
        Parser
          |
          v
       Tradutor
          |
          v
       Executor
          |
          v
     Banco H2
          |
          v
       Resultado
```

| Etapa | Classe | Entrada → saída |
|---|---|---|
| Lexer | `Engine/Lexer` | texto → `List<Token>` |
| Parser | `Engine/Parser` | tokens → objeto `Comando` |
| Tradutor | `Engine/Tradutor` | `Comando` → `String` com SQL do H2 |
| Executor | `Conexao/Executor` | SQL → `Resultado` |

A orquestração fica em `PortuSQL/PortuSQL/Interpretador.java`, no método `executar(String)`. A IDE chama esse mesmo método e recebe um `ResultadoExecucao` com o resultado, o SQL gerado e o tempo gasto.

## Por que etapas separadas

Cada etapa trabalha com um tipo de dados diferente. O lexer percorre caracteres; o parser conhece a gramática; o tradutor transforma comandos em SQL; o executor só conversa com o JDBC. Quando uma mensagem aparece, dá para saber de qual etapa ela veio sem percorrer um arquivo único.

As etapas não são independentes de fato: o `Interpretador` instancia as quatro classes em sequência. A separação existe para manter cada responsabilidade em um lugar previsível, não para permitir trocar módulos em tempo de execução.

## Representação dos comandos

`Comando` é uma interface sem métodos. Cada instrução reconhecida tem uma classe em `Comandos/`, com os dados necessários para a tradução:

- `CriarTabela` guarda o nome da tabela e a lista de `DefinicaoColuna`;
- `Selecionar` guarda tabela, colunas ou agregadas, condição, agrupamento e ordenação;
- `Inserir` guarda tabela, nomes de colunas e tuplas de valores.

`DefinicaoColuna` e `FuncaoAgregada` são auxiliares usados dentro desses objetos. Nenhuma classe de `Comandos/` executa SQL nem conhece o H2. Essa estrutura é a árvore sintática do PortuSQL.

## Tokens e erros

`Token` guarda tipo, lexema, linha e coluna. `ErroLexer` e `ErroParser` também carregam linha e coluna, então falhas de leitura ou sintaxe apontam o lugar exato do problema:

```text
Esperava 'VALORES', mas encontrei '(' (linha 1, coluna 19)
```

Falhas de tradução e erros devolvidos pelo H2 são exibidos como mensagens da etapa que os originou. Hoje essas mensagens não carregam a posição do comando escrito; essa diferença está em [`../LIMITACOES.md`](../LIMITACOES.md).

## Comunicação com o H2

`Conexao/Conexao.java` mantém a conexão ativa e monta o caminho dos bancos em `~/.portusql`, usando o usuário padrão `sa` do H2. `Executor.executar(String)` usa um `Statement` JDBC e verifica `getResultSet()` ou `getUpdateCount()` para montar o `Resultado`.

Consultas viram colunas e linhas. Comandos de alteração viram contagem de linhas afetadas. A IDE usa `Formatador.consulta` para apresentar o retorno.

## Tradutor e banco de destino

O SQL é produzido em `Engine/Tradutor.java`. O método público `traducao(Comando)` despacha por classe de comando e chama um método privado de tradução.

A costura entre linguagem e banco existe: a árvore de comandos não sabe SQL, `Tradutor` devolve uma string e `Executor` aceita SQL JDBC. Na prática, porém, não há interface de tradutor nem injeção de dependência — o `Interpretador` instancia `new Tradutor()` diretamente. O tradutor atual também contém decisões específicas do H2, como nomes de tipos, `SHOW COLUMNS` e consultas a `information_schema`.

Portanto, o PortuSQL **não suporta múltiplos bancos hoje**. A arquitetura facilita pensar em outro destino, mas isso exigiria extrair uma interface, criar uma implementação nova e ajustar `Conexao` e `Executor`. Essa direção está em [`../ROADMAP.md`](../ROADMAP.md).

## DER

O painel lateral de DER lê metadados do banco em uso por `Conexao/MapaBanco.java`. A leitura usa `DatabaseMetaData` do H2 no esquema `PUBLIC` e obtém:

- tabelas e colunas, com o tipo de dado convertido de volta para o vocabulário do PortuSQL;
- marcação de chave primária por coluna;
- chaves estrangeiras, representadas como relacionamentos entre a coluna de origem e a coluna referenciada.

O painel não altera o banco. Depois de cada comando, `Conexao/DiffDer.java` compara o retrato anterior com o atual e destaca:

- tabelas novas ou removidas;
- colunas adicionadas ou removidas;
- mudanças de tipo, mostradas como `antigo -> novo`;
- chaves estrangeiras adicionadas ou removidas.

O desenho e a interação com o diagrama ficam em `Telas/PainelDer.java` e `Telas/DockDer.java`.
