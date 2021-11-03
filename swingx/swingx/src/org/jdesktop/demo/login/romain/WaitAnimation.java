/*
 * $Id: WaitAnimation.java,v 1.1 2005/05/25 23:13:23 rbair Exp $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */

package org.jdesktop.demo.login.romain;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComponent;
import javax.swing.Timer;

public class WaitAnimation extends JComponent implements ActionListener {
	private Image[] animation;
	private int index;
	private int direction;

	public WaitAnimation() {
		setOpaque(false);

		index = 0;
		direction = 1;

		MediaTracker tracker = new MediaTracker(this);
		animation = new Image[6];

		for (int i = 0; i < 6; i++) {
			animation[i] = UIHelper.readImage("auth_" + String.valueOf(i) + ".png");
			tracker.addImage(animation[i], i);
		}

		try {
			tracker.waitForAll();
		} catch (InterruptedException e) {
		}

		Timer animationTimer = new Timer(150, this);
		animationTimer.start();
	}
	
	public void paintComponent(Graphics g) {
		int x = (int) ((getWidth() - animation[index].getWidth(this)) / 2.0);
		int y = (int) ((getHeight() - animation[index].getHeight(this)) / 2.0);
		
		g.drawImage(animation[index], x, y, this);
	}

	public void actionPerformed(ActionEvent e) {
		index += direction;
		if (index > 5) {
			index = 5;
			direction = -1;
		} else if (index < 0) {
			index = 0;
			direction = 1;
		}
	}
}
