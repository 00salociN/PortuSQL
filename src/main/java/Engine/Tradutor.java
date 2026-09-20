/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Engine;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import Comandos.*;
import java.util.stream.Collectors;

/**
 *
 * @author Etec
 */
public class Tradutor {
	StringBuilder banco = new StringBuilder();
    private static final Map<String, String> tipo_dados = new HashMap<>();
    
    static {
        tipo_dados.put("int", "int");
        tipo_dados.put("inteiro", "int");
        tipo_dados.put("texto", "text");
        tipo_dados.put("data", "date");
        tipo_dados.put("decimal", "decimal");
        tipo_dados.put("flutuante", "float");
        tipo_dados.put("booleano", "boolean");
        tipo_dados.put("", ""); // varchar
        tipo_dados.put("", ""); // char
        tipo_dados.put("", ""); // bigint
        tipo_dados.put("", ""); // smallint
        tipo_dados.put("", "");// tinyint
        tipo_dados.put("hora", "time");
        tipo_dados.put("data_hora", "timestamp");
        tipo_dados.put("binario", "binary");
        tipo_dados.put("enum", "enum");
        tipo_dados.put("enumerar", "enum");
        tipo_dados.put("real", "real");
        tipo_dados.put("numerico", "numeric");
    }
    
    private static final Map<String, String> tipo_restricao = new HashMap<>();
    static {
    	tipo_restricao.put("chave primaria", "primary key");
    	tipo_restricao.put("chave estrangeira", "foreign key");
    	tipo_restricao.put("unica", "unique");
    }
        
        private final Map<Class<?>, Function<Comando, String>> traducao = new HashMap<>();
        
        public Tradutor() {
            traducao.put(AdicionarColuna.class, comando -> traducaoAdicionarColuna((AdicionarColuna) comando));
            traducao.put(AdicionarRestricao.class, comando -> traducaoAdicionarRestricao((AdicionarRestricao) comando));
            traducao.put(AlterarColuna.class, comando -> traducaoAlterarColuna((AlterarColuna) comando));
            traducao.put(Atualizar.class, comando -> traducaoAtualizar((Atualizar) comando));
            traducao.put(CriarTabela.class, comando -> traducaoCriarTabela((CriarTabela) comando));
            traducao.put(Deletar.class, comando -> traducaoDeletar((Deletar) comando));
            traducao.put(EsvaziarDadosTabela.class, comando -> traducaoEsvaziarDadosTabela((EsvaziarDadosTabela) comando));
            traducao.put(Inserir.class, comando -> traducaoInserir((Inserir) comando));
            traducao.put(RemoverColuna.class, comando -> traducaoRemoverColuna((RemoverColuna) comando));
            traducao.put(RemoverRestricao.class, comando -> traducaoRemoverRestricao((RemoverRestricao) comando));
            traducao.put(RenomearColuna.class, comando -> traducaoRenomearColuna((RenomearColuna)comando));
            traducao.put(RenomearRestricao.class, comando -> traducaoRenomearRestricao((RenomearRestricao) comando));
            traducao.put(RenomearTabela.class, comando -> traducaoRenomearTabela((RenomearTabela)comando )); // ainda não fiz esse método em /comandos/
            traducao.put(Selecionar.class, comando -> traducaoSelecionar((Selecionar) comando));
            traducao.put(ExcluirTabela.class, comando -> traducaoExcluirTabela((ExcluirTabela) comando));
            traducao.put(ExcluirBancoDados.class, comando -> traducaoExcluirBancoDados((ExcluirBancoDados) comando)); 
            traducao.put(MostrarTabela.class, comando -> traducaoMostrarTabela((MostrarTabela) comando));
            traducao.put(MostrarColuna.class, comando -> traducaoMostrarColuna((MostrarColuna) comando));
        }
        public String traducao(Comando comando){
            Function<Comando, String> funcao = traducao.get(comando.getClass());
            if(funcao == null) {
            	throw new IllegalArgumentException("Comando não encontrado: " + comando.getClass().getSimpleName());
            }
            return funcao.apply(comando);
        
        }
        private String traducaoAdicionarColuna(AdicionarColuna coluna) {
            banco = new StringBuilder("Alter Table ");
            banco.append(coluna.getTabela());
            banco.append(" Add Column ");
            banco.append(traducaoDefinicaoColuna(coluna.getNovaColuna()));
            banco.append(";");
            return banco.toString();
        }
        private String traducaoAdicionarRestricao(AdicionarRestricao restricao) {
        	String tipoSql = tipo_restricao.get(restricao.getDefinicao().toLowerCase());
        		if(tipoSql == null) {
        			throw new IllegalArgumentException("Tipo de restrição não conhecida: " + restricao.getDefinicao());
        		}
        	banco = new StringBuilder("Alter Table ");
        	banco.append(restricao.getTabela());
        	banco.append(" Add Constraint ");
        	banco.append(restricao.getNomeRestricao());
        	banco.append(" ");
        	banco.append(tipoSql);
        	banco.append(" (").append(restricao.getColuna()).append(")");
        	if(restricao.getTabelaReferenciada() != null) {
        		banco.append(" References ");
        		banco.append(restricao.getColunaReferenciada());
        		banco.append("(").append(restricao.getColunaReferenciada()).append(")");
        	}
        	banco.append(";");
        	return banco.toString();
        }
        private String traducaoAtualizar(Atualizar atualizar) {
        	banco = new StringBuilder("Update ");
        	banco.append(atualizar.getTabela());
        	banco.append(" Set ");
        	List<String> campos = atualizar.getCampos().entrySet().stream()
        			.map(entrada -> entrada.getKey() + " = " + entrada.getValue())
        			.collect(Collectors.toList());
        	banco.append(String.join(", ", campos));
        	if (atualizar.getCondicao() != null && !atualizar.getCondicao().isEmpty()) {
        		banco.append(" where ").append(traducaoCondicao(atualizar.getCondicao()));
        	}
        	banco.append(";");
        	return banco.toString();
        }
        private String traducaoAlterarColuna(AlterarColuna coluna) {
        	String tipoSql = tipo_dados.get(coluna.getNovoTipoDado().toLowerCase());
        	if(tipoSql == null) {
        		throw new IllegalArgumentException("Tipo de dado não encontrado: " + coluna.getNovoTipoDado());
        	}
        	banco = new StringBuilder("Alter Table");
        	banco.append(coluna.getTabela());
        	banco.append(" Alter Column ");
        	banco.append(coluna.getNomeColuna());
        	banco.append(" ");
        	banco.append(tipoSql);
        	banco.append(";");
        	return banco.toString();
        }
        private String traducaoDeletar(Deletar deletar) {
        	banco = new StringBuilder("Delete From ");
        	banco.append(deletar.getTabela());
        	if(deletar.getCondicao() != null && !deletar.getCondicao().isEmpty()){
        		banco.append(" where ").append(traducaoCondicao(deletar.getCondicao()));
        	}
        	banco.append(";");
        	return banco.toString();
        }
        private String traducaoEsvaziarDadosTabela (EsvaziarDadosTabela esvaziar) {
        	banco = new StringBuilder("Truncate Table ");
        	banco.append(esvaziar.getTabela());
        	banco.append(";");
        	return banco.toString();
        }
        private String traducaoExcluirTabela(ExcluirTabela excluir) {
        	banco = new StringBuilder("Drop Table ");
        	banco.append(excluir.getNomeTabela());
        	banco.append(";");
        	return banco.toString();
        }
        private String traducaoExcluirBancoDados(ExcluirBancoDados excluir) {
        	throw new IllegalStateException("Esse comando não pode ser executado");        	
        }
        private String traducaoInserir(Inserir inserir) {
        	banco = new StringBuilder("Insert Into ");
        	banco.append(inserir.getTabela());
        	if(inserir.getColunas() != null && !inserir.getColunas().isEmpty()) {
        		banco.append(" (").append(String.join(", ", inserir.getColunas())).append(")");
        	}
        	banco.append(" Values (");
        	banco.append(String.join(", ", inserir.getValores()));
        	banco.append(");");
        	return banco.toString();
        }
        private String traducaoMostrarTabela(MostrarTabela mostrar) {
        	banco = new StringBuilder("Show Columns From ");
        	banco.append(mostrar.getNomeTabela());
        	banco.append(";");
        	return banco.toString();
        }
        private String traducaoMostrarColuna(MostrarColuna mostrar) {
        	banco = new StringBuilder("Select * From Information_Schema.Columns Where Table_Name = '");
        	banco.append(mostrar.getNomeTabela().toUpperCase());
        	banco.append(" ' And Column_Name = '");
        	banco.append(mostrar.getNomeColuna().toUpperCase());
        	banco.append("';");
        	return banco.toString();
        }
        private String traducaoRemoverColuna(RemoverColuna remover) {
        	banco = new StringBuilder("Alter Table ");
        	banco.append(remover.getTabela());
        	banco.append("Drop Column ");
        	banco.append(remover.getNomeColuna());
        	banco.append(";");
        	return banco.toString();
        }
        private String traducaoRemoverRestricao(RemoverRestricao remover) {
        	banco = new StringBuilder("Alter Table ");
        	banco.append(remover.getTabela());
        	banco.append(" Drop Constraint ");
        	banco.append(remover.getNomeRestricao());
        	banco.append(";");
        	return banco.toString();
        }

        private String traducaoRenomearColuna(RenomearColuna renomear) {
        	banco = new StringBuilder("Alter table ");
        	banco.append(renomear.getTabela());
        	banco.append(" Rename Column ");
        	banco.append(renomear.getNomeAntigo());
        	banco.append(" To ");
        	banco.append(renomear.getNomeNovo());
        	banco.append(";");
        	return banco.toString();
        }
        private String traducaoRenomearRestricao(RenomearRestricao renomear) {
        	banco = new StringBuilder("Alter Table ");
        	banco.append(renomear.getTabela());
        	banco.append(" Rename Constraint ");
        	banco.append(renomear.getNomeAntigo());
        	banco.append(" To ");
        	banco.append(renomear.getNomeNovo());
        	banco.append(";");
        	
        	return banco.toString();
        }
        private String traducaoRenomearTabela(RenomearTabela renomear) {
        	banco = new StringBuilder("Alter Table ");
        	banco.append(renomear.getNomeAntigo());
        	banco.append(" Rename To ");
        	banco.append(renomear.getNomeNovo());
        	banco.append(";");
        	return banco.toString();
        }


        
        
        
        
        private String traducaoSelecionar(Selecionar selecionar){
            banco = new StringBuilder("Select ");
            banco.append(String.join(", ", selecionar.getColunas()));
            banco.append(" from ").append(selecionar.getTabela());
            
            if (selecionar.getCondicao() != null && !selecionar.getCondicao().isEmpty()) {
                banco.append(" where ").append(traducaoCondicao(selecionar.getCondicao()));
            }
            banco.append(";");
            return banco.toString();
        }
        
        private String traducaoCondicao(String condicao){
            String resultado = condicao;
            resultado = resultado.replaceAll("(?i)\\bE\\b", "AND");
            resultado = resultado.replaceAll("(?i)\\bOU\\b", "OR");
            resultado = resultado.replaceAll("(?i)\\bNAO\\b", "NOT");
            resultado = resultado.replaceAll("(?i)PARECIDO COM", "LIKE");
            return resultado;
        }

        	
        
        
        private String traducaoCriarTabela(CriarTabela criartabela) {
            banco = new StringBuilder("Create Table ");
            banco.append(criartabela.getNomeTabela()).append(" (\n");
            List<String> definicoes = criartabela.getColunas().stream()
                    .map(this::traducaoDefinicaoColuna)
                    .collect(Collectors.toList());
            banco.append(String.join(",\n", definicoes));
            banco.append("\n);");
            
            return banco.toString();
        }
        private String traducaoDefinicaoColuna(DefinicaoColuna coluna){
            String tipoSql = tipo_dados.get(coluna.getTipoDado().toLowerCase());
            if (tipoSql == null) {
                throw new IllegalArgumentException(
                "Tipo de dado não encontradi" + coluna.getTipoDado());
            }
            StringBuilder definicao = new StringBuilder("    ")
                    .append(coluna.getNome())
                    .append(" ")
                    .append(tipoSql);
            if (coluna.isChavePrimaria()) {
                definicao.append(" Primary Key");
            }
            return definicao.toString();
        }

}

