/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package menggabungkansubstring;

/**
 *
 * @author Richie
 */
public class MenggabungkanSubString {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String str1 = "abcde";
        String str1x = str1.substring(0,3);
        System.out.println(str1x);
        String str1y = str1.substring(0,3)+'x'+str1.substring(4);
        System.out.println(str1y);
    }
    
}
