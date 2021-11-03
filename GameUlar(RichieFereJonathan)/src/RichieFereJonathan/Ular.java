/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RichieFereJonathan;
import java.awt.EventQueue;
import javax.swing.ImageIcon;
import javax.swing.JFrame;

public class Ular extends JFrame {
 public Ular() {  
        initUI();
    }

    private void initUI() {
        add (new Arena());
        setResizable(false);
        pack();
        setTitle("Snake Hungry");
        ImageIcon icon = new ImageIcon("src/snake.png");
        setIconImage(icon.getImage());
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
    public static void main(String[] args) {
        // TODO code application logic here
        EventQueue.invokeLater(() -> {
            JFrame ex = new Ular();
            ex.setVisible(true);
        });
    }
    }
