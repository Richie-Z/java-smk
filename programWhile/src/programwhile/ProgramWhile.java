/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package programwhile;

/**
 *
 * @author Richie
 */
public class ProgramWhile {

   
    public static void main(String[] args) {
        int bilangan = 5;
        int x = 2;
        int hasil = 1;
        while (x<=bilangan){
            System.out.println(hasil + " * " + " = ");
            hasil = hasil*x;
            System.out.println(hasil);
            x++;
                    System.out.println(bilangan + " != " + hasil);
            
        }
    }
    
}
