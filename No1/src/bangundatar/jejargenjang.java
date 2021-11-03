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
public class jejargenjang extends Belahketupat {
    float alas;
    float tinggi;
    
     float luas(){
        float luas = alas * tinggi;
        System.out.println("Luas Jejar Genjang        :"+luas);
        return luas;
    }
}
