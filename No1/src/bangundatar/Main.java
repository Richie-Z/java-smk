/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bangundatar;

/**
 *
 * @author Richie
 */
public class Main {
    public static void main (String[] args){
    Bangundatar aa = new Bangundatar();
    
    persegpanjang ax= new persegpanjang();
    ax.Lebar=2;
    ax.Panjang=9;
    
    Segitiga qa = new Segitiga();
    qa.alas=8;
    qa.tinggi=5;
    Belahketupat wq = new Belahketupat();
    wq.diagonal1=3;
    wq.diagonal2=6;
    wq.sisi=8;
    lingkaran ui = new lingkaran();
    ui.r=21;
    jejargenjang yt= new jejargenjang();
    yt.alas=5;
    yt.tinggi=4;
    
    aa.luas();
    aa.kelilig();
    ax.luas();
    ax.keliling();
    qa.luas();
    wq.luas();
    wq.keliling();
    ui.luas();
    ui.keliling();
    yt.luas();
    
    
}
}
