/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hewan;

/**
 *
 *
 */
public class Sapi extends Kelinci{
    
    public Sapi(String Jeniss, String Warnaa) {
        super(Jeniss, Warnaa);
    }
    public void viewSapi (){
        System.out.println("Jenis Sapi       : "+Jenis);
        System.out.println("Warna            : "+Warna);
}
}
