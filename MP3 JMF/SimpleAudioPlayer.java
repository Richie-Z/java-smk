import javax.media.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.MalformedURLException;

public class SimpleAudioPlayer {
	private Player audioPlayer = null;
	public SimpleAudioPlayer(URL url) throws IOException,NoPlayerException,CannotRealizeException {
		audioPlayer = Manager.createRealizedPlayer(url);
	}

	public SimpleAudioPlayer(File file) throws IOException,NoPlayerException,CannotRealizeException {
		this(file.toURL());
	}

	public void play(){
		audioPlayer.start();
	}

	public void stop(){
		audioPlayer.stop();
		audioPlayer.close();
	}

	public static void printUsage(){
		System.out.println("Usage: java SimpleAudioPlayer audioFile");
	}

	public static void main(String[] args){
		try{
			if(args.length == 1){
				File audioFile = new File(args[0]);
				SimpleAudioPlayer player = new SimpleAudioPlayer(audioFile);
				System.out.println();
				System.out.println("-> Playing file <"+ audioFile.getAbsolutePath()+">");
				System.out.println("	Press the Enter key to exit");
				player.play();
				System.in.read();
				System.out.println("-> Exiting");
				player.stop();			
			}else{
				printUsage();
			}
		} catch (Exception ex){
			ex.printStackTrace();
		}
		System.exit(0);
	}
}