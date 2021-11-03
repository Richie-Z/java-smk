/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package menggunakan_stringbuilder;

/**
 *
 * @author Richie
 */
public class Menggunakan_StringBuilder {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String str1 = "abcde";
        StringBuilder str1z = new StringBuilder (str1);
        str1z.setCharAt(3,'x');
        System.out.println(str1z);
    }
    
}
