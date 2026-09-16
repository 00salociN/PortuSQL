/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Conexao;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Etec
 */
public class Conexao {
    private static final String pasta = System.getProperty("user.home") + File.separator + ".portusql";
    private static final String caminho = pasta + File.separator + "banco";
    private static final String URL_JDBC = "jdbc:h2:" + caminho;
    private static final String USER = "portusql";
    private static final String PASSWORD = "";
    private static Connection conexao;
    
        public static Connection obterConexao() throws SQLException{
            if(conexao == null || conexao.isClosed()){
                new File(pasta).mkdirs();
                conexao = DriverManager.getConnection(URL_JDBC, USER, PASSWORD);
            }
            return conexao;
        }
        public static final void fecharConexao() throws SQLException{
            if(conexao != null && !conexao.isClosed()){
                conexao.close();
            }
        }
    }
    

