/*

 * $Id: LoginDialog.java,v 1.2 2005/06/06 14:25:36 rbair Exp $

 *

 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,

 * Santa Clara, California 95054, U.S.A. All rights reserved.

 */



package org.jdesktop.demo.login.romain;



import java.awt.BorderLayout;

import java.awt.Dimension;

import java.awt.GridBagConstraints;

import java.awt.GridBagLayout;

import java.awt.Insets;

import java.awt.event.ActionEvent;

import java.awt.event.ActionListener;



import javax.swing.JComponent;

import javax.swing.JDialog;

import javax.swing.JFrame;

import javax.swing.JLabel;

import javax.swing.JPanel;

import javax.swing.SwingUtilities;

import javax.swing.Timer;
import org.jdesktop.swingx.auth.LoginEvent;



public class LoginDialog extends JDialog implements FadeListener {

    private JComponent contentPane;

    private LoginTextField loginField;

    private PasswordTextField passwordField;

    private Timer animation;

    private FadingPanel glassPane;

    private org.jdesktop.swingx.auth.LoginService loginService;

    

    public LoginDialog(org.jdesktop.swingx.auth.LoginService service) {

        super((JFrame)null, true);

        this.loginService = service;

        glassPane = new FadingPanel(this);

        setGlassPane(glassPane);

        

        buildContentPane();

        buildLoginForm();

        startAnimation();

        

        setSize(new Dimension(400, 300));

        setResizable(false);

        setLocationRelativeTo(null);

    }

    

    private void buildContentPane() {

        contentPane = new CurvesPanel();

        contentPane.setLayout(new BorderLayout());

        setContentPane(contentPane);

    }

    

    private void startAnimation() {

        animation = new Timer(50, new ActionListener() {

            public void actionPerformed(ActionEvent e) {

                contentPane.repaint();

            }

        });

        animation.start();

    }

    

    private void buildLoginForm() {

        JPanel form = new JPanel(new GridBagLayout());

        form.add(new JLabel(UIHelper.readImageIcon("title.png")), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.3, GridBagConstraints.SOUTH, GridBagConstraints.NONE, new Insets(12, 36, 11, 7), 0, 0));

        

        loginField = new LoginTextField();

        form.add(loginField, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHEAST, GridBagConstraints.NONE, new Insets(0, 36, 5, 11), 0, 0));

        

        passwordField = new PasswordTextField();

        form.add(passwordField, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.7, GridBagConstraints.NORTHEAST, GridBagConstraints.NONE, new Insets(0, 36, 11, 11), 0, 0));

        

        passwordField.addActionListner(new ActionListener() {

            public void actionPerformed(ActionEvent e) {

                glassPane.setVisible(true);

                new Thread() {

                    public void run() {

                        

                        //wait a few seconds before accepting...

//                        try {

//                            Thread.sleep(5000);

//                        } catch (Exception e) {

//                            //probably a wake up exception

//                        }

                        //perform the login procedures

                        loginService.addLoginListener(new org.jdesktop.swingx.auth.LoginListener() {

                            /**

                             *  Called by the <strong>JXLoginPanel</strong> in the event of a login failure

                             *

                             * @param source panel that fired the event

                             */

                            public void loginFailed(LoginEvent source) {

//                                glassPane.setVisible(false);

//                                setVisible(false);

                            }

                            /**

                             *  Called by the <strong>JXLoginPanel</strong> when the Authentication

                             *  operation is started.

                             * @param source panel that fired the event

                             */

                            public void loginStarted(LoginEvent source) {

                                

                            }

                            /**

                             *  Called by the <strong>JXLoginPanel</strong> in the event of a login

                             *  cancellation by the user.

                             *

                             * @param source panel that fired the event

                             */

                            public void loginCanceled(LoginEvent source) {

//                                glassPane.setVisible(false);

//                                setVisible(false);

                            }

                            /**

                             *  Called by the <strong>JXLoginPanel</strong> in the event of a

                             *  successful login.

                             *

                             * @param source panel that fired the event

                             */

                            public void loginSucceeded(LoginEvent source) {

                                glassPane.setVisible(false);

                                setVisible(false);

                            }

                        });

                        try {
                            loginService.startAuthentication(loginField.getText(), passwordField.getText().toCharArray(), "java.net");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }

                }.start();

            }

        });

        

        form.setOpaque(false);

        contentPane.add(form, BorderLayout.CENTER);

    }

    

    public void fadeInFinished() {

        glassPane.setVisible(false);

    }

    

    public void fadeOutFinished() {

        SwingUtilities.invokeLater(new Runnable() {

            public void run() {

                contentPane = new CirclesPanel();

                contentPane.setLayout(new BorderLayout());

                WaitAnimation waitAnimation = new WaitAnimation();

                contentPane.add(waitAnimation, BorderLayout.CENTER);

                setContentPane(contentPane);

                validate();

                glassPane.switchDirection();

            }

        });

    }

    

    public String getTitle() {

        return "Login...";

    }

    

    public static void main(String[] args) {
        LoginDialog frame = new LoginDialog(null);
        frame.setVisible(true);
    }

}

