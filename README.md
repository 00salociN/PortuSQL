# PortuSQL

> Uma ferramenta para estudantes que traz a sintaxe SQL para perto do português, tornando a aprendizagem mais natural e intuitiva.

O **PortuSQL** é um IDE desktop (Java Swing) que permite escrever consultas de banco de dados usando **palavras em português** no lugar dos comandos em inglês. Em vez de digitar `CREATE TABLE`, `SELECT` ou `UPDATE`, você escreve `criar tabela`, `selecionar` e `atualizar` — e a ferramenta traduz tudo para SQL real e executa no banco.

## Como funciona

A execução de uma consulta passa por um motor completo em 4 etapas:

```
Lexer → Parser → Tradutor → Executor
```

1. **Lexer** — transforma o texto digitado em tokens (palavras-chave, identificadores, valores…).
2. **Parser** — monta um objeto `Comando` a partir dos tokens, validando a estrutura da consulta.
3. **Tradutor** — converte o comando em português para SQL do banco H2.
4. **Executor** — executa o SQL gerado e devolve o resultado (grade de consulta ou linhas afetadas).

O resultado aparece na aba **Resultados** do editor e o SQL gerado na aba **Mensagens**, então o estudante vê, lado a lado, o comando em português e o SQL correspondente.

### Interface

- **Homepage** — tela de boas-vindas com cards de acesso rápido: novo banco, configurações, sugestões, dúvidas, comandos e "quem somos".
- **Editor de consultas** — onde você digita e executa os comandos em PortuSQL (atalho `F5` ou `Ctrl+Enter`).
- **Navegador de bancos** — visualiza tabelas e colunas do banco em uso.

### Segurança

Por padrão, o PortuSQL **nunca abre o banco real do usuário** sem pedir: o editor trabalha com o banco de testes dedicado `teste_portusql`. Expandir um banco no navegador (ou criar um novo) é que passa a usar o banco escolhido.

## Exemplos de comandos

| PortuSQL | SQL gerado (H2) |
|---|---|
| `criar tabela alunos (id int chave primaria, nome texto, idade int)` | `CREATE TABLE alunos (id int PRIMARY KEY, nome text, idade int)` |
| `inserir (nome, idade) em alunos valores ("João", 20)` | `INSERT INTO alunos (nome, idade) VALUES ('João', 20)` |
| `selecionar nome, idade de alunos onde idade > 18` | `SELECT nome, idade FROM alunos WHERE idade > 18` |
| `atualizar alunos definir idade = 21 onde nome = "João"` | `UPDATE alunos SET idade = 21 WHERE nome = 'João'` |
| `deletar de alunos onde idade < 18` | `DELETE FROM alunos WHERE idade < 18` |

### Palavras-chave suportadas

- **DDL:** `criar tabela`, `alterar tabela`, `adicionar coluna`, `remover coluna`, `renomear coluna`, `renomear tabela`, `excluir tabela`, `esvaziar tabela`
- **DML:** `inserir ... em ... valores`, `selecionar ... de`, `atualizar ... definir`, `deletar de`
- **Restrições:** `chave primaria`, `chave estrangeira`, `unica`, `nao nulo`, `adicionar restricao`, `remover restricao`, `renomear restricao`
- **Banco de dados:** `criar banco de dados`, `usar banco de dados`, `excluir banco de dados`
- **Consultas:** `onde`, `e`, `ou`, `nao`, `ordenar por`, `agrupar por`
- **Tipos de dado:** `int` / `inteiro`, `texto`, `decimal`, `data`, `hora`, `data_hora`, `booleano`, `flutuante`, entre outros

## Requisitos

- **Java 17+**
- **Maven 3.x**
- Dependência: banco H2 (gerenciada automaticamente pelo Maven)

## Como compilar e executar

```bash
# compilar o projeto
mvn compile

# rodar os testes
mvn test

# executar a aplicação
mvn exec:java -Dexec.mainClass="PortuSQL.PortuSQL.App"
```

> ⚠️ É necessário configurar o plugin `exec-maven-plugin` no `pom.xml` (ou gerar um JAR executável) para o comando acima; alternativamente, use o suporte de execução da sua IDE com a classe `PortuSQL.PortuSQL.App`.

## Estrutura do projeto

```
├── pom.xml                     # configuração Maven
└── src
    ├── main/java
    │   ├── PortuSQL/App.java   # ponto de entrada da aplicação
    │   ├── Telas/              # interface gráfica (Homepage, IDE, Sidebar, Cards, Tema)
    │   ├── Engine/             # Lexer, Parser, Tradutor, Executor
    │   ├── Comandos/           # modelos de cada comando PortuSQL (CriarTabela, Selecionar, Inserir…)
    │   └── Conexao/            # conexão com o banco H2
    └── test/java               # testes (motor de tradução e execução)
```

## Tecnologias

- **Java 17** (Swing para a interface)
- **Maven** (build e dependências)
- **H2 Database** (banco embutido, usado para testes e prática)

## Autor

Nicolas Gabriel Venâncio dos Santos

## Contribuições

Encontrou um problema ou tem uma sugestão? Abra uma issue ou participe das discussões no GitHub:

- Repositório: <https://github.com/00salociN/PortuSQL>
- Issues (sugestões de melhorias e bugs): <https://github.com/00salociN/PortuSQL/issues>
- Discussões (dúvidas): <https://github.com/00salociN/PortuSQL/discussions>