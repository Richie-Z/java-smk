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
    public void cekMakhlukHidup (MakhlukHidup mHidup){
        mHidup.berdiri();
        mHidup.oksigen();
    }
    public static void main(String[] args){
        System.out.println("============================================================");
        Main MH = new Main();
        MH.cekMakhlukHidup (new Manusia("Dua Kaki"));
        System.out.println("============================================================");
        MH.cekMakhlukHidup (new Hewan("Empat Kaki","Dua Kaki"));
        System.out.println("============================================================");
        MH.cekMakhlukHidup (new Tumbuhan("Akar"));
        System.out.println("============================================================");
        System.out.println("Nama              : Immanuel Richie D. Z.");
        System.out.println("Jurusan/Kelas     : RPL 1 / XI");
        System.out.println("============================================================");
    }
    
}
