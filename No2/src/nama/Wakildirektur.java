/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nama;

/**
 *
 * @author Richie
 */
public class Wakildirektur extends Direktur{
    public void tampil(){
        Direktur dir = new Direktur();
        dir.input("Doni","25","Wakil Direktur","Rp. 7.000.000,00","Rp. 3.000.000,00","Mobil Dinas");
        dir.cetak();
    }
}
