# H2_DATABASE_COMPLETE_GUIDE.md

## Introdução
O H2 é um banco de dados relacional em memória e uma solução de banco de dados leve. Este guia fornece uma lista completa de comandos e funções do H2, traduzidos para o português, com descrições, sintaxes e exemplos.

### Comandos de Manipulação de Dados
#### SELECT
**Descrição**: O comando SELECT é utilizado para consultar dados de uma tabela.

**Sintaxe**: `SELECT coluna1, coluna2 FROM tabela WHERE condição;`

**Exemplo**: `SELECT nome FROM clientes WHERE cidade='São Paulo';`

#### INSERT
**Descrição**: O comando INSERT é utilizado para inserir novos registros em uma tabela.

**Sintaxe**: `INSERT INTO tabela (coluna1, coluna2) VALUES (valor1, valor2);`

**Exemplo**: `INSERT INTO clientes (nome, cidade) VALUES ('João', 'Rio de Janeiro');`

#### UPDATE
**Descrição**: O comando UPDATE é utilizado para modificar dados existentes em uma tabela.

**Sintaxe**: `UPDATE tabela SET coluna1=valor1 WHERE condição;`

**Exemplo**: `UPDATE clientes SET cidade='Belo Horizonte' WHERE nome='João';`

#### DELETE
**Descrição**: O comando DELETE é utilizado para remover registros de uma tabela.

**Sintaxe**: `DELETE FROM tabela WHERE condição;`

**Exemplo**: `DELETE FROM clientes WHERE nome='João';`

#### BACKUP
**Descrição**: O comando BACKUP cria uma cópia de segurança do banco de dados.

**Sintaxe**: `BACKUP TO 'caminho/para/backup';`

**Exemplo**: `BACKUP TO 'backup_h2.zip';`

#### CALL
**Descrição**: O comando CALL executa procedimentos armazenados.

**Sintaxe**: `CALL procedimento(parametros);`

**Exemplo**: `CALL calcularDesconto(10);`

#### EXECUTE IMMEDIATE
**Descrição**: O comando EXECUTE IMMEDIATE executa uma instrução SQL como uma string.

**Sintaxe**: `EXECUTE IMMEDIATE 'instrução SQL';`

**Exemplo**: `EXECUTE IMMEDIATE 'DROP TABLE tabela_teste';`

#### EXPLAIN
**Descrição**: O comando EXPLAIN fornece informações sobre como uma consulta será executada.

**Sintaxe**: `EXPLAIN SELECT * FROM tabela;

**Exemplo**: `EXPLAIN SELECT nome FROM clientes WHERE cidade='São Paulo';`

#### MERGE INTO
**Descrição**: O comando MERGE INTO insere ou atualiza registros em uma tabela com base na correspondência de chaves.

**Sintaxe**: `MERGE INTO tabela_destino USING tabela_src ON condição WHEN MATCHED THEN UPDATE SET coluna1=valor1 WHEN NOT MATCHED THEN INSERT (colunas) VALUES (valores);`

**Exemplo**: `MERGE INTO clientes_destino USING clientes_origem ON (clientes_destino.id = clientes_origem.id) WHEN MATCHED THEN UPDATE SET clientes_destino.nome = clientes_origem.nome WHEN NOT MATCHED THEN INSERT (id, nome) VALUES (clientes_origem.id, clientes_origem.nome);`

#### MERGE USING
**Descrição**: O comando MERGE USING combina vários registros de uma tabela com base na condição especificada.

**Sintaxe**: Igual ao MERGE INTO.

#### RUNSCRIPT
**Descrição**: O comando RUNSCRIPT executa um script SQL de um arquivo.

**Sintaxe**: `RUNSCRIPT FROM 'caminho/para/script.sql';`

**Exemplo**: `RUNSCRIPT FROM 'script_inicial.sql';`

#### SCRIPT
**Descrição**: O comando SCRIPT gera um script SQL com instruções para recriar o banco de dados.

**Sintaxe**: `SCRIPT 'caminho/para/script.sql';`

**Exemplo**: `SCRIPT 'backup_script.sql';`

#### SHOW
**Descrição**: O comando SHOW exibe metadados sobre o banco de dados ou suas tabelas.

**Sintaxe**: `SHOW TABLES;`

**Exemplo**: `SHOW TABLES;`

#### Explicit Table
**Descrição**: Permite a referência a uma tabela específica em consultas.

**Sintaxe**: `Tabela(tabela) AS alias;`

#### Table Value
**Descrição**: Define um valor de tabela para um determinado escopo.

#### WITH
**Descrição**: O comando WITH é usado para definir uma consulta comum para ser referida em instruções subsequentes.

**Sintaxe**: `WITH nome_comum AS (consulta) SELECT * FROM nome_comum;

**Exemplo**: `WITH vendas_totais AS (SELECT cliente_id, SUM(valor) as total FROM vendas GROUP BY cliente_id) SELECT * FROM vendas_totais;`

### Comandos de Definição de Dados
#### ALTER
**Descrição**: O comando ALTER modifica a estrutura de uma tabela existente.

**Sintaxe**: `ALTER TABLE tabela ADD coluna tipo;`

**Exemplo**: `ALTER TABLE clientes ADD email VARCHAR(255);`

#### CREATE
**Descrição**: O comando CREATE é utilizado para criar novas tabelas ou outros objetos de banco de dados.

**Sintaxe**: `CREATE TABLE tabela (coluna1 tipo, coluna2 tipo);`

**Exemplo**: `CREATE TABLE clientes (id INT PRIMARY KEY, nome VARCHAR(255));`

#### DROP
**Descrição**: O comando DROP remove tabelas ou objetos do banco de dados.

**Sintaxe**: `DROP TABLE tabela;

**Exemplo**: `DROP TABLE clientes;`

#### ANALYZE
**Descrição**: O comando ANALYZE analisa a tabela e coleta estatísticas.

**Sintaxe**: `ANALYZE TABLE tabela;`

**Exemplo**: `ANALYZE TABLE clientes;`

#### COMMENT ON
**Descrição**: O comando COMMENT ON adiciona comentários descritivos a objetos do banco de dados.

**Sintaxe**: `COMMENT ON TABLE tabela IS 'comentário';`

#### TRUNCATE
**Descrição**: O comando TRUNCATE remove todos os registros de uma tabela sem registrar cada exclusão.

**Sintaxe**: `TRUNCATE TABLE tabela;

**Exemplo**: `TRUNCATE TABLE clientes;`

#### REFRESH
**Descrição**: O comando REFRESH atualiza uma tabela ou um objeto.

### Outros Comandos
#### CHECKPOINT
**Descrição**: O comando CHECKPOINT força a gravação de todas as alterações no disco.

**Sintaxe**: `CHECKPOINT;`

**Exemplo**: `CHECKPOINT;`

#### COMMIT
**Descrição**: O comando COMMIT salva todas as alterações feitas na transação atual.

**Sintaxe**: `COMMIT;`

#### GRANT
**Descrição**: O comando GRANT concede permissões a usuários ou roles.

**Sintaxe**: `GRANT permissões ON objeto TO usuário;`

#### REVOKE
**Descrição**: O comando REVOKE remove permissões concedidas.

**Sintaxe**: `REVOKE permissões ON objeto FROM usuário;`

#### SET variations
**Descrição**: O comando SET é usado para alterar configurações do banco de dados.

**Sintaxe**: `SET variável = valor;`

#### SHUTDOWN
**Descrição**: O comando SHUTDOWN encerra a sessão do banco de dados ou o servidor.

**Sintaxe**: `SHUTDOWN;`

**Exemplo**: `SHUTDOWN;`

#### HELP
**Descrição**: O comando HELP fornece informações sobre comandos disponível.

**Sintaxe**: `HELP comando;`

### Funções Numéricas
- **Descrição**: Funções para processamento numérico, como funções trigonométricas, lógicas e matemáticas.

### Funções de String
- **Descrição**: Funções para manipulação de strings, incluindo concatenação, conversão e busca.

### Funções de Data/Hora
- **Descrição**: Funções para manipulação de data e hora, como extrair partes e calcular intervalos.

### Funções do Sistema
- **Descrição**: Funções que fornecem informações sobre o estado e propriedades do banco de dados.

### Funções de Agregação Geral
- **Descrição**: Funções para calcular valores estatísticos em conjuntos de dados.

### Funções de Conjunto Binário
- **Descrição**: Funções que operam em conjuntos de dados binários.

### Funções de Agregação Ordenadas
- **Descrição**: Funções que produzem resultados agregados ordenados.

### Funções de Conjunto Hipotético
- **Descrição**: Funções específicas para análise de classificação.

### Funções de Distribuição Inversa
- **Descrição**: Funções utilizadas para determinar a distribuição de valores.

### Funções de Agregação JSON
- **Descrição**: Funções que operam com objetos JSON.

### Funções de Janela
- **Descrição**: Funções que realizam operações sobre um conjunto de linhas, mantendo o contexto de execução.

## Conclusão
Explore as riquezas do H2 e aproveite ao máximo as suas funcionalidades.