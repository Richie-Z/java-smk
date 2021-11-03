/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mobilLengkap;

/**
 *
 * @author Richie
 */
public class MobilBeraksi {
    public static void main (String[] args){
        MobilLengkap mobil = new MobilLengkap ();
        mobil.warna = "Hitam";
        mobil.TahunProduksi = 2006;
        mobil.hidupkanMobil ="Hidup";
        mobil.matikanMobil =" Mati";
        mobil.ubahGigi="";
        
        mobil.printMobil();
    }
    
    
}
