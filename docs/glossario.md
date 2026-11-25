# Glossário

Termos que aparecem na documentação técnica do PortuSQL.

| Termo | Significado |
|---|---|
| Lexer | etapa que percorre o texto e separa palavras, números, símbolos e textos em tokens |
| Parser | etapa que verifica a gramática e transforma tokens em um comando estruturado |
| Token | menor unidade reconhecida pelo lexer; guarda tipo, texto, linha e coluna |
| AST | Árvore Sintática Abstrata; aqui, o objeto `Comando` e seus auxiliares que representam a instrução reconhecida |
| JDBC | API do Java para conversar com bancos de dados; é usada pelo `Executor` para enviar SQL ao H2 |
| Shaded JAR | `.jar` que inclui as dependências dentro dele; aqui, o H2 já vem embutido no executável |
| DER | Diagrama de Entidades e Relacionamentos; no PortuSQL, mostra tabelas, colunas e chaves estrangeiras do banco em uso |
| Metadados | dados sobre a estrutura do banco, como tabelas, colunas e chaves, lidos pelo H2 para montar o navegador e o DER |

Os termos ligados ao fluxo de execução são detalhados em [`arquitetura.md`](arquitetura.md).
