/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package perbandingan;

public class Perbandingan {
    public static void main(String[] args) {
        int i = 75;
        int k = 90;
        //Lebih Besar Dari
        System.out.println("Lebih besar dari");
        System.out.println(" k > i = " + (k > i));//true
        //lebih besar atau sama dengan
        System.out.println("Lebih besar atau sama dengan");
        System.out.println(" k >= i = " + (k >= i));//true
        //lebih kecil dari
        System.out.println("lebih kecil dari");
        System.out.println(" k < i = " + (k < i));//false
        //lebih kecil atau sama dengan
        System.out.println("lebih kecil atau sama dengan");
        System.out.println(" k <= i = " + (k <= i));//false
        //sama dengan
        System.out.println("sama dengan");
        System.out.println(" k == i = " + (k == i));//false
        //tidak sama dengan
        System.out.println("tidak sama dengan");
        System.out.println(" k != i = " + (k != i));//true
    }
    
}
