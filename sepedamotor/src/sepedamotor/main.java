/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sepedamotor;

/**
 *
 * @author Richie
 */
class main{
public static void main(String[] args){
    System.out.println("Nama Anggota");
    mobil mobil = new mobil();
    Motor motor = new Motor();
    jonathan jo = new jonathan();
    System.out.print("1 : ");
    motor.tambahkecepatan();
    System.out.print("2 : ");
    motor.kurangikecepatan();
    System.out.print("3 : ");
    mobil.tambahkecepatan();
    System.out.print("4 : ");
    mobil.kurangikecepatan();
    System.out.print("5 : ");
    jo.tambahkecepatan();
}
}
