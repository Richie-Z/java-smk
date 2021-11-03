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
public class Tumbuhan extends MakhlukHidup {
    private String Akar;
    public Tumbuhan (String Akar){
        this.Akar = Akar;
    }
    public void berdiri(){
        System.out.println("Tumbuhan berdiri dengan "+Akar);
    }
    
}
