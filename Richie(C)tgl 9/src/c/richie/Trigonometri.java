/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package c.richie;

/**
 *
 * @author Richie
 */
public class Trigonometri {
     public static void main(String[] args) {
         float a1 = 8;
         float a2 = 10;
         float sudut1 = 30;//01
         float sudut2 = 30;//02
         float x1,x2,y1,y2;
         /*rumusnya sebagai berikut :
         x1 = a1 * cos(01)
         y1 = a1 * sin(01)
         x2= a1 * cos(01)+a2*cos(01+02)
         y2= a1 * sin(01)+a2*sin(01+02)
         
         */
         x1 = (float)(a1* Math.cos(Math.toRadians(sudut1)));
         y1 = (float)(a1* Math.sin(Math.toRadians(sudut1)));
         x2 = (float)((a1 * Math.cos(Math.toRadians(sudut1)) + (a2 * Math.cos(Math.toRadians(sudut1+sudut2)))));
         y2 = (float)((a1 * Math.sin(Math.toRadians(sudut1)) + (a2 * Math.sin(Math.toRadians(sudut1+sudut2)))));
         System.out.println("Nilai x1 adalah "+x1+"\n");
         System.out.println("Nilai y1 adalah "+y1+"\n");
         System.out.println("Nilai x1 adalah "+x2+"\n");
         System.out.println("Nilai y1 adalah "+y2+"\n");
     
     }
    
}
