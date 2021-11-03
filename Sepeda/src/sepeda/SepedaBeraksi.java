/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sepeda;

/**
 *
 * @author Richie
 */
public class SepedaBeraksi {
    public static void main (String[] args){
        Sepeda sepedaku= new Sepeda();
        sepedaku.kecepatan=10;
        sepedaku.gir=2;
        sepedaku.tambahKecepatan(30);
        sepedaku.ubahGir(3);
    }
    
}
