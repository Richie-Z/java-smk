/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Immanuel_Richie;

/**
 *
 * @author Richie
 */
public class posisikarakter {
    public static void main(String[] args){
         String str1 = "Ilmu Komputer Matematika dan Ilmu"
            + "Pengetahuan Alam Universitas Gadjah Mada";
        System.out.println(str1.indexOf("Komputer"));
        System.out.println(str1.indexOf("Komputer",10));
        System.out.println(str1.lastIndexOf("Matematika"));
        System.out.println(str1.lastIndexOf("Matematika",10));
        System.out.println(str1.lastIndexOf("Matematika",20));
        System.out.println(str1.indexOf(97));
        System.out.println(str1.indexOf(97,7));
        System.out.println(str1.lastIndexOf(97));
        System.out.println(str1.lastIndexOf(97,7)); 
    }
}
