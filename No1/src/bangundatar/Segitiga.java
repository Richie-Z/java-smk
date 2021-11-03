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
public class Segitiga extends Bangundatar{
    float alas;
    float tinggi;
    
    float luas(){
        float luas= alas * tinggi /2;
        System.out.println("Luas Segitiga            :"+luas);
        System.out.println("");
        return luas;
}
}
