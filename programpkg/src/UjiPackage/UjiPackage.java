/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UjiPackage;
import Pegawai.Pegawai;
import Pegawai.Tunjangan;
import honor.Honor;
public class UjiPackage {
     public static void main (String[] args){
        Pegawai objekpegawai = new Pegawai();
        objekpegawai.Gol("3A");
        objekpegawai.masa("IMMANUEL RICHIE D. Z.");
        objekpegawai.Gjpokok("ANDRI WIRAYANDI");
        Tunjangan objektunjangan = new Tunjangan();
        objektunjangan.Tunjkel("GALIH BAGUS");
        objektunjangan.Tunjbt("GALIH BAGUS");
        Honor objekhonor = new Honor();
        objekhonor.HonorTetap("JONATHAN A.S.J");
     
     }
    
}
