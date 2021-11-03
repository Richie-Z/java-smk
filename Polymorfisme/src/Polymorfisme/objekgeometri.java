/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Polymorfisme;

/**
 *
 * @author Richie
 */
public class objekgeometri {
    private String warna = "putih";
    private boolean terisi;
    private java.util.Date tanggalDiciptakan;
    //menciptakan suatu objek geometri dengan warna dan nilai terisi tertentu 
    public objekgeometri(){
        tanggalDiciptakan = new java.util.Date();
    //menciptakan suatu objek geometri dengan warna dan nilai terisi terentu 
     }
    public objekgeometri (String warna, boolean terisi){
        tanggalDiciptakan = new java.util.Date();
        this.warna = warna;
        this.terisi = terisi;
    //mengambil warna
    }
    public String dapatwarna(){
        return warna; 
    //menetapkan suautu warna
    }
    public void dapatwarna(String warna){
        this.warna = warna ;
    /*Mengmbalikan terisi karena terisi adalah suatu boolean 
        metode dapat dinamai apaTerisi
        */
    }
    public boolean apaTerisi(){
        return terisi;
        //menetapkan suatu nilail terisi yang baru
    }
    public void tetapkanTerisi (boolean terisi){
         this.terisi = terisi;
         //menetapkpan suatu nilai terisi yang baru
    }
    public java.util.Date dapattanggaldiciptakan(){
        return tanggalDiciptakan;
        //mengembalikan suatu representasi string atas objek ini
    }
    public String keString(){
        return "diciptakan pada " + tanggalDiciptakan + "\nwarna : " + warna +
               "dan nilai terisi :" + terisi;
    }
}
