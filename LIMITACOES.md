# Limitações conhecidas

Este arquivo descreve o estado atual do PortuSQL. Funcionalidades planejadas ficam em [`ROADMAP.md`](ROADMAP.md); mudanças já entregues ficam em [`CHANGELOG.md`](CHANGELOG.md).

## Recursos não implementados

| Situação | Como contornar |
|---|---|
| Não há `JOIN`, `HAVING` nem subconsultas | usar consultas de uma única tabela, com `ONDE` e `AGRUPAR POR` |
| Não há comentários na linguagem | manter as explicações fora do comando |
| H2 é o único banco de destino | usar o banco embarcado; suporte a outros bancos está planejado, mas ainda não existe |

## Recursos parcialmente implementados

| Situação | O que acontece | Como contornar |
|---|---|---|
| `USAR BANCO DE DADOS` | o parser reconhece o comando, mas não há tradução registrada | trocar de banco pelo navegador da IDE |
| `gint`, `cint` e `mint` | são aceitos como tipos pelo lexer, mas o tradutor não tem mapeamento para o H2 | usar `int`, `texto`, `data`, `decimal` ou os demais tipos da referência |
| Erros de tradução e do H2 | a mensagem não aponta linha e coluna do comando escrito | conferir o SQL gerado na aba **Mensagens** da IDE |

## Funcionalidades temporariamente indisponíveis

| Situação | Como contornar |
|---|---|
| `EXCLUIR BANCO DE DADOS` é bloqueado pelo tradutor | excluir o banco pelo navegador da IDE |
| Salvar arquivo, histórico, configurações e **Parar** são placeholders | copiar o texto do editor e salvar manualmente |

## Comportamento atual da IDE

A IDE executa uma instrução por vez. O Enter não continua o comando: selecione um comando no editor e execute antes de passar para o próximo.

Os exemplos da linguagem estão em [`COMANDOS.md`](COMANDOS.md), e as regras formais em [`ESPECIFICACAO.md`](ESPECIFICACAO.md).
