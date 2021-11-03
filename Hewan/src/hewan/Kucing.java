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
public class Kucing extends Kelinci {
    
    public Kucing(String Jeniss, String Warnaa) {
        super(Jeniss, Warnaa);
    }
    public void viewKucing (){
        System.out.println("Jenis Kucing     : "+Jenis);
        System.out.println("Warna            : "+Warna);
}
    
}
