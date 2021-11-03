/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Kode;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.JOptionPane;
/**
 *
 * @author Richie-PC
 */
public class Koneksi {
    private ResultSet rs;
    private Connection con;
    private Statement st;
    
    public Koneksi(){
        try{
            Class.forName("com.mysql.jdbc.Driver");
        }catch(Exception E){
            JOptionPane.showMessageDialog(null,"Error instalasi Driver:"+ E);
        }
        try{
            st = DriverManager.getConnection("jdbc:mysql://localhost/dbguestbook","root","").createStatement();
        }catch(Exception E){
            JOptionPane.showMessageDialog(null,"Error koneksi ke DB:"+ E);   
        }
    }
    public void insertData(String data){
        try{
            st.executeUpdate(data);
        }catch(Exception E){
            JOptionPane.showMessageDialog(null,"Gagal Insert data:"+ E);   
        }
    }
    public ResultSet lihatData(String a){
        try{
           rs =  st.executeQuery(a);
        }catch(Exception E){
            JOptionPane.showMessageDialog(null,"Gagal Lihat data:"+ E);   
        }
        return rs;
    }
}
