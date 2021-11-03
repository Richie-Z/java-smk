/*
 * $Id: JXTaskPaneDemoPanel.java,v 1.3 2005/09/10 13:14:20 l2fprod Exp $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.demo.swingx;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.util.ResourceBundle;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JEditorPane;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.LookAndFeel;
import javax.swing.UIManager;
import javax.swing.text.html.HTMLDocument;

import org.jdesktop.demo.DemoPanel;
import org.jdesktop.swingx.JXErrorDialog;
import org.jdesktop.swingx.JXTaskPane;
import org.jdesktop.swingx.JXTaskPaneContainer;
import org.jdesktop.swingx.plaf.LookAndFeelAddons;
import org.jdesktop.swingx.plaf.aqua.AquaLookAndFeelAddons;
import org.jdesktop.swingx.plaf.metal.MetalLookAndFeelAddons;
import org.jdesktop.swingx.plaf.windows.WindowsClassicLookAndFeelAddons;
import org.jdesktop.swingx.plaf.windows.WindowsLookAndFeelAddons;
import org.jdesktop.swingx.util.JVM;

public class JXTaskPaneDemoPanel extends DemoPanel {
    
    static ResourceBundle RESOURCE = ResourceBundle
            .getBundle("org.jdesktop.demo.swingx.resources.JXTaskPaneDemoPanelRB");
    
    public JXTaskPaneDemoPanel() {
        setName("JXTaskPane Demo");
        initComponents();
    }
    
    public String getHtmlDescription() {
        return RESOURCE.getString("description");
    }
    
    public String getName() {
        return "Task Panes";
    }
    
    public void initComponents() {
        LookAndFeel oldLnF = UIManager.getLookAndFeel();
        try {
            setLayout(new BorderLayout());
            setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            
            JTabbedPane tabs = new JTabbedPane();
            
            { // the metal look and feel
                UIManager.setLookAndFeel(UIManager
                        .getCrossPlatformLookAndFeelClassName());
                LookAndFeelAddons.setAddon(MetalLookAndFeelAddons.class);
                if (JVM.current().isOrLater(JVM.JDK1_5)) {
                    UIManager.getLookAndFeelDefaults().put(JXTaskPane.uiClassID,
                            "org.jdesktop.swingx.plaf.misc.GlossyTaskPaneUI");
                }
                DemoPanel demo = new DemoPanel();
                tabs.addTab("Metal L&F", demo);
            }
            
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            
            { // the windows look and feel "Luna" style
                UIManager.put("win.xpstyle.name", "luna");
                LookAndFeelAddons.setAddon(WindowsLookAndFeelAddons.class);
                DemoPanel demo = new DemoPanel();
                tabs.addTab("Windows (Luna)", demo);
            }
            
            { // the windows look and feel "Homestead" style
                UIManager.put("win.xpstyle.name", "homestead");
                LookAndFeelAddons.setAddon(WindowsLookAndFeelAddons.class);
                DemoPanel demo = new DemoPanel();
                tabs.addTab("Windows (Homestead)", demo);
            }
            
            { // the windows look and feel "Metallic" style
                UIManager.put("win.xpstyle.name", "metallic");
                LookAndFeelAddons.setAddon(WindowsLookAndFeelAddons.class);
                DemoPanel demo = new DemoPanel();
                tabs.addTab("Windows (Silver)", demo);
            }
            
            UIManager.put("win.xpstyle.name", null);
            
            { // the windows classic look and feel
                LookAndFeelAddons.setAddon(WindowsClassicLookAndFeelAddons.class);
                DemoPanel demo = new DemoPanel();
                tabs.addTab("Windows (Classic)", demo);
            }
            
            { // the glossy look
                LookAndFeelAddons.setAddon(AquaLookAndFeelAddons.class);
                DemoPanel demo = new DemoPanel();
                tabs.addTab("Glossy", demo);
            }
            
            add("Center", tabs);
        } catch (Exception e) {
            JXErrorDialog.showDialog(null, "Error", e);
        } finally {
            try {
                UIManager.setLookAndFeel(oldLnF.getClass().getName());
            } catch (Exception e) {}
        }
    }
    
    static class DemoPanel extends JXTaskPaneContainer {
        
        public DemoPanel() {
            JXTaskPaneContainer taskPane = new JXTaskPaneContainer();
            
            // "System" GROUP
            JXTaskPane systemGroup = new JXTaskPane();
            systemGroup.setTitle(RESOURCE.getString("tasks.systemGroup"));
            systemGroup.setToolTipText(RESOURCE
                    .getString("tasks.systemGroup.tooltip"));
            systemGroup.setSpecial(true);
            systemGroup.setIcon(new ImageIcon(JXTaskPaneDemoPanel.class
                    .getResource("resources/tasks-email.png")));
            
            systemGroup.add(makeAction(RESOURCE.getString("tasks.email"), "",
                    "resources/tasks-email.png"));
            systemGroup.add(makeAction(RESOURCE.getString("tasks.delete"), "",
                    "resources/tasks-recycle.png"));
            
            taskPane.add(systemGroup);
            
            // "Office" GROUP
            JXTaskPane officeGroup = new JXTaskPane();
            officeGroup.setTitle(RESOURCE.getString("tasks.office"));
            officeGroup.add(makeAction(RESOURCE.getString("tasks.word"), "",
                    "resources/tasks-writedoc.png"));
            officeGroup.setExpanded(false);
            officeGroup.setScrollOnExpand(true);
            
            taskPane.add(officeGroup);
            
            // "SEE ALSO" GROUP and ACTIONS
            JXTaskPane seeAlsoGroup = new JXTaskPane();
            seeAlsoGroup.setTitle(RESOURCE.getString("tasks.seealso"));
            
            seeAlsoGroup.add(makeAction("The Internet", RESOURCE
                    .getString("tasks.internet.tooltip"),
                    "resources/tasks-internet.png"));
            
            seeAlsoGroup.add(makeAction(RESOURCE.getString("tasks.help"),
                    RESOURCE.getString("tasks.help.tooltip"),
                    "resources/tasks-question.png"));
            
            taskPane.add(seeAlsoGroup);
            
            // "Details" GROUP
            JXTaskPane detailsGroup = new JXTaskPane();
            detailsGroup.setTitle(RESOURCE.getString("tasks.details"));
            detailsGroup.setScrollOnExpand(true);
            
            JEditorPane area = new JEditorPane("text/html", "<html>");
            
            area.setFont(UIManager.getFont("Label.font"));
            area.setEditable(false);
            area.setOpaque(false);
            
            Font defaultFont = UIManager.getFont("Button.font");
            
            String stylesheet = "body { margin-top: 0; margin-bottom: 0; margin-left: 0; margin-right: 0; font-family: "
                    + defaultFont.getName()
                    + "; font-size: "
                    + defaultFont.getSize()
                    + "pt;  }"
                    + "a, p, li { margin-top: 0; margin-bottom: 0; margin-left: 0; margin-right: 0; font-family: "
                    + defaultFont.getName()
                    + "; font-size: "
                    + defaultFont.getSize()
                    + "pt;  }";
            if (area.getDocument() instanceof HTMLDocument) {
                HTMLDocument doc = (HTMLDocument)area.getDocument();
                try {
                    doc.getStyleSheet().loadRules(new java.io.StringReader(stylesheet),
                            null);
                } catch (Exception e) {
                    // TODO: handle exception
                }
            }
            
            area.setText(RESOURCE.getString("tasks.details.message"));
            detailsGroup.add(area);
            
            taskPane.add(detailsGroup);
            
            JScrollPane scroll = new JScrollPane(taskPane);
            scroll.setBorder(null);
            
            setLayout(new BorderLayout());
            add("Center", scroll);
            
            setBorder(null);
        }
        
        Action makeAction(String title, String tooltiptext, String iconPath) {
            Action action = new AbstractAction(title) {
                public void actionPerformed(ActionEvent e) {}
            };
            action.putValue(Action.SMALL_ICON, new ImageIcon(
                    JXTaskPaneDemoPanel.class.getResource(iconPath)));
            action.putValue(Action.SHORT_DESCRIPTION, tooltiptext);
            return action;
        }
    }
    
}