# Comandos e Funções do Banco de Dados H2

## 1. Criar Banco de Dados
**Sintaxe:** `CREATE DATABASE <nome>;
**Descrição:** Cria um novo banco de dados com o nome especificado.

## 2. Conectar-se ao Banco de Dados
**Sintaxe:** `CONNECT TO <nome>;
**Descrição:** Conecta ao banco de dados que foi criado ou existe.

## 3. Criar Tabela
**Sintaxe:**  
```
CREATE TABLE <nome_tabela> (
    <coluna1> <tipo1>,
    <coluna2> <tipo2>,
    ...
);
```
**Descrição:** Cria uma nova tabela com as colunas e tipos de dados especificados.

## 4. Inserir Dados
**Sintaxe:**  
```
INSERT INTO <nome_tabela> (<coluna1>, <coluna2>, ...) VALUES (<valor1>, <valor2>, ...);
```
**Descrição:** Insere uma nova linha na tabela com os valores fornecidos.

## 5. Atualizar Dados
**Sintaxe:**  
```
UPDATE <nome_tabela> SET <coluna1> = <novo_valor1> WHERE <condicao>;
```
**Descrição:** Atualiza as linhas que satisfazem a condição especificada.

## 6. Deletar Dados
**Sintaxe:**  
```
DELETE FROM <nome_tabela> WHERE <condicao>;
```
**Descrição:** Remove linhas da tabela que atendem à condição.

## 7. Selecionar Dados
**Sintaxe:**  
```
SELECT <colunas> FROM <nome_tabela> WHERE <condicao>;
```
**Descrição:** Recupera dados das tabelas com base nos critérios fornecidos.

## 8. Criar Índice
**Sintaxe:**  
```
CREATE INDEX <nome_indice> ON <nome_tabela> (<coluna>);
```
**Descrição:** Cria um índice para melhorar a performance de consultas.

## 9. Alterar Tabela
**Sintaxe:**  
```
ALTER TABLE <nome_tabela> ADD <coluna> <tipo>;
```
**Descrição:** Adiciona uma nova coluna à tabela existente.

## 10. Dropar Tabela
**Sintaxe:**  
```
DROP TABLE <nome_tabela>;
```
**Descrição:** Remove a tabela completamente do banco de dados.