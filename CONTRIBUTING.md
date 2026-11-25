# Contribuindo

Obrigado pelo interesse no PortuSQL. Este arquivo explica como preparar o ambiente, compilar o projeto e verificar uma mudança antes de abrir um pull request.

## Ambiente

- Java 17 ou superior.
- Maven Wrapper incluído no repositório (`./mvnw`); não é preciso instalar o Maven separadamente.
- Nada mais é necessário para compilar: o H2 é uma dependência do projeto e termina embutido no executável.

## Compilação

```bash
./mvnw clean package
# execute o .jar gerado em target/
```

O shade plugin gera o executável com o H2 dentro. `src/main/resources/version.properties` é filtrado pelo Maven com a versão do `pom.xml`; `BuildInfo.versao()` lê esse arquivo de dentro do `.jar`.

## Verificações antes do pull request

1. Rode `./mvnw clean package` e confirme que o build termina sem erros.
2. Execute o `.jar` gerado e teste o comando alterado na IDE.
3. Confira o SQL mostrado na aba **Mensagens**, além do resultado na aba **Resultados**.
4. Se a linguagem mudar, atualize [`COMANDOS.md`](COMANDOS.md) e [`ESPECIFICACAO.md`](ESPECIFICACAO.md) no mesmo pull request.
5. Se uma limitação deixou de existir ou uma nova apareceu, ajuste [`LIMITACOES.md`](LIMITACOES.md).

Ainda não há suíte automatizada de testes no repositório. Ela está registrada em [`ROADMAP.md`](ROADMAP.md); quando existir, este arquivo passa a trazer os comandos para executá-la.

## Escopo

Abra uma [issue](https://github.com/00salociN/PortuSQL/issues) antes do pull request para alinhar a mudança. Isso evita trabalho duplicado e deixa claro qual documento precisa acompanhar a alteração.

Dúvidas gerais podem ser postadas nas [Discussões](https://github.com/00salociN/PortuSQL/discussions).
