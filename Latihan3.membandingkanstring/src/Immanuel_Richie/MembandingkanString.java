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
public class MembandingkanString {
    public static void main(String[] args){
        String str1 = "Ilmu";
        String str2 = "Komputer";
        String str3 = "IlmuKomputer";
        System.out.println("String 1: " + str1);
        System.out.println("String 2: " + str2);
        System.out.println("String 3: " + str3);
        System.out.println("String 1 = String 3 ==> " + str1.equals(str3));
        System.out.println("String 2 = String 3 ==> " + str2.equals(str3));
        System.out.println("String 1 + String 2 = String 3 ==> " + (str1+str2).equals(str3));
        String str4 = "Universitas Gadjah Mada"; String str5 ="universitas gadjah mada";
        System.out.println("String 4: " + str4); System.out.println("String 5: " + str5);
        String kata = "Ini perkataan";
        kata +="saya sendiri";
        System.out.println("Perintah Pebandingan dibawah mengabaikan antara huruf besar degan huruf kecil");
        System.out.println("String 4 = String 5 ==> " + str4.equalsIgnoreCase(str5));
        System.out.println("String 4 + String 5 = String 5 + String 4 ==> " 
                + (str4+str5).equalsIgnoreCase(str5+str4));
        String str6 = "MIPA";
        String str7 = new String(str6);
        String str8 = "mipa";
        String str9 = "MIPA";
        System.out.println("String 6:"+str6);
        System.out.println("String 7:"+str7);
        System.out.println("String 8:"+str8);
        System.out.println("String 9:"+str9);
        System.out.println("Perintah Pembadingan degan ==");
        System.out.println("String 6 = String 7 ==> "+(str6==str7));
        System.out.println("String 6 = String 8 ==> "+(str6==str8));
        System.out.println("String 6 = String 9 ==> "+(str6==str9));
        
    }
}
