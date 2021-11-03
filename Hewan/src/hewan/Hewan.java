/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hewan;

/**
 *
 *
 */
public class Hewan {
      public static void main(String[] args) {
          Kucing bb = new Kucing("Persia","Oren");
          Kelinci aa = new Kelinci("Anggora","Putih");
         
          Sapi cc = new Sapi("Jersey","Coklat");
          
          System.out.println("              Program NetBeans");
          System.out.println("                Fere Iswanda");
          System.out.println("==========================================");
          bb.viewKucing();
          System.out.println("");
          aa.view();
          System.out.println("");
          cc.viewSapi();
          System.out.println("");
          System.out.println("Ini Adalah Jenis-Jenis Binatang");
    }
      
      
}
