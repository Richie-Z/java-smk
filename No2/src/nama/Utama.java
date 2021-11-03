/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nama;

/**
 *
 * @author Richie
 */
public class Utama {
     public static void main (String[] args){
        Direktur dir = new Direktur();
        dir. tampil();
        System.out.println();
        Wakildirektur wd = new Wakildirektur();
        wd. tampil();
        System.out.println();
        Stafthdr st= new Stafthdr();
        st.tampil();
     }
}