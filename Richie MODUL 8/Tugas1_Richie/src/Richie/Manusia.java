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
public class Manusia extends MakhlukHidup {
    private String DuaKaki;
    public Manusia(String DuaKaki){
        this.DuaKaki=DuaKaki;
    }
    public void berdiri(){
        System.out.println("Manusia berdiri dengan "+DuaKaki);
    }
}
