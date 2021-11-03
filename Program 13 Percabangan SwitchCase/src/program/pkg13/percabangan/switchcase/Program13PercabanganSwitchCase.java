/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package program.pkg13.percabangan.switchcase;

/**
 *
 * @author Richie
 */
public class Program13PercabanganSwitchCase {

   
    public static void main(String[] args) {
       int grade = 'B';
       switch (grade){
           case 'A' :
               System.out.println("Excellent");break;
           case 'B' :
               System.out.println("Good Job!");break;
           case 'C' :
               System.out.println("Study Harder");break;
           case 'D' :
               System.out.println("Sorry, You failed");break;
       }
    }
    
}
