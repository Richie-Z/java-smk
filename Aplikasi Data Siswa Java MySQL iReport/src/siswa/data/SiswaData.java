/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package siswa.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author maful
 */
public class SiswaData {

    public SiswaData() {
        Connection con = null;
        try {
            String jdbcDriver = "com.mysql.jdbc.Driver";
            Class.forName(jdbcDriver);

            String url = "jdbc:mysql://localhost/data_siswa";
            String user = "root";
            String pass = "usbw";

            con = DriverManager.getConnection(url, user, pass);
            Statement stm = con.createStatement();

            try {
                Map<String, Object> prs = new HashMap<String, Object>();
                JasperReport JRpt = JasperCompileManager.compileReport("report1.jrxml");
                JasperPrint JPrint = JasperFillManager.fillReport(JRpt, prs, con);
                JasperViewer.viewReport(JPrint, false);
            } catch (Exception rptexcpt) {
                System.out.println("Report Can't view because : " + rptexcpt);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
}
    
}
