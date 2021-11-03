/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Immanuel_Richie;

/**
 *
 * @author Richie
 */
public class Stringbufferku {
    public static void main(String[] args){
        StringBuffer sb = new StringBuffer();
        String kata="ILMU KOMPUTER";
        sb.append("PRODI ").append(kata).append(" UGM");
        System.out.println(sb.toString());
        System.out.println(sb);
    }
}
