# Roadmap

Este arquivo registra direções planejadas para o PortuSQL. Ele não é um cronograma com prazos: prioridades podem mudar conforme o uso do projeto.

## Linguagem

- `JOIN` para relacionar tabelas em uma consulta.
- `HAVING` para filtrar resultados de agrupamento.
- Subconsultas.
- Comentários na linguagem.

## Interpretador e banco

- Isolar o tradutor e a conexão para permitir outros bancos. Hoje o único destino é o H2: a separação entre etapas ajuda, mas ainda não existe uma interface plugável nem suporte a múltiplos bancos.
- Ampliar as mensagens de erro para que falhas de tradução e do banco também apontem a posição no comando escrito.

## Verificação

- Criar uma suíte automatizada para lexer, parser, tradutor e execução no H2.
- Documentar os comandos para executar essa suíte e explicar o que cada conjunto verifica.

## Documentação e interface

- Registrar capturas adicionais da interface.
- Completar as ações que hoje são placeholders na IDE: salvar arquivo, histórico, configurações e botão **Parar**.

O estado atual dessas áreas está em [`LIMITACOES.md`](LIMITACOES.md). As mudanças já realizadas estão em [`CHANGELOG.md`](CHANGELOG.md).
