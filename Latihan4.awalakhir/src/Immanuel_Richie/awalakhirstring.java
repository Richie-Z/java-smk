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
public class awalakhirstring {
    public static void main(String[] args){
        String str1 = "Ilmu Komputer Matematika dan Ilmu"
            + "Pengetahuan Alam Universitas Gadjah Mada";
        System.out.println(str1.startsWith("Ilmu"));
        System.out.println(str1.endsWith("Ilmu"));
        System.out.println(str1.startsWith("lmu",1));
        System.out.println(str1.startsWith("lmu",2));
        System.out.println(str1.startsWith("lmu",3));
        System.out.println(str1.startsWith("Komp",7));
        System.out.println(str1.startsWith("Komp",6));
        System.out.println(str1.startsWith("Komp",5));
        System.out.println(str1.endsWith("Mada"));
        System.out.println(str1.startsWith("Mada"));
    }
}