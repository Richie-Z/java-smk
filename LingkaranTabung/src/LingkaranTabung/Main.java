/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package LingkaranTabung;

/**
 *
 *
 */
public class Main {
    public static void main (String[] args){
        Lingkaran aa = new Lingkaran();
        aa.r= 8;
        
        Tabung bb = new Tabung();
        bb.r= 9;
        bb.t=  10;
        
        aa.luas();
        aa.keliling();
        bb.Volume();
        bb.Luas_Permukaan();
    }
}
