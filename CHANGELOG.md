# Histórico de alterações

Este arquivo resume o que mudou em cada versão do PortuSQL. A descrição pública de cada lançamento fica nas [notas das releases](https://github.com/00salociN/PortuSQL/releases) e, quando aplicável, em `.github/release-notes/`.

## [1.1.1]

### Adicionado

- Comentários de linha (`--`) e de bloco (`/* ... */`) na linguagem, ignorados pelo lexer e realçados no editor.
- Reconsulta automática de atualizações enquanto a IDE permanece aberta, sem precisar clicar no menu nem reiniciar.

## [1.1.0]

Atualização da 1.0.0 focada em acompanhamento de versões e experiência da IDE. A linguagem e os bancos H2 existentes continuam compatíveis: não é preciso recriar nem converter nada.

### Adicionado

- Verificação de atualizações em segundo plano, com aviso uma vez por versão e abertura das notas no navegador.
- Item **Ajuda → Verificar atualizações** na IDE.
- Painel lateral de DER com o esquema do banco atual e destaque das mudanças após cada execução.
- Catálogo único da linguagem, usado pelo analisador, pelo realce de sintaxe e pelas dicas do editor.
- Metadados de versão dentro do executável, lidos por `BuildInfo`.

### Alterado

- Instalação in-place: o instalador do Windows mantém o mesmo identificador de atualização e o `.jar` substitui o executável anterior.
- Documentação reorganizada para separar conteúdo permanente, referência da linguagem e histórico de versões.

### Observação

A verificação de atualizações apenas informa a versão disponível e abre as notas. A substituição automática do arquivo não foi incluída nesta versão.

[Notas completas da v1.1.0](.github/release-notes/v1.1.0.md)

## [1.0.0]

Primeira versão pública do PortuSQL.

### Adicionado

- Dialeto de SQL em português com 20 comandos em quatro famílias: DDL, DML, DQL e gerenciamento de banco.
- Interpretador próprio com lexer, parser, tradutor e executor.
- Banco H2 embarcado, com arquivos mantidos em `~/.portusql`.
- Interface gráfica em Java Swing com editor de consultas, navegador de bancos e tema escuro.
- Especificação da linguagem e referência de comandos.

[Notas completas da v1.0.0](.github/release-notes/v1.0.0.md)
