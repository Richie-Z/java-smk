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
public class Kelinci {
    public String Jenis;
    public String Warna;
    
    Kelinci (String Jeniss, String Warnaa){
        Jenis = Jeniss;
        Warna = Warnaa;
    }
    public void view (){
        System.out.println("Jenis Kelinci    : "+Jenis);
        System.out.println("Warna            : "+Warna);
}
}
