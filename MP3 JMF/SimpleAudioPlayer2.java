import javax.media.Manager;
import javax.media.Player;
import java.io.File;

public class SimpleAudioPlayer2 {
	static Player audioPlayer = null;
	public static void main(String[] args) {
		try{
			System.out.println("TIDAK ADA TAMPILAN TAPI ADA SUARA MUSIK ");
			Manager.createRealizedPlayer(new File("D:/Music/richie.mp3").toURL()).start();

		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
}