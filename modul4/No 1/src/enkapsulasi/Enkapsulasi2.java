/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package enkapsulasi;

/**
 *
 * @author Richie
 */
public class Enkapsulasi2 {
    public static void main(String[]  args){
        Enkapsulasi Richie = new Enkapsulasi("Immanuel Richie Dehardjo Zakaria","Richie","123456789","XI RPL 1","Filipina,11-03-03","zakariarichie@yahoo.com","089233214");
        System.out.println("Nama Lengkap \t     :" + Richie.tampilkanNama());
        System.out.println("Nama Panggilan \t     :"  + Richie.tampilkanNamaPangilan());
        System.out.println("NIS \t             :"  + Richie.tampilkanNIS());
        System.out.println("Jurusan \t     :" + Richie.tampilkanJurusan());
        System.out.println("Tempat Tanggal Lahir :" + Richie.tampilkanTTL());
        System.out.println("Email \t             :" + Richie.tampilkanEmail());
        System.out.println("Nomor HP \t     :"  + Richie.tampilkanNomerHP());
    }
    
}
