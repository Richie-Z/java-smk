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
public class Masukandatarichie {
     public static void main(String[] args){
            
        Scanner inputUser = new Scanner(System.in);
        
        System.out.print("Masukan nama anda = ");
        String nama = inputUser.nextLine();
        
        System.out.print("Masukan Alamat anda = ");
        String alamat = inputUser.nextLine();
        
        System.out.print("Masukan Nomer Telepon anda = ");
        String nomertelepon = inputUser.nextLine();
        
        System.out.print("Hallo " + nama + " alamatmu di " + alamat + " nomer teleponmu adalah "+ nomertelepon);

    }
}
