/*
 * $Id: JXTipOfTheDayDemoPanel.java,v 1.2 2005/08/25 14:32:42 l2fprod Exp $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.demo.swingx;

import java.awt.BorderLayout;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.util.ResourceBundle;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JEditorPane;
import javax.swing.JOptionPane;
import javax.swing.JSeparator;
import javax.swing.JTree;
import javax.swing.LookAndFeel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.plaf.metal.MetalLookAndFeel;

import org.jdesktop.demo.DemoPanel;
import org.jdesktop.swingx.JXEditorPane;
import org.jdesktop.swingx.JXHyperlink;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.JXTipOfTheDay;
import org.jdesktop.swingx.VerticalLayout;
import org.jdesktop.swingx.plaf.LookAndFeelAddons;
import org.jdesktop.swingx.plaf.basic.BasicTipOfTheDayUI;
import org.jdesktop.swingx.plaf.metal.MetalLookAndFeelAddons;
import org.jdesktop.swingx.plaf.windows.WindowsLookAndFeelAddons;
import org.jdesktop.swingx.tips.DefaultTip;
import org.jdesktop.swingx.tips.DefaultTipOfTheDayModel;

public class JXTipOfTheDayDemoPanel extends DemoPanel {

  static ResourceBundle RESOURCE = ResourceBundle
    .getBundle("org.jdesktop.demo.swingx.resources.JXTipOfTheDayDemoPanelRB");

  public JXTipOfTheDayDemoPanel() {
    setName("JXTipOfTheDay Demo");
    initComponents();
  }

  public String getHtmlDescription() {
    return RESOURCE.getString("description");
  }

  public String getName() {
    return RESOURCE.getString("name");
  }

  public void initComponents() {
    setLayout(new VerticalLayout(3));
    // click to show the Tip Of the Day dialog with the default look and feel
    // applies for all but Windows
    addLookAndFeel(
      RESOURCE.getString("defaultlnf.description"), 
      MetalLookAndFeel.class.getName(),
      MetalLookAndFeelAddons.class.getName());
    
    addLookAndFeel(
      RESOURCE.getString("windowslnf.description"), 
      "com.sun.java.swing.plaf.windows.WindowsLookAndFeel",
      WindowsLookAndFeelAddons.class.getName());
    
    //
    // JXTipOfTheDay is also a regular component which can be embedded
    //
    JXEditorPane descriptionPane = new JXEditorPane();
    descriptionPane.setEditable(false);
    descriptionPane.setContentType("text/html");
    descriptionPane.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES,
      Boolean.TRUE);
    descriptionPane.setText(RESOURCE.getString("embedded.description"));
    descriptionPane.setOpaque(false);
    descriptionPane.setBorder(BorderFactory.createEmptyBorder(3, 12, 3, 3));
    add(descriptionPane);

    JXPanel panel = new JXPanel(new BorderLayout());
    panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));    
    JXTipOfTheDay embedded = newTOTD();
    panel.add("Center", embedded);
    embedded.setAlpha(0.8f);

    JXHyperlink nextTip = new JXHyperlink(embedded.getActionMap().get("nextTip"));
    nextTip.setText(RESOURCE.getString("embedded.nextTip"));
    nextTip.setVerticalAlignment(SwingConstants.CENTER);
    nextTip.setHorizontalAlignment(SwingConstants.CENTER);
    nextTip.setFocusPainted(false);
    
    add(nextTip);
    add(panel);    
  }
    
  void addLookAndFeel(String description, String lnfClassname,
      String addonClassname) {
    // do not add components if the look and feel is not found on this platform
    try {
      Class.forName(lnfClassname);
    } catch (Exception e) {
      return;
    }
    
    JXEditorPane descriptionPane = new JXEditorPane();
    descriptionPane.setEditable(false);
    descriptionPane.setContentType("text/html");
    descriptionPane.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES,
      Boolean.TRUE);
    descriptionPane.setText(description);
    descriptionPane.setOpaque(false);
    descriptionPane.setBorder(BorderFactory.createEmptyBorder(3, 12, 3, 3));
    add(descriptionPane);
    
    Action action = makeAction(
      lnfClassname,
      addonClassname);    
    JXHyperlink link = new JXHyperlink(action);
    link.setVerticalAlignment(SwingConstants.CENTER);
    link.setHorizontalAlignment(SwingConstants.CENTER);
    link.setFocusPainted(false);
    link.setText("Click to open the Tip Of The Day dialog");
    add(link);
    
    add(new JSeparator(JSeparator.HORIZONTAL));
  }
  
  static JXTipOfTheDay newTOTD() {
    // Create a tip model with some tips
    DefaultTipOfTheDayModel tips = new DefaultTipOfTheDayModel();
    
    // plain text
    tips
      .add(new DefaultTip(
        "tip1",
        "This is the first tip " +
        "This is the first tip " +
        "This is the first tip " +
        "This is the first tip " +
        "This is the first tip " +
        "This is the first tip\n" +
        "This is the first tip " +
        "This is the first tip"));
    
    // html text
    tips
      .add(new DefaultTip(
        "tip2",
        "<html>This is an html <b>TIP</b><br><center>" +
        "<table border=\"1\">" +
        "<tr><td>1</td><td>entry 1</td></tr>" +
        "<tr><td>2</td><td>entry 2</td></tr>" +
        "<tr><td>3</td><td>entry 3</td></tr>" +
        "</table>"));
    
    // a Component
    tips.add(new DefaultTip("tip3", new JTree()));

    // an Icon
    tips
      .add(new DefaultTip(
        "tip 4",
        new ImageIcon(BasicTipOfTheDayUI.class.getResource("resources/TipOfTheDay24.gif"))));
    
    JXTipOfTheDay totd = new JXTipOfTheDay(tips);
    totd.setCurrentTip(0);
    
    return totd;
  }
  
  static Action makeAction(final String lnf, final String addon) {
    
    // JXTipOfTheDay component can save the "Show Tips on startup" user
    // selection in java.util.prefs.Preferences. Additionally it offers
    // an interface called ShowOnStartupChoice to abstract the place where
    // the user choice is saved.

    // Create a non-persistent ShowOnStartupChoice
    final JXTipOfTheDay.ShowOnStartupChoice fake = new JXTipOfTheDay.ShowOnStartupChoice() {
      private boolean value = true;
      public boolean isShowingOnStartup() {
        return value;
      }
      public void setShowingOnStartup(boolean showOnStartup) {
        value = showOnStartup;
      }
    };

    // This action will set the look and feel and addon given as parameters
    Action action = new AbstractAction() {
      public void actionPerformed(ActionEvent e) {
        LookAndFeel currentLnf = UIManager.getLookAndFeel();
        LookAndFeelAddons currentAddon = LookAndFeelAddons.getAddon();
        
        // if the user previously chooses to not show the tips, show a dialog to
        // cancel this choice. This is only for demo purpose. In a real world
        // application, you would just call showDialog and the JXTipOfTheDay
        // would check if the dialog has to be shown or not.
        if (!fake.isShowingOnStartup()) {
          if (JOptionPane.OK_OPTION == JOptionPane
            .showConfirmDialog(
              null,
              RESOURCE.getString("doyouwanttocancel"),
              RESOURCE.getString("question"),
              JOptionPane.YES_NO_OPTION)) {
            // reset the choice
            fake.setShowingOnStartup(true);
          }
        }

        // change the look and feel and addon
        try {
          UIManager.setLookAndFeel(lnf);
          LookAndFeelAddons.setAddon(addon);
        } catch (Exception ex) {
        }
        
        JXTipOfTheDay totd = newTOTD();
        totd.showDialog(KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner(), fake);
        
        try {
          UIManager.setLookAndFeel(currentLnf.getClass().getName());
          LookAndFeelAddons.setAddon(currentAddon.getClass().getName());
        } catch (Exception ex) {
        }
      }
    };
    
    return action;
  }

}
