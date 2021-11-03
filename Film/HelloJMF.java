//import Package
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.media.*;
import java.net.*;
import java.io.File;

//membuat public class dengan turunan Jframe
public class HelloJMF extends JFrame{
	static Player myPlayer = null; //mendefiniskan variable myPlayer
	public HelloJMF(){
		super("Immanuel Richie");
		//Memanggil method play 
		play(); 
		Component panelControl = myPlayer.getControlPanelComponent();
		Component visualComponent = myPlayer.getVisualComponent();

		//menempatkan posisi video
		this.getContentPane().add(panelControl,BorderLayout.SOUTH);
		this.getContentPane().add(visualComponent,BorderLayout.CENTER);

		//memanggil windows media player
		this.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent we){ //method yang dijalankan saat windows media player ditutup
				stop(); //memanggilan method stop
				System.exit(0);
			}
		});
		//mengatur ukuran windows media player
		this.pack();
		this.setSize(new Dimension(500,400));
		this.setVisible(true);
	}

	public static void main(String[] args) {
		HelloJMF helloJMF = new HelloJMF();	
	}

	//method play
	void play(){
		try{
			myPlayer = Manager.createRealizedPlayer(new File("D:/Videos/Facebook.mpg").toURL()); //membuka file dan mengeksekusinya
			myPlayer.start();
		}catch(Exception ex){
			System.out.println("Unable to create the audioPlayer:" + ex);
		}
	}
	//method stop
	void stop(){
		//melakukan stop pada video
		myPlayer.stop();
		myPlayer.close();
	}
}