/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package richie;


import jaco.mp3.player.MP3Player;
import java.io.File;
import javax.swing.JFileChooser;
/**
 *
 * @author Richie-PC
 */
public class Player extends javax.swing.JFrame {

    /**
     * Creates new form Form
     */

    MP3Player player;
    File songFile;
    String currentDirectory = "D:\\Music"; 
   
    public Player() {
        initComponents();
        songFile = new File("D:\\Music\\Copyright-Richie.mp3");
        String Filename = songFile.getName();
        songname.setText(Filename);
        player = mp3Player();
        player.addToPlayList(songFile);
        
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        panelSong = new javax.swing.JPanel();
        songname = new javax.swing.JLabel();
        exitbtn = new javax.swing.JLabel();
        pausebtn = new javax.swing.JLabel();
        playbtn = new javax.swing.JLabel();
        stopbtn = new javax.swing.JLabel();
        openbtn = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setUndecorated(true);

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        panelSong.setBackground(new java.awt.Color(255, 255, 255));
        panelSong.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        songname.setBackground(new java.awt.Color(204, 204, 255));
        songname.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        songname.setText("NOW PLAYING");

        javax.swing.GroupLayout panelSongLayout = new javax.swing.GroupLayout(panelSong);
        panelSong.setLayout(panelSongLayout);
        panelSongLayout.setHorizontalGroup(
            panelSongLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelSongLayout.createSequentialGroup()
                .addComponent(songname, javax.swing.GroupLayout.DEFAULT_SIZE, 690, Short.MAX_VALUE)
                .addContainerGap())
        );
        panelSongLayout.setVerticalGroup(
            panelSongLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelSongLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(songname)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        exitbtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/richie.image/exit.png"))); // NOI18N
        exitbtn.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        exitbtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                exitbtnMouseClicked(evt);
            }
        });

        pausebtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/richie.image/pause.png"))); // NOI18N
        pausebtn.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        pausebtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                pausebtnMouseClicked(evt);
            }
        });

        playbtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/richie.image/play.png"))); // NOI18N
        playbtn.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        playbtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                playbtnMouseClicked(evt);
            }
        });

        stopbtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/richie.image/stop.png"))); // NOI18N
        stopbtn.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        stopbtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                stopbtnMouseClicked(evt);
            }
        });

        openbtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/richie.image/open.png"))); // NOI18N
        openbtn.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        openbtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                openbtnMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(exitbtn))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(openbtn)
                        .addGap(191, 191, 191)
                        .addComponent(pausebtn)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(playbtn)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(stopbtn)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(panelSong, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(27, Short.MAX_VALUE))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(exitbtn)
                        .addGap(29, 29, 29)
                        .addComponent(panelSong, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 78, Short.MAX_VALUE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(playbtn, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(openbtn, javax.swing.GroupLayout.Alignment.TRAILING)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(pausebtn)
                            .addComponent(stopbtn))))
                .addGap(30, 30, 30))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void exitbtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_exitbtnMouseClicked
        System.exit(0);
    }//GEN-LAST:event_exitbtnMouseClicked

    private void openbtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_openbtnMouseClicked
        JFileChooser openFileChooser = new JFileChooser(currentDirectory);
        openFileChooser.setFileFilter(new FileTypeFilter(".mp3", "Hanya bisa mp3!"));
        int result = openFileChooser.showOpenDialog(null);
        if(result == JFileChooser.APPROVE_OPTION){
            songFile = openFileChooser.getSelectedFile();
            player.addToPlayList(songFile);
            player.skipForward();
            currentDirectory = songFile.getAbsolutePath();
            songname.setText(songFile.getName());
        }
    }//GEN-LAST:event_openbtnMouseClicked

    private void playbtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_playbtnMouseClicked
        player.play();
        songname.setText("Playing ... | "+songFile.getName());
    }//GEN-LAST:event_playbtnMouseClicked

    private void stopbtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_stopbtnMouseClicked
        songname.setText("Stoped ... | "+songFile.getName());
        player.stop();
    }//GEN-LAST:event_stopbtnMouseClicked

    private void pausebtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pausebtnMouseClicked
        songname.setText("Paused ... | "+songFile.getName());
        player.pause();
    }//GEN-LAST:event_pausebtnMouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Player.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Player.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Player.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Player.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Player().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel exitbtn;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JLabel openbtn;
    private javax.swing.JPanel panelSong;
    private javax.swing.JLabel pausebtn;
    private javax.swing.JLabel playbtn;
    private javax.swing.JLabel songname;
    private javax.swing.JLabel stopbtn;
    // End of variables declaration//GEN-END:variables

    private MP3Player mp3Player() {
        MP3Player mp3Player = new MP3Player();
        return mp3Player;
    }
}