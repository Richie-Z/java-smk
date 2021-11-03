/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg3.linkedlist;

/**
 *
 * @author Richie
 */
import java.util.ArrayList;
import java.util.List;
public class Linkedlist {
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        List warna = new ArrayList();
        warna.add("Magenta");
        warna.add("Red");
        warna.add("White");
        warna.add("Blue");
        warna.add("Cyan");
        List warnaHapus = new ArrayList();
        warnaHapus.add("Red");
        warnaHapus.add("White");
        warnaHapus.add("Blue");
        System.out.println("Warna : "+warna);
        System.out.println("Warna Dihapus : "+warnaHapus);
        warna.removeAll(warnaHapus);
        System.out.println("Warna Sekarang : "+warna);
    }
}
