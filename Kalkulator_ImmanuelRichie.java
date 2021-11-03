/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Richie
 */
import java.io.*;
public class Kalkulator_ImmanuelRichie {


    public static void main(String[] args) throws Exception {
        BufferedReader baca = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("Masukkan Bilangan ke-1 : ");
        double bil1 = Double.parseDouble(baca.readLine());
        System.out.print("Masukkan Bilangan ke-2 : ");
        double bil2 = Double.parseDouble(baca.readLine());
        do {
            double hasil = 0;
            System.out.println("==================================");
            System.out.println("=========Menu Kalkulator==========");
            System.out.println("==================================");
            System.out.println("1. Hitung Hasil Penjumlahan");
            System.out.println("2. Hitung Hasil Pengurangan");
            System.out.println("3. Hitung Hasil Perkalian");
            System.out.println("4. Hitung Hasil Pembagian");
            System.out.println("5. Keluar (Exit)");
            System.out.print("Masukkan No. Pilihan Menu : ");
            int nopil = Integer.parseInt(baca.readLine());
            switch(nopil) {
                case 1 :
                hasil = bil1 + bil2;
                System.out.println("Hasil Penjumlahan \t: " + hasil);
                break;
                case 2 :
                hasil = bil1 - bil2;
                System.out.println("Hasil Pengurangan \t: " + hasil);
                break;
                case 3 :
                hasil = bil1 * bil2;
                System.out.println("Hasil Perkalian \t: " + hasil);
                break;
                case 4 :
                hasil = bil1 / bil2;
                System.out.println("Hasil Pembagian \t: " + hasil);
                break; 
                case 5 :
                System.exit(0);
                break;
            }
        } while (true);
    }
}
    
