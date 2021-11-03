/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package programmatematika;
import java.util.Scanner;
/**
 *
 * @author CC
 */
public class ProgramMatematika {
    public static void main(String[] args) {
int pilih, menu=0;
Scanner input=new Scanner(System.in);
System.out.println("\n====>MENU PROGRAM<====");
System.out.println("0. MENU UTAMA");
System.out.println("1. MENGHITUNG VOLUME KUBUS");
System.out.println("2. MENGHITUNG VOLUME BOLA");
System.out.println("3. EXIT (KELUAR)" );
System.out.print("\nMasukkan Pilihan Anda (1/2/3): ");pilih=input.nextInt();
System.out.print("====================================");
do {
switch(pilih)
        {
case 0: System.out.println("\n====>MENU PROGRAM<====");
        System.out.println("0. MENU UTAMA");
        System.out.println("1. MENGHITUNG VOLUME KUBUS");
        System.out.println("2. MENGHITUNG VOLUME BOLA");
        System.out.println("3. EXIT (KELUAR)" );
        System.out.print("\nMasukkan Pilihan Anda (1/2/3) : ");pilih=input.nextInt();
        System.out.print("=========================================");
case 1: System.out.print("\n1. VOLUME KUBUS");
        System.out.println ("\nRUMUS VOLUME KUBUS = SISI X SISI X SISI");
        System.out.print("=========================================");
        float sisi;
        System.out.print("\n");
        System.out.print(" => Masukan Nilai Sisi\t: ");
        sisi = input.nextFloat();
        float volume = sisi*sisi*sisi;
        System.out.print("==========================================");

System.out.println("\n => Volume Kubus \t: " + volume);
        System.out.print("==========================================");
        break;
case 2: System.out.print("\n2. VOLUME BOLA");
    System.out.println("\nRUMUS VOLUME BOLA = 4/3 X 3.14 X Jari X Jari");
        System.out.print("======================================== ");
        float jari2;
        System.out.print("\n");
        System.out.print(" => Masukan Nilai Jari-Jarii\t: ");
        jari2 = input.nextFloat();
        volume = (float) (4/3*3.14*jari2*jari2*jari2);
        System.out.print("=========================================");
        System.out.println("\n => Volume Bola \t: " + volume);
        System.out.print("=========================================");
        break;
case 3: System.exit(0);
        break;
        }
    System.out.print("\n"); 
    System.out.print("\n0. Kembali Ke Menu Utama" );
    System.out.print("\n3. EXIT (KELUAR)" );
    System.out.print("\n"); 
    System.out.print("\nMASUKAN PILIHAN \t= " );pilih=input.nextInt();
    System.out.print("==========================================");
   } while(pilih <= 3);
}
}
