/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package program.pkg09.pkgfor;

/**
 *
 * @author Richie
 */
public class Program09For {

    public static void main(String[] args) {
        int x=5;
        for (int i=1;i<=5;i++) {
            for (int j=4;j>=i;j--) {
                System.out.println(" ");
            }
            for (int k=1;k<=i;k++) {
                System.out.println("*");
            }
            System.out.println();
        }
    }
    
}
