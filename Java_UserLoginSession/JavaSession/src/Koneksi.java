
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 *
 * @author www.kangsunu.web.id
 */
public class Koneksi {

    private Connection connection;

    public Connection getConnection() {
        return connection;
    }

    public void dbConnection() { //<-- untuk koneksi ke database  
        // Cek Driver  
        try {
            Class.forName("com.mysql.jdbc.Driver"); //<-- nama driver untuk koneksi ke MySQL  

            // Cek Database  
            try {
                String url, user, password;
                url = "jdbc:mysql://localhost/db_datasiswa"; //alamat DB  
                user = "root";
                password = "";
                connection = DriverManager.getConnection(url, user, password);

                System.out.println("Koneksi Sukses");
            } catch (SQLException se) {
                JOptionPane.showMessageDialog(null, "Koneksi Gagal! " + se);
                System.exit(0);
            }
        } catch (ClassNotFoundException cnfe) {
            JOptionPane.showMessageDialog(null, "Driver Tidak Ditemukan!" + cnfe);
            System.exit(0);
        }
    }

    public static void main(String[] kon) {
        new Koneksi().dbConnection();
    }
}
