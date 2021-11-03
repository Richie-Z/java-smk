/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pewarisan;

/**
 *
 * @author Richie
 */
public class Dekan extends Dosen{
    private String fakultas;

    public Dekan(String namaX, String nikX, String jurX, String fakX) {
        super(namaX, nikX, jurX);
        fakultas = fakX;
    }
    public void viewDekan(){
        System.out.println("Fakultas            :"+fakultas);
    }
    
    
}
