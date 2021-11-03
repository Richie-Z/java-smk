/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package luas_lingkaran;
import java.util.Scanner;
/**
 *
 * @author Richie-PC
 */
public class Luas_lingkaran {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Scanner key = new Scanner(System.in);
        Model model = input_tampil();
        View view = new View();
        Controller controller = new Controller(model,view);
        controller.updateView();
    }

    private static Model input_tampil() {
       System.out.println("HITUNG LUAS LINGKARAN DENGAN MVC");
        Scanner key = new Scanner(System.in);
        Model tampil = new Model();
        System.out.print("INPUT DIAMETER : ");
        int x = key.nextInt();
        tampil.setDiameter(x);
        return tampil;
    }
    
}
