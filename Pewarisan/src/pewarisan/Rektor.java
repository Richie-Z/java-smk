package pewarisan;

/**
 *
 * @author Richie
 */
public class Rektor extends Dosen{
    private int th_mulai;
    private int jabatan_ke;
    
  Rektor(String namaX, String nikX, String jurX, int thX, int keX) {
        super(namaX, nikX, jurX);
        th_mulai = thX;
        jabatan_ke = keX;
    }
  public void viewRektor(){
      System.out.println("Thn Mulai Jabatan   :"+th_mulai);
      System.out.println("Jabatan Ke          :"+jabatan_ke);
  }
    
}
