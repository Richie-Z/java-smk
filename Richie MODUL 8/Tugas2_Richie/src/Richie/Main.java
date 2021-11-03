/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Richie;

/**
 *
 * @author Richie
 */
public class Main {
    public static void main(String[] args){
        Bunglon bl = new Bunglon ();
        Ular ul = new Ular ();
        Cumi cm = new Cumi ();
        Cicak ck = new Cicak ();
        
        System.out.print("Cara Bertahan Hidup Bunglon     :");
        bl.caraBertahan();
        System.out.print("Cara Bertahan Hidup Ular        :");
        ul.caraBertahan();
        System.out.print("Cara Bertahan Hidup Cumi-cumi   :");
        cm.caraBertahan();
        System.out.print("Cara Bertahan Hidup Cicak       :");
        ck.caraBertahan();
    }
}
