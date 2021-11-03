import javax.media.Manager;
import javax.media.Player;
import java.io.File;

public class Main{
	static Player audioPlayer = null;
	public static void main(String[] args) {
		try{
			Manager.createRealizedPlayer(new File("D:/Music/test.mp3").toURL()).start();
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
}
