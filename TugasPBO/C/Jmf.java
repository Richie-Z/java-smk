import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FileDialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import javax.media.CannotRealizeException;
import javax.media.Manager;
import javax.media.NoPlayerException;
import javax.media.Player;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;

public class Jmf extends JFrame{
	private JButton buttonBrowse, buttonPlay;
	private JLabel labelDuration;
	private JTextField textFileName;
	private JPanel panel1, panel2,panel3;
	private Player player;
	private final String title = "JMF MP3 Player";

	public Jmf(){
		initComponents();
		setLabelDuration(0.0);
	}

	private void initComponents(){
		setSize(new Dimension(400,200));
		setResizable(false);
		setTitle(title);
		setLocationRelativeTo(null);

		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		buttonBrowse = new JButton();
		buttonBrowse.setText("...");
		buttonBrowse.setPreferredSize(new Dimension(50,30));
		buttonBrowse.setToolTipText("Browse Media");
		buttonBrowse.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				openFileDialog();
			}
		});

		buttonPlay = new JButton();
		buttonPlay.setText("Play");
		buttonPlay.setToolTipText("Play Media");
		buttonPlay.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				playMedia();
			}
		});

		textFileName = new JTextField();
		textFileName.setEditable(false);
		textFileName.setPreferredSize(new Dimension(300,30));
		textFileName.setToolTipText("File Name");

		labelDuration= new JLabel();

		labelDuration.setHorizontalAlignment(SwingConstants.CENTER);
		labelDuration.setText("Duration :");
		labelDuration.setToolTipText("Duration");

		panel1 = new JPanel();
		panel1.add(textFileName);
		panel1.add(buttonBrowse);

		panel2 = new JPanel();
		panel2.add(buttonPlay);

		add(panel1, BorderLayout.NORTH);
		add(panel2, BorderLayout.CENTER);
		add(labelDuration, BorderLayout.SOUTH);
		pack();
	}

	private void setLabelDuration(Double duration){
		Integer d =duration.intValue();
		if(d > 0) {
			Integer minutes = d / 60;
			Integer second = d % 60;
			labelDuration.setText("Duration " + minutes +" : "+ second);
		}
	} 

	private void openFileDialog(){
		FileDialog fileDialog = new FileDialog(this, "Open Media File", FileDialog.LOAD);
		fileDialog.setVisible(true);
		try{
			if(fileDialog.getFile() != null ){
				File file = new File(fileDialog.getDirectory(), fileDialog.getFile());
				textFileName.setText(file.getName());
				try {
					player = Manager.createRealizedPlayer(file.toURI().toURL());
					Double time = player.getDuration().getSeconds();
					setLabelDuration(time);
				}catch(NoPlayerException noPx){}
			}
		}catch (IOException ex) {
			
		}catch(CannotRealizeException ex){}
	}
	private void playMedia(){
		if(buttonPlay.getText().equals("Play")){
			try{
				player.start();
				buttonPlay.setText("Pause");
				buttonBrowse.setEnabled(false);
				buttonPlay.setToolTipText("pause");
				this.setTitle(title + "[Playing...]");
			}catch (Exception ex) {}
		}else if(buttonPlay.getText().equals("Pause")){
				try{
				player.start();
				buttonPlay.setText("Play");
				buttonPlay.setToolTipText("Play Media");
				this.setTitle(title + "[Pause...]");
				}catch (Exception e) {}
		}
	}	
	public static void main(String[] args) {
		try{
			UIManager.setLookAndFeel(new javax.swing.plaf.nimbus.NimbusLookAndFeel());
		}catch (UnsupportedLookAndFeelException ex) {
			EventQueue.invokeLater(new Runnable(){
				public void run(){
					new Jmf().setVisible(true);
				}
			});
		}
	}
}