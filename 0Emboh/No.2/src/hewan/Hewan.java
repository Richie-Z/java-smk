
package hewan;
/**
 *
 * @author Richie
 */

public class Hewan {
    int jumlahkaki = 0; 
    String namahewan = "";
    
    public Hewan(String nama, int kaki){
        this.jumlahkaki = kaki;
        this.namahewan = nama;
    }
    
    public void info (){
        System.out.println("Nama Hewan: " + this.namahewan + ", Jumlah kaki:" + this.jumlahkaki);
    }
}
