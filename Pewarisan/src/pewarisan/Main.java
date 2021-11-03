/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this  file, choose Tools | Templates
 * and open the template in the editor.
 */
package pewarisan;
public class Main {
    public static void main (String[] args){
        Rektor rek = new Rektor ("Andi","123456789","Information",2006,2);
        Dekan dek = new Dekan ("Ahmad","123456789","Kimia","TI");
        Kalab lab = new Kalab ("Indah","123456","Information","KSC");
        
        rek.view();
        rek.viewRektor();
        
        dek.view();
        dek.viewDekan();
        
        lab.view();
        lab.viewKalab();
    }
    
}
