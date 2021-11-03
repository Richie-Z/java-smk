/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Immanuel_Richie;

/**
 *
 * @author Richie
 */
import javax.swing.JOptionPane;
public class Input_example {
    public static void main(String[] args) {
        String nama; //Variable String untuk menyimpan input
        //memunculkan pesan dialog serta menerima input dari user
        nama = JOptionPane.showInputDialog("Siapa Nama Kamu?");
        //Menampilkan Pesan kepada user
        JOptionPane.showMessageDialog(null,"Salam Kenal "+nama);
    }
}
