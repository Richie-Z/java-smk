/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nama;

/**
 *
 * @author Richie
 */
public class Direktur{
    private String nama;
    private String usia;
    private String bagian;
    private String gaji;
    private String tunjangan;
    private String fasilitas;
    
  private String getnama(){
      return nama;
  }
  public void setnama(String nama){
      this.nama=nama;
  }
  private String getusia(){
      return usia;
  }
  public void setusia(String usia){
      this.usia=usia;
}
  private String getbagian(){
      return bagian;
  }
  public void setbagian(String bagian){
      this.bagian=bagian;
  }
  public String getgaji(){
      return gaji;
  }
  public void setgaji(String gaji){
      this.gaji=gaji;
  }
  public String getfasilitas(){
      return fasilitas;
  }
  public void setfasilitas(String fasilitas){
      this.fasilitas=fasilitas;
  }
  public String gettunjangan(){
      return tunjangan;
  }
  public void settunjangan (String tunjangan){
      this.tunjangan=tunjangan;
  }
  public void cetak(){
      System.out.println("Nama           : "+nama);
      System.out.println("Usia           : "+usia);
      System.out.println("Bagian         : "+bagian);
      System.out.println("Gaji           : "+gaji);
      System.out.println("Tunjangan      : "+tunjangan);
      System.out.println("Fasilitas      : "+fasilitas);
      
  }
  
public void input(String nama, String usia, String bagian, String gaji, String tunjangan, String fasilitas){
    setnama(nama);
    setusia(usia);
    setbagian(bagian);
    setgaji(gaji);
    settunjangan(tunjangan);       
    setfasilitas(fasilitas);        
}
public void tampil(){
    Direktur dir = new Direktur();
    dir.input("Antonio Anggara", "25", "Direktur", "Rp. 10.000.000,00", "Rp.  5.000.000,00", "Mobil Dinas, Rumah Dinas");
    dir.cetak();
}
}