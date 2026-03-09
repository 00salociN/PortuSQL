Vou montar um **arquivo Markdown organizado e completo** com:

* ✅ **Todos os comandos que você listou**
* ✅ **Correções de organização**
* ✅ **Alguns comandos importantes do H2 que não estavam na lista**
* ✅ Estrutura mais próxima da documentação oficial
* ✅ Índice navegável

Você pode **copiar direto para um arquivo `h2-comandos-completo.md`**.

---

# H2 Database – Guia Completo de Comandos e Funções

Guia completo de **comandos SQL e funções do H2 Database**, incluindo manipulação de dados, definição de estrutura, transações, segurança e configuração.

Documentação oficial: [https://h2database.com](https://h2database.com)

---

# Índice

* Manipulação de Dados (DML)
* Definição de Dados (DDL)
* Controle de Transações
* Controle de Acesso
* Utilitários
* Configuração
* Funções Numéricas
* Funções de Texto
* Funções de Data
* Funções de Sistema

---

# Manipulação de Dados (DML)

## SELECT

Consulta dados de tabelas.

```sql
SELECT coluna FROM tabela;
```

Exemplo:

```sql
SELECT nome,email FROM usuarios WHERE idade > 18;
```

---

## INSERT

Insere dados em uma tabela.

```sql
INSERT INTO tabela(coluna1,coluna2)
VALUES(valor1,valor2);
```

Exemplo:

```sql
INSERT INTO usuarios(nome,email)
VALUES('João','joao@email.com');
```

---

## UPDATE

Atualiza registros existentes.

```sql
UPDATE tabela
SET coluna = valor
WHERE condição;
```

Exemplo:

```sql
UPDATE usuarios
SET email='novo@email.com'
WHERE id=1;
```

---

## DELETE

Remove registros.

```sql
DELETE FROM tabela
WHERE condição;
```

---

## MERGE INTO

Permite inserir ou atualizar registros.

```sql
MERGE INTO tabela_destino
USING tabela_origem
ON condição
WHEN MATCHED THEN UPDATE
WHEN NOT MATCHED THEN INSERT;
```

---

## WITH (CTE)

Define uma tabela temporária.

```sql
WITH ranking AS (
SELECT id,nome
FROM usuarios
)
SELECT * FROM ranking;
```

---

## CALL

Executa função ou procedimento.

```sql
CALL nome_funcao(parametros);
```

---

## EXPLAIN

Mostra plano de execução.

```sql
EXPLAIN SELECT * FROM usuarios;
```

---

## SCRIPT

Exporta banco em SQL.

```sql
SCRIPT TO 'backup.sql';
```

---

## RUNSCRIPT

Executa script SQL.

```sql
RUNSCRIPT FROM 'script.sql';
```

---

## BACKUP

Cria backup do banco.

```sql
BACKUP TO 'backup.zip';
```

---

## SHOW

Mostra objetos do banco.

```sql
SHOW TABLES;
SHOW COLUMNS FROM tabela;
SHOW SCHEMAS;
SHOW USERS;
```

---

# Definição de Dados (DDL)

## CREATE TABLE

Cria tabela.

```sql
CREATE TABLE usuarios(
id INT PRIMARY KEY AUTO_INCREMENT,
nome VARCHAR(100),
email VARCHAR(100)
);
```

---

## ALTER TABLE

Modifica estrutura da tabela.

### Adicionar coluna

```sql
ALTER TABLE usuarios
ADD COLUMN telefone VARCHAR(15);
```

### Alterar coluna

```sql
ALTER TABLE usuarios
ALTER COLUMN nome VARCHAR(200);
```

### Remover coluna

```sql
ALTER TABLE usuarios
DROP COLUMN telefone;
```

### Renomear tabela

```sql
ALTER TABLE usuarios
RENAME TO clientes;
```

---

## DROP TABLE

Remove tabela.

```sql
DROP TABLE usuarios;
```

---

## TRUNCATE TABLE

Remove todos os registros rapidamente.

```sql
TRUNCATE TABLE usuarios;
```

---

## CREATE INDEX

Cria índice.

```sql
CREATE INDEX idx_email
ON usuarios(email);
```

---

## DROP INDEX

Remove índice.

```sql
DROP INDEX idx_email;
```

---

## CREATE VIEW

Cria view.

```sql
CREATE VIEW vw_usuarios
AS
SELECT nome,email
FROM usuarios;
```

---

## DROP VIEW

Remove view.

```sql
DROP VIEW vw_usuarios;
```

---

## CREATE MATERIALIZED VIEW

View com dados armazenados.

```sql
CREATE MATERIALIZED VIEW mv_vendas
AS
SELECT mes,SUM(valor)
FROM vendas
GROUP BY mes;
```

---

## REFRESH MATERIALIZED VIEW

Atualiza dados.

```sql
REFRESH MATERIALIZED VIEW mv_vendas;
```

---

## CREATE SEQUENCE

Cria sequência numérica.

```sql
CREATE SEQUENCE seq_id
START WITH 1
INCREMENT BY 1;
```

---

## ALTER SEQUENCE

Modifica sequência.

```sql
ALTER SEQUENCE seq_id
INCREMENT BY 10;
```

---

## DROP SEQUENCE

Remove sequência.

```sql
DROP SEQUENCE seq_id;
```

---

## CREATE SCHEMA

Cria esquema.

```sql
CREATE SCHEMA vendas;
```

---

## DROP SCHEMA

Remove esquema.

```sql
DROP SCHEMA vendas CASCADE;
```

---

## CREATE TRIGGER

Cria gatilho.

```sql
CREATE TRIGGER tr_insert
AFTER INSERT
ON usuarios
FOR EACH ROW
CALL "com.exemplo.Trigger";
```

---

## DROP TRIGGER

Remove trigger.

```sql
DROP TRIGGER tr_insert;
```

---

# Controle de Transações

## BEGIN

Inicia transação.

```sql
BEGIN;
```

---

## COMMIT

Confirma alterações.

```sql
COMMIT;
```

---

## ROLLBACK

Desfaz alterações.

```sql
ROLLBACK;
```

---

## SAVEPOINT

Cria ponto intermediário.

```sql
SAVEPOINT sp1;
```

---

## ROLLBACK TO SAVEPOINT

```sql
ROLLBACK TO SAVEPOINT sp1;
```

---

# Controle de Acesso

## CREATE USER

```sql
CREATE USER joao PASSWORD '123';
```

---

## ALTER USER

```sql
ALTER USER joao SET PASSWORD 'novaSenha';
```

---

## DROP USER

```sql
DROP USER joao;
```

---

## CREATE ROLE

```sql
CREATE ROLE admin;
```

---

## GRANT

Concede permissões.

```sql
GRANT SELECT,INSERT
ON usuarios
TO joao;
```

---

## REVOKE

Remove permissões.

```sql
REVOKE DELETE
ON usuarios
FROM joao;
```

---

# Utilitários

## ANALYZE

Atualiza estatísticas.

```sql
ANALYZE;
```

---

## CHECKPOINT

Força gravação em disco.

```sql
CHECKPOINT;
```

---

## SHUTDOWN

Fecha banco.

```sql
SHUTDOWN;
```

---

## HELP

Mostra ajuda.

```sql
HELP;
```

---

# Configuração

## SET MODE

Modo compatibilidade.

```sql
SET MODE PostgreSQL;
```

---

## SET SCHEMA

Define schema atual.

```sql
SET SCHEMA vendas;
```

---

## SET AUTOCOMMIT

```sql
SET AUTOCOMMIT TRUE;
```

---

## SET TIME ZONE

```sql
SET TIME ZONE 'America/Sao_Paulo';
```

---

## SET LOCK_TIMEOUT

```sql
SET LOCK_TIMEOUT 3000;
```

---

# Funções Numéricas

## ABS

```sql
SELECT ABS(-5);
```

Resultado

```
5
```

---

## ROUND

```sql
SELECT ROUND(10.567,2);
```

---

## CEILING

```sql
SELECT CEILING(4.3);
```

---

## FLOOR

```sql
SELECT FLOOR(4.8);
```

---

## POWER

```sql
SELECT POWER(2,3);
```

Resultado

```
8
```

---

## SQRT

```sql
SELECT SQRT(16);
```

---

## PI

```sql
SELECT PI();
```

---

# Funções de Texto

## CONCAT

```sql
SELECT CONCAT('Hello',' ','World');
```

---

## LENGTH

```sql
SELECT LENGTH('abc');
```

---

## LOWER

```sql
SELECT LOWER('ABC');
```

---

## UPPER

```sql
SELECT UPPER('abc');
```

---

## SUBSTRING

```sql
SELECT SUBSTRING('abcdef',1,3);
```

---

## REPLACE

```sql
SELECT REPLACE('abcabc','a','x');
```

---

# Funções de Data

## CURRENT_DATE

```sql
SELECT CURRENT_DATE;
```

---

## CURRENT_TIME

```sql
SELECT CURRENT_TIME;
```

---

## CURRENT_TIMESTAMP

```sql
SELECT CURRENT_TIMESTAMP;
```

---

## DATEADD

```sql
SELECT DATEADD('DAY',5,CURRENT_DATE);
```

---

## DATEDIFF

```sql
SELECT DATEDIFF('DAY','2024-01-01','2024-01-10');
```

---

## EXTRACT

```sql
SELECT EXTRACT(YEAR FROM CURRENT_DATE);
```

---

# Funções de Sistema

## DATABASE()

```sql
SELECT DATABASE();
```

---

## USER()

```sql
SELECT USER();
```

---

## SESSION_ID()

```sql
SELECT SESSION_ID();
```

---

# Comandos Avançados do H2

## CREATE ALIAS

Permite chamar funções Java.

```sql
CREATE ALIAS SQRT
FOR "java.lang.Math.sqrt";
```

---

## CREATE DOMAIN

Tipo de dado customizado.

```sql
CREATE DOMAIN email
AS VARCHAR(255)
CHECK(VALUE LIKE '%@%');
```

---

## CREATE LINKED TABLE

Tabela conectada a outro banco.

```sql
CREATE LINKED TABLE clientes_externos
(
'org.postgresql.Driver',
'jdbc:postgresql://host/db',
'user',
'pass',
'public',
'clientes'
);
```

---

# Comando Perigoso

## DROP ALL OBJECTS

Apaga **todo o banco**.

```sql
DROP ALL OBJECTS;
```

⚠ Use com extremo cuidado.

---

# Observação

O **H2 Database suporta mais de 300 funções SQL**, incluindo:

* funções JSON
* funções agregadas
* funções analíticas (WINDOW FUNCTIONS)
* funções espaciais

Documentação completa:

[https://h2database.com/html/functions.html](https://h2database.com/html/functions.html)

---

Se quiser, posso também criar uma **versão MUITO mais completa (tipo documentação profissional)** com:

* **todos os 300+ comandos e funções do H2**
* exemplos
* tabelas
* comparações com **PostgreSQL / MySQL**
* diagramas

ficaria um **guia de 1200+ linhas bem organizado para estudo ou documentação**.
