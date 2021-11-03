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
public class Kalab extends Dosen {
    private String laboratorium;

    public Kalab(String namaX, String nikX, String jurX, String labX) {
        super(namaX, nikX, jurX);
        laboratorium = labX;
    }
    public void viewKalab (){
        System.out.println("Laboratorium        :"+laboratorium);
    }

    
}
