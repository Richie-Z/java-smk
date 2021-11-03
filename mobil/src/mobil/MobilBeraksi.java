package mobil;
public class MobilBeraksi {
    public static void main (String[] args){
        //membuat objek
        Mobil mobil = new Mobil ();
        //memangil atribut dan method nilai */
        mobil.warna ="merah";
        mobil.TahunProduksi = 2009;
        System.out.println("Warna : " + mobil.warna);
        System.out.println("Tahun Produksi : " + mobil.TahunProduksi);
    }
    
}
 