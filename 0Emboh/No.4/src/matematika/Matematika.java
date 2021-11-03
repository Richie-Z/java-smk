
package matematika;
/**
 *
 * @author Richie
 */

public class Matematika {
    String operasi;
    int x,y,hasil;
    double hasil2;
    void penjumlahan(){
        x = 40;
        y = 30;
        hasil = x+y;
        System.out.println("Penjumlahan :"+x+"+"+y+" = "+ hasil);
    }
    void pengurangan(){
        x = 10;
        y = 2;
        hasil = x-y;
        System.out.println("Pengurangan :"+x+"-"+y+" = "+ hasil);
    }
    void perkalian(){
        x = 6;
        y = 50;
        hasil = x*y;
        System.out.println("Perkalian :"+x+"x"+y+" = "+ hasil);
    }
    void pembagian(){
        x = 21;
        y = 4;
        double hasil2 = (double) x/y;
        System.out.println("Pembagian :"+x+"/"+y+" = "+ hasil2+"(Float or double)");
    }

   
    
}
