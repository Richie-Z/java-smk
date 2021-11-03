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
public class Stringarray {
    public static void main(String[] args){
        char[] arraystr={'I','L','M','U',' ','K','O','M','P','U','T','E','R'};
        System.out.println("Array Char");
    for (int i=0; i<arraystr.length; i++)
        {
            System.out.println(arraystr[i]);
        }
        System.out.println("String Baru: ");
        String str=String.copyValueOf(arraystr);
        System.out.println(str);
    }
}

