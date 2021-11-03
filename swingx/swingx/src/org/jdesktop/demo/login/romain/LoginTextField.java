/*
 * $Id: LoginTextField.java,v 1.2 2005/06/03 20:27:42 rbair Exp $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */

package org.jdesktop.demo.login.romain;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import org.jdesktop.swingx.border.DropShadowBorder;

public class LoginTextField extends JPanel {
	private JTextField loginField;

	public LoginTextField() {
		setOpaque(false);
//		setBorder(BorderFactory.createLineBorder(Color.BLACK));
                DropShadowBorder border = new DropShadowBorder(Color.BLACK, 1, 3);
                setBorder(border);
		setLayout(new BorderLayout());

		addLoginLabel();
		addLoginTextField();
		
		addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				loginField.requestFocus();
			}
		});
	}

	public String getText() {
		return loginField.getText();
	}

	public void addActionListner(ActionListener actionListener) {
		loginField.addActionListener(actionListener);
	}

	private void addLoginTextField() {
		loginField = new JTextField(System.getProperty("user.name"), 10);
		loginField.setBorder(BorderFactory.createEmptyBorder(3, 12, 3, 3));
		loginField.setOpaque(false);
		loginField.setSelectionColor(Color.GRAY);
		loginField.setSelectedTextColor(Color.WHITE);
		loginField.setSelectionStart(0);
		loginField.setSelectionEnd(loginField.getText().length());
		add(BorderLayout.EAST, loginField);
	}

	private void addLoginLabel() {
		JLabel loginLabel = new JLabel("login_");
		loginLabel.setForeground(Color.GRAY);
		loginLabel.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
		add(BorderLayout.WEST, loginLabel);
	}

	public void paintComponent(Graphics g) {
		Color veil = new Color(255, 255, 255, 150);
		g.setColor(veil);
                Insets insets = getInsets();
		g.fillRect(insets.left, insets.top, getWidth() - insets.right - insets.left, getHeight() - insets.bottom - insets.top);
	}
}
