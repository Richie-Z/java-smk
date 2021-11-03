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
public class Stafthdr extends Direktur{
    public void tampil(){
        Direktur dir = new Direktur();
        dir.input("Eni","22","Staf HRD","Rp. 2.500.000,00","-","-");
        dir.cetak();
    }
}
