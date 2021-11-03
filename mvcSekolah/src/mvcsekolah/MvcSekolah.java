/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mvcsekolah;

/**
 *
 * @author Richie-PC
 */
public class MvcSekolah {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        Siswa model = retriveSiswaFromDB();
        SiswaView view = new SiswaView();
        SiswaController controller = new SiswaController(model,view);
        controller.updateView();
        controller.setSiswaNama("Zakaria");
        controller.updateView();
    }

    private static Siswa retriveSiswaFromDB() {
       Siswa Siswa = new Siswa();
       Siswa.setNama("Richie");
       Siswa.setNoDaftar("100");
       return Siswa;
    }
    
}
