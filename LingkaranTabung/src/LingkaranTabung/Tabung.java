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
public class Tabung extends Lingkaran{
    float t;
    
 float Volume(){
        float Volume = (float) Math.PI * r *r *t;
        System.out.println("Volume Tabung            :"+Volume);
        return Volume;
    }
     float Luas_Permukaan(){
        float Luas_Permukaan = (float) (2 * Math.PI * r * r);
        System.out.println("Luas Permukaan Tabung    :"+Luas_Permukaan);
        return Luas_Permukaan;
     }
}

