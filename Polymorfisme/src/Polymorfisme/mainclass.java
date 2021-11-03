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
public class mainclass {
    public static void main(String [] args){
        tampilObjek (new lingkaran(1, "merah", false));
        tampilObjek (new persegipanjang (1,1,"merah",true));
    }
    public static void tampilObjek(objekgeometri objek){
        System.out.println("Diciptakan pada "+ objek.dapattanggaldiciptakan()
                            +".Warna adalah "+objek.dapatwarna());
    }
}
