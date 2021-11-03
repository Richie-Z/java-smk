/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package luas_persegi_panjang;

/**
 *
 * @author Richie-PC
 */
public class View {
    public void printLuasDetails(int NilPanjang, int NilLebar , int NilLuas){
        NilLuas = NilPanjang * NilLebar;
        System.out.println("Luas Persegi Panjang = " +NilLuas);
    }
}
