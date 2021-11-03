/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg2.interface_list;

/**
 *
 * @author Richie
 */
import java.util.*;
public class Interface_list {
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        List nama = new ArrayList();
        nama.add ("Richie");
        nama.add ("Rohelio");
        nama.add ("Cadeck");
        nama.add ("Fere");
        System.out.println("Daftar Nama : "+nama);
        Collections.reverse(nama);
        System.out.println("Balik Nama : "+nama);
        Collections.shuffle(nama);
        System.out.println("Acak Nama : "+nama);
        Collections.sort(nama);
        System.out.println("Urutan Nama : "+nama);
    }
    
}
