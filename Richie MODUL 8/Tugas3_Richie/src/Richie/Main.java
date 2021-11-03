/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Richie;

/**
 *
 * @author Richie
 */
public class Main {
    public static void main(String[] args){
        Pengacara pcr = new Pengacara();
        Dokter dkr = new Dokter();
        Itsupport its = new Itsupport ();
        Arsitektur ast = new Arsitektur ();
        
        System.out.println("==============================================================================================================");
        System.out.println("Nama                    : Immanuel Richie D. Z.");
        System.out.println("Tugas                   : Nomer 3");
        System.out.println("Jurusan/Kelas           : RPL 1 / XI");
        System.out.println("==============================================================================================================");
        
        System.out.print("Nama Bidang Keahlian    :");
        pcr.bidangkeahlian();
        System.out.print("Tugas                   :");
        pcr.tugas();
        System.out.print("Tanggung Jawab          :");
        pcr.tangungjawab();
        System.out.println("==============================================================================================================");
       
        System.out.print("Nama Bidang Keahlian    :");
        dkr.bidangkeahlian();
        System.out.print("Tugas                   :");
        dkr.tugas();
        System.out.print("Tanggung Jawab          :");
        dkr.tangungjawab();
        System.out.println("==============================================================================================================");
        
        System.out.print("Nama Bidang Keahlian    :");
        its.bidangkeahlian();
        System.out.print("Tugas                   :");
        its.tugas();
        System.out.print("Tanggung Jawab          :");
        its.tangungjawab();
        System.out.println("==============================================================================================================");
        
        System.out.print("Nama Bidang Keahlian    :");
        ast.bidangkeahlian();
        System.out.print("Tugas                   :");
        ast.tugas();
        System.out.print("Tanggung Jawab          :");
        ast.tangungjawab();
        System.out.println("==============================================================================================================");
    }
}
