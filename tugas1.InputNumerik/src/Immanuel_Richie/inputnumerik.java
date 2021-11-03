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
import java.io.*;
public class inputnumerik {
    public static void main(String[] args) throws IOException {
        System.out.print("Masukan sebuah bilangan bulat: ");
        String temp;
        int bilangan= 0;
        InputStreamReader isr = new InputStreamReader (System.in);
        BufferedReader br = new BufferedReader(isr);
        temp = br.readLine();
        try {
            bilangan = Integer.parseInt(temp);
        } catch (NumberFormatException nfe) {
            System.out.println("Data yang dimasukan " + "bukan bilangan bulat"); System.exit(1);
        }
        System.out.println("Bilangan yang dimasukan "+ "adalah " + bilangan);
    }
}
