/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package banco;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class CriaBancoDados {

    private String driver;
    private String url;
    private String user;
    private String senha;
    private String database;

    public CriaBancoDados() {
        this.driver = "com.mysql.cj.jdbc.Driver";
        this.url = "jdbc:mysql://localhost:3306/";
        this.user = "root";
        this.senha = "";
        this.database = "passagens_aereas";
    }

    
    //Verifica se o banco já existe e cria-lo caso não//
    public boolean makeDatabase() {
        try {
            Class.forName(driver);
            Connection conexao = DriverManager.getConnection(url,
                    user, senha);
            Statement sessao = conexao.createStatement();
            ResultSet s = sessao.executeQuery("SELECT COUNT(*) FROM information_schema.tables WHERE table_schema LIKE 'passagens_aereas'");
            s.next();
            boolean exists = s.getInt("COUNT(*)") > 0;
            if (!exists) {
                String sql = "CREATE DATABASE " + database;
                sessao.executeUpdate(sql);
                return false;
            }
        } catch (ClassNotFoundException ex) {
            System.out.println("Driver errado: " + driver);
            return false;
        } catch (SQLException ex) {
            System.out.println("Erro sql exception " + ex.getMessage());
            return false;
        }
        return true;
    }

    //cria o schema do banco//
    public void makeTables() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader("Banco.txt"));
        String line;
        String sql = "";
        while ((line = reader.readLine()) != null) {
            sql += line + "\n";
        }
        System.out.println(sql);
        try {
            Class.forName(driver);
            Connection conexao = DriverManager.getConnection(url + database,
                    user, senha);
            Statement sessao = conexao.createStatement();

            sessao.executeUpdate(sql);
            System.out.println("Tabela " + database + " criado com sucesso!!!");
        } catch (ClassNotFoundException ex) {
            System.out.println("Driver errado: " + driver);
        } catch (SQLException ex) {
            System.out.println("Erro sql exception " + ex.getMessage());
        }
    }
}
