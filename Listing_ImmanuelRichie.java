/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Richie
 */
import java.util.Scanner;
public class Listing_ImmanuelRichie {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        String nama, nis, kls, jur; 
        System.out.println("ISILAH DATA BERIKUT INI, DAN AKHIRI DENGAN ENTER ..............");
        
        System.out.print("Nama Anda\t\t: " );
        nama = input.nextLine();
        System.out.print("NIS\t\t\t: " );
        nis = input.nextLine();
        System.out.print("Kelas\t\t\t: " );
        kls = input.nextLine();
        System.out.print("Kompetensi Keahlian\t: " );
        jur = input.nextLine();

        System.out.println("");

        System.out.println("===============================================");
        System.out.println("=================BIODATA SISWA=================");
        System.out.println("===============================================");
        System.out.println("Nama Anda\t\t: " + nama);
        System.out.println("NIS\t\t\t: " + nis);
        System.out.println("Kelas\t\t\t: " + kls );
        System.out.println("Kompetensi Keahlian\t: " + jur);
    }
    
}
