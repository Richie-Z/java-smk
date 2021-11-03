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
import java.util.Scanner;
public class InputOutput {
    public static void main(String[] args){
            
        Scanner inputUser = new Scanner(System.in);
        
        System.out.println("Masukan nama anda = ");
        String nama = inputUser.nextLine();
        
        System.out.println("Nama anda adalah = " + nama);
    }
}
