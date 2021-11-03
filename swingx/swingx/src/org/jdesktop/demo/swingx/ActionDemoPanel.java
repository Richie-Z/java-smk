/*
 * DropShadowBorderPanel.java
 *
 * Created on April 28, 2005, 10:27 AM
 */

package org.jdesktop.demo.swingx;
import com.jgoodies.looks.HeaderStyle;
import com.jgoodies.looks.Options;
import com.sun.java.swing.SwingUtilities2;
import java.awt.BorderLayout;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JMenuBar;
import org.jdesktop.demo.DemoPanel;
import org.jdesktop.demo.MainWindow;
import org.jdesktop.swingx.JXEditorPane;
import org.jdesktop.swingx.action.AbstractActionExt;
import org.jdesktop.swingx.action.ActionContainerFactory;
import org.jdesktop.swingx.action.ActionManager;
import org.jdesktop.swingx.action.BoundAction;
import org.jdesktop.swingx.action.TargetManager;
import org.jdesktop.swingx.action.TargetableAction;
import org.jdesktop.swingx.border.DropShadowBorder;


/**
 *
 * @author  rbair
 */
public class ActionDemoPanel extends DemoPanel {
    private List actions = new ArrayList();
    private List toolbarActions = new ArrayList();
    private ActionManager manager;
    private DropShadowBorder dsb = new DropShadowBorder();
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        MainWindow.main(new String[]{"-d", "org.jdesktop.demo.swingx.ActionDemoPanel"});
    }
    
    private final class AntiAliasAction extends AbstractActionExt {
        public AntiAliasAction(String text, String commandId, Icon icon) {
            super(text, commandId, icon);
            super.setStateAction(true);
        }
        public void actionPerformed(java.awt.event.ActionEvent e) {
            //do nothing
        }

        public void itemStateChanged(ItemEvent e) {
            switch (e.getStateChange()) {
                case ItemEvent.SELECTED:
                    editorPane.putClientProperty(SwingUtilities2.AA_TEXT_PROPERTY_KEY, Boolean.TRUE);
                    editorPane.repaint();
                    break;
                case ItemEvent.DESELECTED:
                    editorPane.putClientProperty(SwingUtilities2.AA_TEXT_PROPERTY_KEY, Boolean.FALSE);
                    editorPane.repaint();
                    break;
            }
        }
    }
   
    public ActionDemoPanel() {
        setName("Action Demo");
        
        //just using the same icon for now
        ImageIcon icon = new ImageIcon(getClass().getResource("resources/gear.png"));
        
        manager = ActionManager.getInstance();
        manager.addAction(new BoundAction("File", "fileMenu"));
        manager.addAction(new BoundAction("Text", "textMenu"));
        manager.addAction(new BoundAction("Save", "save", loadIcon("/toolbarButtonGraphics/general/Save16.gif")));
        manager.addAction(new BoundAction("Print", "print", loadIcon("/toolbarButtonGraphics/general/Print16.gif")));
        manager.addAction(new TargetableAction("Cut", "cut", loadIcon("/toolbarButtonGraphics/general/Cut16.gif")));
        manager.addAction(new TargetableAction("Copy", "copy", loadIcon("/toolbarButtonGraphics/general/Copy16.gif")));
        manager.addAction(new TargetableAction("Paste", "paste", loadIcon("/toolbarButtonGraphics/general/Paste16.gif")));
        manager.addAction(new TargetableAction("Undo", "undo", loadIcon("/toolbarButtonGraphics/general/Undo16.gif")));
        manager.addAction(new TargetableAction("Redo", "redo", loadIcon("/toolbarButtonGraphics/general/Redo16.gif")));
        manager.addAction(new TargetableAction("Find", "find", loadIcon("/toolbarButtonGraphics/general/Search16.gif")));
        manager.addAction(new TargetableAction("Left Align", "left-justify", loadIcon("/toolbarButtonGraphics/text/AlignLeft16.gif")));
        manager.addAction(new TargetableAction("Center Align", "center-justify", loadIcon("/toolbarButtonGraphics/text/AlignCenter16.gif")));
        manager.addAction(new TargetableAction("Right Align", "right-justify", loadIcon("/toolbarButtonGraphics/text/AlignRight16.gif")));
        manager.addAction(new TargetableAction("Bold", "font-bold", loadIcon("/toolbarButtonGraphics/text/Bold16.gif")));
        manager.addAction(new TargetableAction("Italic", "font-italic", loadIcon("/toolbarButtonGraphics/text/Italic16.gif")));
        manager.addAction(new TargetableAction("Normal", "font-normal", loadIcon("/toolbarButtonGraphics/text/Normal16.gif")));
        manager.addAction(new TargetableAction("Underline", "font-underline", loadIcon("/toolbarButtonGraphics/text/Underline16.gif")));
        manager.addAction(new AntiAliasAction("Anti-Alias", "aa", icon));
        
        List fileMenu = new ArrayList();
        fileMenu.add("fileMenu");
        actions.add(fileMenu);
        actions.add("save");
        actions.add("print");
        List textMenu = new ArrayList();
        textMenu.add("textMenu");
        actions.add(textMenu);
        actions.add("cut");
        actions.add("copy");
        actions.add("paste");
        actions.add("undo");
        actions.add("redo");
        actions.add("find");
        actions.add(null);
        actions.add("left-justify");
        actions.add("center-justify");
        actions.add("right-justify");
        actions.add(null);
        actions.add("font-bold");
        actions.add("font-italic");
        actions.add("font-underline");
        actions.add("font-normal");
        actions.add(null);
        actions.add("aa");
        
        ((AbstractActionExt)manager.getAction("left-justify")).setGroup("alignment");
//        ((AbstractActionExt)manager.getAction("left-justify")).setStateAction(true);
        ((AbstractActionExt)manager.getAction("center-justify")).setGroup("alignment");
//        ((AbstractActionExt)manager.getAction("center-justify")).setStateAction(true);
        ((AbstractActionExt)manager.getAction("right-justify")).setGroup("alignment");
//        ((AbstractActionExt)manager.getAction("right-justify")).setStateAction(true);
        
        ((AbstractActionExt)manager.getAction("font-bold")).setStateAction(true);
        ((AbstractActionExt)manager.getAction("font-italic")).setStateAction(true);
        ((AbstractActionExt)manager.getAction("font-underline")).setStateAction(true);
        ((AbstractActionExt)manager.getAction("font-normal")).setStateAction(true);

        toolbarActions.addAll(actions);
        toolbarActions.remove(fileMenu);
        toolbarActions.remove(textMenu);
        toolbarActions.add(2, null);
        
//        dsb.setShadowSize(5);
//        dsb.setShowRightShadow(false);
        
        initComponents();

//        menuPanel.setBorder(dsb);
        
        toolBar.putClientProperty(Options.HEADER_STYLE_KEY, HeaderStyle.BOTH);
        toolBar.setBorderPainted(false);
        toolBar.setMargin(new Insets(0, 0, 0, 0));
        toolBar.setBorder(null);
        
        TargetManager targetManager = TargetManager.getInstance();
        targetManager.addTarget(editorPane);
        
        JMenuBar menuBar = new ActionContainerFactory(manager).createMenuBar(actions);
        menuBar.setBorderPainted(false);
        menuBar.putClientProperty(Options.HEADER_STYLE_KEY, HeaderStyle.BOTH);
        menuPanel.add(menuBar, BorderLayout.NORTH);
    }

    private Icon loadIcon(String path) {
        return new ImageIcon(getClass().getResource(path));
    }
    
    public String getHtmlDescription() {
        return "<html>Demonstrates various uses of the Action framework.</html>";
    }

    public String getName() {
        return "Action Framework";
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        menuPanel = new javax.swing.JPanel();
        toolBar = new ActionContainerFactory(manager).createToolBar(toolbarActions);
        jScrollPane1 = new javax.swing.JScrollPane();
        editorPane = new JXEditorPane("text/rtf", "");

        setLayout(new java.awt.GridBagLayout());

        menuPanel.setLayout(new java.awt.BorderLayout());

        toolBar.setBorder(null);
        menuPanel.add(toolBar, java.awt.BorderLayout.CENTER);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 1.0;
        add(menuPanel, gridBagConstraints);

        jScrollPane1.setViewportView(editorPane);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(12, 12, 11, 11);
        add(jScrollPane1, gridBagConstraints);

    }
    // </editor-fold>//GEN-END:initComponents
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private org.jdesktop.swingx.JXEditorPane editorPane;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel menuPanel;
    private javax.swing.JToolBar toolBar;
    // End of variables declaration//GEN-END:variables
}
