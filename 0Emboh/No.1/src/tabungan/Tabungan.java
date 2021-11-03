package tabungan;
/**
 *
 * @author Richie
 */

public class Tabungan {
    
    private String nama;        // awalnya Private diganti menjadi private
    private int NoRekening;     // awalnya Private diganti menjadi private
    private int saldo;          // awalnya Private diganti menjadi private
    private int pin;            // awalnya Private diganti menjadi private
    private String getSaldo;    // penambahan getSaldo
            
    private Tabungan(String namaNasabah, int NoRek){ //public void digantiprivae
        nama = namaNasabah;
        NoRekening = NoRek;
        saldo = 10000;
        pin = 12345;
    }
    public void simpanUang (int sal){ //awalnya Public diganti mejadi public
        saldo = saldo + sal;  //awalnya Saldo diganti mejadi saldo
    } 
    public void tarikUang (int jumlahTarikan){  //awalnya Public diganti mejadi public
        saldo = saldo - jumlahTarikan;  //awalnya Saldo diganti mejadi saldo
    }
    public int getSaldo()  {    //awalnya Public diganti mejadi public
        return saldo;   //awalnya Return diganti mejadi return
    }
    public void getSaldo(int sal) { //awalnya Public diganti mejadi public
        saldo = sal;    //awalnya Saldo diganti mejadi saldo
    }
    public void setPin (int pinBaru) {  //awalnya Public diganti mejadi public
        pin = pinBaru;  //awalnya Pin diganti mejadi pin
    }
    public static void main(String[] args) {  
//awalnya Public diganti mejadi public dan String diganti menjadi String[] dan args[] diganti menjadi args
        int jumlahTarikan = 2000;
        int jumlahSimpanan = 5000;
        Tabungan bls = new Tabungan("Richie", 12345);
        System.out.println("Saldo Awal :"+ bls.getSaldo());
        bls.tarikUang(jumlahTarikan);
        System.out.println("Saldo setelah penarikan 2000 adalah :"+ bls.getSaldo());
        bls.simpanUang(jumlahSimpanan);
        System.out.println("Saldo setelah penyetoran 5000 adlah :"+ bls.getSaldo());
      }
}



    
