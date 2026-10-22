#!/usr/bin/env python3
"""
Audita se a documentacao do PortuSQL esta em sincronia com o codigo-fonte.

Compara, automaticamente:
  1. Anexo A da ESPECIFICACAO  x  palavras reservadas do KW.java
  2. Secao 12.2 da ESPECIFICACAO x  substituicoes reais do Tradutor.java
  3. Secao 10.3 da ESPECIFICACAO x  mapeamento das agregadas do Parser.java
  4. Secao 10.1/10.2            x  tipos aceitos pelo Lexer
  5. Secoes do COMANDOS.md      x  classes de comando em Comandos/
  6. Recursos novos (DISTINCT, INSERIR multi-linha, CARACTERE VARIAVEL)
  7. Ausencia de texto obsoleto (por exemplo, o antigo "NOT NULO")

Uso:  python3 tools/auditar_docs.py
Sai com codigo 1 se encontrar alguma divergencia.
"""

import os
import re
import sys

REPO = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))


def ler(caminho):
    with open(os.path.join(REPO, caminho), encoding='utf-8') as f:
        return f.read()


kw = ler('src/main/java/Engine/KW.java')
trad = ler('src/main/java/Engine/Tradutor.java')
par = ler('src/main/java/Engine/Parser.java')
esp = ler('ESPECIFICACAO.md')
cmd = ler('COMANDOS.md')
rd = ler('README.md')
rel = ler('.github/release-notes/v1.0.0.md')

falhas = []


def checar(titulo, condicao, detalhe=''):
    marca = '[OK]    ' if condicao else '[FALHA] '
    print(marca + titulo + (('  -> ' + detalhe) if detalhe and not condicao else ''))
    if not condicao:
        falhas.append(titulo)


def normalizar(padrao):
    """normaliza um padrao de replace do Java para comparar com a documentacao"""
    p = re.sub(r'\(\?i\)', '', padrao)
    p = re.sub(r'\\+b', '', p)        # remove \b  (no arquivo Java aparece como \\b)
    p = re.sub(r'\\+s\+', ' ', p)     # remove \s+
    return re.sub(r'\s+', ' ', p).strip().upper()


print('=== 1. Anexo A (palavras reservadas) x KW.java ===')
reservadas = {p for p in re.findall(r'palavras_simples\.put\("([^"]*)"', kw) if p}
reservadas |= set(re.findall(r'funcoes_agregadas\.put\("([^"]*)"', kw))
bloco = re.findall(r'```\n(.*?)```', esp.split('## Anexo A')[-1], re.S)[0]
documentadas = set(bloco.replace('\n', ' ').split())
checar('toda palavra reservada do codigo esta no Anexo A',
       reservadas <= documentadas, str(sorted(reservadas - documentadas)))
checar('Anexo A nao lista palavra inexistente no codigo',
       documentadas <= reservadas, str(sorted(documentadas - reservadas)))
checar('Anexo A sem duplicatas',
       len(bloco.split()) == len(documentadas),
       f'{len(bloco.split())} tokens vs {len(documentadas)} unicos')

print('=== 2. Secao 12.2 (substituicoes) x Tradutor.java ===')
reais = {normalizar(m) for m in
         re.findall(r'replace(?:All)?\("\(\?i\)([^"]*)"', trad)}
tabela = esp.split('### 12.2')[1].split('###')[0]
documentadas = {normalizar(a) for a, _ in
                re.findall(r'\| `([A-ZÀ-Ú ]+)` \| `([A-Z ]+)` \|', tabela)}
checar(f'substituicoes do codigo {sorted(reais)} <= §12.2 {sorted(documentadas)}',
       reais <= documentadas, str(sorted(reais - documentadas)))
checar('§12.2 registra IS NOT NULL', 'IS NOT NULL' in tabela)
checar('§12.2 nao tem o obsoleto NOT NULO', 'NOT NULO' not in tabela)

print('=== 3. Secao 10.3 (agregadas) x Parser.java ===')
mapa_codigo = dict(re.findall(r'case "(\w+)":\s*\n\s*return "(\w+)";', par))
secao = esp.split('### 10.3')[1].split('## 11.')[0] if '### 10.3' in esp else ''
mapa_doc = {m[0].lower(): m[1] for m in re.findall(r'\| `(\w+)` \| `(\w+)` \|', secao)}
checar('agregadas: Parser.java == §10.3', mapa_codigo == mapa_doc,
       f'{mapa_codigo} vs {mapa_doc}')

print('=== 4. Tipos: secao 10 x KW.java ===')
tipos_codigo = set(re.findall(r'tipo_dados\.put\("([^"]*)"', kw))
secao10 = esp.split('## 10. Tipos de dado')[1].split('## 11.')[0]
tipos_doc = set(re.findall(r'"(\w+)"', secao10.split('### 10.1')[1].split('### 10.2')[0]))
checar('todos os tipos do Lexer estao em §10.1',
       tipos_codigo <= tipos_doc, str(sorted(tipos_codigo - tipos_doc)))
checar('§10.2 mapeia os 8 tipos com traducao',
       len(re.findall(r'\| `(?:int|texto|data|decimal|hora|data_hora|caractere \w+\(n\))`', secao10)) >= 8)

print('=== 5. Comandos: classes Java x COMANDOS.md ===')
pacote = os.path.join(REPO, 'src/main/java/Comandos')
auxiliares = ('Comando.java', 'DefinicaoColuna.java', 'FuncaoAgregada.java')
classes = [f for f in os.listdir(pacote)
           if f.endswith('.java') and f not in auxiliares]
checar('20 classes de comando', len(classes) == 20, str(len(classes)))
checar('20 secoes no COMANDOS.md', len(re.findall(r'^###\s*\d+\.', cmd, re.M)) == 20)
checar('sumario do COMANDOS.md com 20 entradas', len(re.findall(r'^  - \[', cmd, re.M)) == 20)
checar("'20 comandos' no README e no release notes",
       '20 comandos' in rd and '20 comandos' in rel)

print('=== 6. Recursos novos documentados ===')
multi = re.compile(r'VAL(?:ORES|UES)\s*\([^)]*\)\s*,\s*\(')
for nome, txt in (('ESPECIFICACAO', esp), ('COMANDOS', cmd),
                  ('README', rd), ('release-notes', rel)):
    d = 'DISTINCT' in txt.upper()
    m = bool(multi.search(txt))
    c = 'CARACTERE VARIAVEL' in txt.upper()
    checar(f'{nome}: DISTINCT={d} multi-linha={m} CARACTERE VARIAVEL={c}', d and m and c)

print('=== 7. Nada obsoleto ===')
for nome, txt in (('ESPECIFICACAO', esp), ('COMANDOS', cmd),
                  ('README', rd), ('release-notes', rel)):
    checar(f"{nome}: sem 'NOT NULO' obsoleto", 'NOT NULO' not in txt)
    checar(f"{nome}: sem o SQL quebrado do VALUES",
           'String.join(", ", inserir' not in txt)

print()
if falhas:
    print(f'RESULTADO: {len(falhas)} INCONSISTENCIA(S)')
    for f in falhas:
        print('  -', f)
    sys.exit(1)
print('RESULTADO: DOCUMENTACAO 100% EM SINCRONIA COM O CODIGO')
