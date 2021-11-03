package readconsole;

import java.io.*;
public class ReadConsole {
    public static void main(String[] args) throws IOException {
    InputStreamReader cin = null;
    
        try {
         //membaca input user
          cin=new InputStreamReader (System.in);
          System.out.println("Masukan karakter,'x' untuk berhenti.");
           char c;
         //menampilkan input user selama user tidak memasuka karakter'x'
         
           do {
              c =(char)cin.read();
              System.out.print(c);
           }while (c !='x');
           
         } finally {
              if (cin != null){
              cin.close();
              }
            }
    }
}
