# Instalação

Este arquivo concentra artefatos, requisitos e compatibilidade. O [início rápido](../README.md#início-rápido) mostra apenas o caminho mínimo para começar.

## Requisitos

- Java 17 ou superior para o `.jar`.
- O H2 vem embarcado: não é preciso instalar banco nem configurar classpath.
- O instalador do Windows traz o Java embutido.

## Artefatos da release

Os arquivos são publicados como anexos na [página de releases](https://github.com/00salociN/PortuSQL/releases). Os nomes acompanham a versão; abaixo estão os nomes da release atual.

| Arquivo | Indicado para | Conteúdo |
|---|---|---|
| `PortuSQL-1.1.0.msi` | Windows | aplicação com Java embutido |
| `PortuSQL.jar` | Windows, Linux e macOS com Java 17+ | aplicação e H2 embarcados |
| `instalar-linux.sh` | Linux | cria executável, ícone e atalho no menu |

O workflow `.github/workflows/release.yml` gera os três arquivos. A publicação da release continua manual.

## Windows

Baixe o `.msi` e execute o instalador. Não é preciso instalar o Java separadamente.

A atualização é feita no lugar: o instalador mantém o mesmo identificador de atualização da versão anterior, substitui a instalação existente e não apaga os bancos em `~/.portusql`.

## Jar

Baixe `PortuSQL.jar` e execute:

```bash
java -jar PortuSQL.jar
```

O mesmo arquivo serve para qualquer sistema com Java 17 ou superior.

## Linux

Baixe **os dois arquivos** `PortuSQL.jar` e `instalar-linux.sh` da mesma release, coloque-os na mesma pasta e execute:

```bash
chmod +x instalar-linux.sh
./instalar-linux.sh PortuSQL.jar
```

O script instala o executável em `~/.local/share/portusql/`, o ícone e um atalho no menu de aplicações. Ele é gerado pelo mesmo workflow que produz o `.jar` e o `.msi`, por isso sempre acompanha a release.

Um `.jar` é um arquivo ZIP. No Linux, o gerenciador de arquivos costuma abri-lo para mostrar o conteúdo em vez de executá-lo. Por isso o `.jar` publicado começa com um inicializador de shell: continua válido para a JVM e passa a ser reconhecido como programa executável após o `chmod +x`.

## A partir do código

```bash
git clone https://github.com/00salociN/PortuSQL.git
cd PortuSQL
./mvnw clean package
java -jar target/PortuSQL-*.jar
```

O `./mvnw` é o Maven Wrapper: o Maven é baixado automaticamente, e basta ter o Java instalado. O `*` ajuda quando não se quer decorar o número da versão; se houver mais de um `.jar` em `target/`, escolha o da versão desejada.

## Compatibilidade

| Situação | Sistemas | Observação |
|---|---|---|
| Verificado nesta rodada | Linux | build, execução do `.jar` e fluxo da IDE validados em ambiente Linux |
| Compatibilidade esperada | Windows e macOS | por ser Java/Swing, o `.jar` deve funcionar com Java 17+; não foi executado aqui |
| Específico para Windows | Windows | o `.msi` usa `jpackage` e traz um runtime montado com `jlink` |

Compatibilidade esperada não significa validação: ela indica o comportamento previsto pela tecnologia usada.
