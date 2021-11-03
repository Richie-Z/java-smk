/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package immanuelrichie;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import javax.swing.JOptionPane;
/**
 *
 * @author Richie-PC
 */
public class config {
    Connection con;
    Statement stm;
    private Connection koneksi;
    public Connection getKoneksi(){
        PreparedStatement pst =null;
        return koneksi;
    }
    
    public void config(){
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con=DriverManager.getConnection("jdbc:mysql://localhost/db_biodatasiswa","root","");
            stm= con.createStatement();
        }
        catch (Exception e) {
             JOptionPane.showMessageDialog(null,"koneksi gagal"+e.getMessage());
        }
    }
}
