/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package luas_lingkaran;

/**
 *
 * @author Richie-PC
 */
public class Model {
    private int Diameter ;
    private double Luas;
    public int getDiameter () {
        return Diameter;
    }
    public void setDiameter (int D){
        this.Diameter = D;
    }
     public double getLuas () {
        return Luas;
    }
    public void setLuas (Double L){
        this.Luas = L;
    }
}
