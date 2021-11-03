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
public class Hewan extends MakhlukHidup {
    private String EmpatKaki, DuaKaki;
    public Hewan(String EmpatKaki, String DuaKaki){
        this.EmpatKaki = EmpatKaki;
        this.DuaKaki = DuaKaki;
    }
    public void berdiri(){
        System.out.println("Hewan berdiri dengan "
                +EmpatKaki+" dan berdiri dengan "+DuaKaki);
    }
}
