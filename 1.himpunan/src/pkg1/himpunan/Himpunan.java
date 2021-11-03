/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg1.himpunan;

/**
 *
 * @author Richie
 */
import java.util.HashSet;
import java.util.Set;
public class Himpunan {
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        HashSet a = new HashSet ();
        Set b = new HashSet ();
        Set c = new HashSet ();
        Set d = new HashSet ();
        Set e = new HashSet ();
        Set f = new HashSet ();
        for(int i=1;i<=5;i++){
            a.add(i);
            b.add(i);
            c.add(i);
            d.add(i);
            e.add(i);
        }
        for(int i=5;i<=10;i++){
            f.add(i);
        }
        System.out.println("A = "+a);
        System.out.println("B = "+f);
        b.removeAll(f);
        System.out.println("A - B = "+b);
        c.retainAll(f);
        System.out.println("A ∩ B = "+c);
        d.addAll(f);
        System.out.println("A ∪ B = "+d);
        e.containsAll(f);
        System.out.println("A ⊂ B = "+e);
    }
    
}
