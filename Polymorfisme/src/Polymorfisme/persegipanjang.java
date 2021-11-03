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
public class persegipanjang extends objekgeometri {
    private double Lebar;
    private double Tinggi;
    
    public persegipanjang(){
        
    }
    public persegipanjang(double Lebar,double Tinggi){
        this.Lebar = Lebar;
        this.Tinggi = Tinggi;
    }
    public persegipanjang (double Lebar, double Tinggi, String warna, boolean terisi){
        this.Lebar = Lebar;
        this.Tinggi = Tinggi;
        dapatwarna(warna);
        tetapkanTerisi(terisi);
    }
    public double dapatLebar(){
        return Lebar;
    }
    public void dapatLebar (double Lebar){
        this.Lebar = Lebar;
    }
    public double dapatTinggi(){
        return Tinggi;
    }
    public void dapatTinggi (double Tinggi){
        this.Tinggi= Tinggi;
    }
    public double dapatLuas(){
        return Lebar * Tinggi;
    }
    public double dapatKeliling(){
        return 2 * (Lebar + Tinggi);
}
}