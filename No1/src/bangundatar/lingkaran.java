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
public class lingkaran extends Bangundatar {
    float r;
    
    float luas(){
        float luas = (float) Math.PI * r *r;
        System.out.println("Luas Lingkaran           :"+luas);
        return luas;
    }
     float keliling(){
        float keliling = (float) (2 * Math.PI * r);
        System.out.println("Keliling lingkaran       :"+keliling);
        System.out.println("");
        return keliling;
    }
}
