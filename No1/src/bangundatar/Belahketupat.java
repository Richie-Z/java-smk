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
public class Belahketupat extends Bangundatar{
    float sisi;
    float diagonal1;
    float diagonal2;
    
    float luas(){
        float luas = diagonal1 * diagonal2 * 1/2;
        System.out.println("Luas Belah Ketupat       :"+luas);
        return luas;
    }
    float keliling(){
        float keliling = 4*sisi;
        System.out.println("Keliling Belah Ketupat   :"+keliling);
        System.out.println("");
        return keliling;
    }
    
}
