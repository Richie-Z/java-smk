/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package menggunakan_char_replace;

/**
 *
 * @author Richie
 */
public class Menggunakan_char_replace {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String test = "abcde";
        String hasil = replace(test,3, 'x');
        System.out.println(hasil);
    }
    private static String replace(String str,int index,char replace){
        char[] charx = str.toCharArray();
        charx[index] = replace;
        return String.valueOf(charx);
    }
    
}
