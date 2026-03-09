# H2 Database Commands and Functions in Portuguese

Este arquivo contém uma lista abrangente de comandos e funções do banco de dados H2, com descrições completas e exemplos práticos. Todos os comandos são apresentados em inglês, seguido de sua tradução para o português, uma descrição do que fazem, um exemplo de sintaxe e um exemplo de uso prático.

## 1. Comandos de Manipulação de Dados (Data Manipulation Commands)

### SELECT
- **Inglês:** SELECT
- **Português:** SELECIONAR
- **Descrição:** Usado para consultar dados em uma tabela.
- **Exemplo de Sintaxe:** `SELECT * FROM tabela;`
- **Exemplo Prático:** `SELECT nome, idade FROM usuarios;`

### INSERT
- **Inglês:** INSERT
- **Português:** INSERIR
- **Descrição:** Usado para inserir novos dados em uma tabela.
- **Exemplo de Sintaxe:** `INSERT INTO tabela (coluna1, coluna2) VALUES (valor1, valor2);`
- **Exemplo Prático:** `INSERT INTO usuarios (nome, idade) VALUES ('João', 30);`

### UPDATE
- **Inglês:** UPDATE
- **Português:** ATUALIZAR
- **Descrição:** Usado para atualizar dados existentes em uma tabela.
- **Exemplo de Sintaxe:** `UPDATE tabela SET coluna1 = valor1 WHERE condicao;`
- **Exemplo Prático:** `UPDATE usuarios SET idade = 31 WHERE nome = 'João';`

### DELETE
- **Inglês:** DELETE
- **Português:** DELETAR
- **Descrição:** Usado para remover dados de uma tabela.
- **Exemplo de Sintaxe:** `DELETE FROM tabela WHERE condicao;`
- **Exemplo Prático:** `DELETE FROM usuarios WHERE nome = 'João';`

### BACKUP
- **Inglês:** BACKUP
- **Português:** BACKUP
- **Descrição:** Usado para criar um backup do banco de dados.
- **Exemplo de Sintaxe:** `BACKUP TO 'backup.zip';`
- **Exemplo Prático:** `BACKUP TO 'meu_backup.zip';`

### CALL
- **Inglês:** CALL
- **Português:** CHAMAR
- **Descrição:** Usado para chamar procedimentos armazenados.
- **Exemplo de Sintaxe:** `CALL meu_procedimento();`
- **Exemplo Prático:** `CALL inserir_usuario('Maria', 25);`

### EXECUTE IMMEDIATE
- **Inglês:** EXECUTE IMMEDIATE
- **Português:** EXECUTAR IMEDIATAMENTE
- **Descrição:** Usado para executar um comando SQL dinâmico.
- **Exemplo de Sintaxe:** `EXECUTE IMMEDIATE 'comando';`
- **Exemplo Prático:** `EXECUTE IMMEDIATE 'INSERT INTO usuarios (nome, idade) VALUES (''Pedro'', 40);';`

### EXPLAIN
- **Inglês:** EXPLAIN
- **Português:** EXPLICAR
- **Descrição:** Usado para mostrar o plano de execução de uma consulta.
- **Exemplo de Sintaxe:** `EXPLAIN SELECT * FROM tabela;`
- **Exemplo Prático:** `EXPLAIN SELECT nome FROM usuarios WHERE idade > 30;`

### MERGE INTO
- **Inglês:** MERGE INTO
- **Português:** MESCLAR EM
- **Descrição:** Usado para inserir ou atualizar registros com base em uma condição.
- **Exemplo de Sintaxe:** `MERGE INTO tabela USING outra_tabela ON (condicao) WHEN MATCHED THEN UPDATE SET coluna1 = valor1 WHEN NOT MATCHED THEN INSERT (coluna1) VALUES (valor1);`
- **Exemplo Prático:** `MERGE INTO destinatarios USING novos_destinatarios ON (destinatarios.id = novos_destinatarios.id) WHEN MATCHED THEN UPDATE SET destinatarios.nome = novos_destinatarios.nome WHEN NOT MATCHED THEN INSERT (id, nome) VALUES (novos_destinatarios.id, novos_destinatarios.nome);`

### MERGE USING
- **Inglês:** MERGE USING
- **Português:** MESCLAR USANDO
- **Descrição:** Similar ao MERGE INTO, mas usado com tabelas derivadas.
- **Exemplo de Sintaxe:** `MERGE USING (SELECT coluna1, coluna2 FROM outra_tabela) ON (condicao) ...`

### RUNSCRIPT
- **Inglês:** RUNSCRIPT
- **Português:** EXECUTAR SCRIPT
- **Descrição:** Usado para executar um script SQL.
- **Exemplo de Sintaxe:** `RUNSCRIPT FROM 'caminho/do/script.sql';`
- **Exemplo Prático:** `RUNSCRIPT FROM 'importar_dados.sql';`

### SCRIPT
- **Inglês:** SCRIPT
- **Português:** SCRIPT
- **Descrição:** Usado para criar um script SQL do banco de dados atual.
- **Exemplo de Sintaxe:** `SCRIPT 'caminho/para/script.sql';`
- **Exemplo Prático:** `SCRIPT 'meu_script.sql';`

### SHOW
- **Inglês:** SHOW
- **Português:** MOSTRAR
- **Descrição:** Usado para exibir informações sobre o banco de dados.
- **Exemplo de Sintaxe:** `SHOW TABLES;`
- **Exemplo Prático:** `SHOW USERS;`

### Explicit table
- **Inglês:** Explicit table
- **Português:** Tabela explícita
- **Descrição:** Refere-se ao uso explícito de tabelas em comandos SQL.
- **Exemplo de Sintaxe:** `SELECT * FROM tabela_explicita;`
- **Exemplo Prático:** `SELECT * FROM usuarios_explicitos;`

### Table value
- **Inglês:** Table value
- **Português:** Valor da tabela
- **Descrição:** Refere-se ao valor retornado por uma tabela.

### WITH
- **Inglês:** WITH
- **Português:** COM
- **Descrição:** Usado para definir uma subconsulta para uso em uma consulta maior.
- **Exemplo de Sintaxe:** `WITH subconsulta AS (SELECT ... ) SELECT * FROM subconsulta;`

### Comandos de Definição de Dados (Data Definition Commands)

### ALTER
- **Inglês:** ALTER
- **Português:** ALTERAR
- **Descrição:** Usado para modificar a estrutura de uma tabela existente.
- **Exemplo de Sintaxe:** `ALTER TABLE tabela ADD coluna tipo;`
- **Exemplo Prático:** `ALTER TABLE usuarios ADD email VARCHAR(255);`

### CREATE
- **Inglês:** CREATE
- **Português:** CRIAR
- **Descrição:** Usado para criar novas tabelas, índices ou outros objetos.
- **Exemplo de Sintaxe:** `CREATE TABLE tabela (coluna1 tipo, coluna2 tipo);`
- **Exemplo Prático:** `CREATE TABLE usuarios (id INT PRIMARY KEY, nome VARCHAR(100), idade INT);`

### DROP
- **Inglês:** DROP
- **Português:** REMOVER
- **Descrição:** Usado para remover tabelas ou objetos do banco de dados.
- **Exemplo de Sintaxe:** `DROP TABLE tabela;`
- **Exemplo Prático:** `DROP TABLE usuarios;`

### ANALYZE
- **Inglês:** ANALYZE
- **Português:** ANALISAR
- **Descrição:** Usado para atualizar as estatísticas de uma tabela.
- **Exemplo de Sintaxe:** `ANALYZE TABLE tabela;`
- **Exemplo Prático:** `ANALYZE TABLE usuarios;`

### COMMENT ON
- **Inglês:** COMMENT ON
- **Português:** COMENTAR EM
- **Descrição:** Usado para adicionar comentários a objetos do banco de dados.
- **Exemplo de Sintaxe:** `COMMENT ON TABLE tabela IS 'comentário';`
- **Exemplo Prático:** `COMMENT ON TABLE usuarios IS 'Tabela de usuários cadastrados';`

### TRUNCATE TABLE
- **Inglês:** TRUNCATE TABLE
- **Português:** TRUNCAR TABELA
- **Descrição:** Usado para remover todos os registros de uma tabela sem registrar cada exclusão.
- **Exemplo de Sintaxe:** `TRUNCATE TABLE tabela;`
- **Exemplo Prático:** `TRUNCATE TABLE usuarios;`

## 3. Outros Comandos (Other Commands)

### TRANSACTION
- **Inglês:** TRANSACTION
- **Português:** TRANSAÇÃO
- **Descrição:** Usado para iniciar uma nova transação.
- **Exemplo de Sintaxe:** `BEGIN TRANSACTION;`
- **Exemplo Prático:** `BEGIN TRANSACTION;`

### SECURITY
- **Inglês:** SECURITY
- **Português:** SEGURANÇA
- **Descrição:** Usado para definir os privilégios de segurança do banco de dados.
- **Exemplo de Sintaxe:** `SET USER '';`
- **Exemplo Prático:** `SET USER 'admin';`

### SET
- **Inglês:** SET
- **Português:** DEFINIR
- **Descrição:** Usado para definir variáveis de sessão ou configuração do banco de dados.
- **Exemplo de Sintaxe:** `SET variavel = valor;`
- **Exemplo Prático:** `SET AUTO_COMMIT = OFF;`

### SHUTDOWN
- **Inglês:** SHUTDOWN
- **Português:** DESLIGAR
- **Descrição:** Usado para desligar o servidor do banco de dados.
- **Exemplo de Sintaxe:** `SHUTDOWN;`
- **Exemplo Prático:** `SHUTDOWN;`

### HELP
- **Inglês:** HELP
- **Português:** AJUDA
- **Descrição:** Usado para obter ajuda sobre comandos disponíveis.
- **Exemplo de Sintaxe:** `HELP;`
- **Exemplo Prático:** `HELP;`

## 4. Funções Numéricas (Numeric Functions)

### Trigonometric Functions
- **Inglês:** Trigonometric Functions
- **Português:** Funções Trigonométricas
- **Descrição:** Funções utilizadas para cálculos trigonométricos. Exemplo inclui `SIN`, `COS`, `TAN`.

### Bitwise Functions
- **Inglês:** Bitwise Functions
- **Português:** Funções Bitwise
- **Descrição:** Operações que lidam com bits de um número. Exemplo inclui `BIT_AND`, `BIT_OR`.

### Logarithmic Functions
- **Inglês:** Logarithmic Functions
- **Português:** Funções Logarítmicas
- **Descrição:** Funções utilizadas para cálculos logarítmicos. Exemplo inclui `LOG`, `LOG10`.

## 5. Funções de String (String Functions)

### Concat
- **Inglês:** CONCAT
- **Português:** CONCATENAR
- **Descrição:** Usado para concatenar duas ou mais strings.
- **Exemplo de Sintaxe:** `CONCAT(string1, string2);`
- **Exemplo Prático:** `CONCAT('Olá', ' Mundo');`

### SUBSTR
- **Inglês:** SUBSTR
- **Português:** SUBSTR
- **Descrição:** Usado para extrair parte de uma string.
- **Exemplo de Sintaxe:** `SUBSTR(string, inicio, comprimento);`
- **Exemplo Prático:** `SUBSTR(nome, 1, 3);`

## 6. Funções de Data/Hora (Date/Time Functions)

### NOW
- **Inglês:** NOW
- **Português:** AGORA
- **Descrição:** Retorna a data e hora atual.
- **Exemplo de Sintaxe:** `NOW();`
- **Exemplo Prático:** `SELECT NOW();`

### DATEADD
- **Inglês:** DATEADD
- **Português:** ADICIONAR DATA
- **Descrição:** Adiciona um intervalo a uma data.
- **Exemplo de Sintaxe:** `DATEADD(intervalo, quantidade, data);`
- **Exemplo Prático:** `DATEADD('DAY', 1, NOW());`

## 7. Funções do Sistema (System Functions)

### USER
- **Inglês:** USER
- **Português:** USUÁRIO
- **Descrição:** Retorna o nome do usuário atual.
- **Exemplo de Sintaxe:** `USER();`
- **Exemplo Prático:** `SELECT USER();`

## 8. Funções Agregadas (Aggregate Functions)

### COUNT
- **Inglês:** COUNT
- **Português:** CONTAR
- **Descrição:** Conta o número de registros.
- **Exemplo de Sintaxe:** `COUNT(coluna);`
- **Exemplo Prático:** `SELECT COUNT(*) FROM usuarios;`

## 9. Funções de Janela (Window Functions)

### ROW_NUMBER
- **Inglês:** ROW_NUMBER
- **Português:** NÚMERO DA LINHA
- **Descrição:** Atribui um número sequencial a cada linha em uma partição.
- **Exemplo de Sintaxe:** `ROW_NUMBER() OVER (PARTITION BY coluna ORDER BY coluna);`
- **Exemplo Prático:** `SELECT nome, ROW_NUMBER() OVER (ORDER BY idade) AS num FROM usuarios;`

# Fim do Arquivo
