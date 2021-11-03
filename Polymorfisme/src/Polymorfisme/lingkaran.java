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
public class lingkaran extends objekgeometri{
    private double radius;
    public lingkaran(){
        
    }
    public lingkaran(double radius){
        this.radius = radius;
    }
    public lingkaran (double radius, String warna, boolean terisi){
        this.radius = radius;
        tetapkanTerisi(terisi);
    }
    //mengembalikan radius
    public double dapatRadius(){
        return radius ;
    }
    public void tetapkanRadius(double radius){
        this.radius = radius;
    }
    public double dapatLuas(){
        return radius * radius * Math.PI;
    }
    public double dapatDiameter(){
        return 2* radius;
    }
    public double dapatKeliling(){
        return 2 * radius * Math.PI;
    }
    public void tampilLingkaran(){
        System.out.println("Lingkaran diciptakan pada"+ dapattanggaldiciptakan() +
                            "dan radiusnya adalah" + radius);
    }
}
