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
public class persegpanjang extends Bangundatar{
    float Panjang;
    float Lebar;
    
   float luas(){
           float luas= Lebar * Panjang ;
           System.out.println("Luas Persegi Panjang     :"+luas);
           return luas;
   }
    float keliling(){
           float keliling= 2*Panjang+Lebar;
           System.out.println("Keliling Persegi Panjang :"+keliling);
           System.out.println("");
           return keliling;
    }
}

