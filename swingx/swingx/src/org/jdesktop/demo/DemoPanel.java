/*
 * $Id: DemoPanel.java,v 1.8 2005/06/28 13:18:25 kleopatra Exp $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */

package org.jdesktop.demo;

import java.awt.Container;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JMenuBar;
import javax.swing.border.Border;

import org.jdesktop.swingx.JXPanel;

/**
 *
 * @author Richard Bair
 */
public abstract class DemoPanel extends JXPanel {
    private Icon icon;

    /**
     * the border to use for lists. Set top/bottom only
     */
    protected Border listBorder = BorderFactory.createEmptyBorder(3, 0, 3, 0);

    /**
     * border to use for left/right margin in list.
     */
    protected Border marginBorder = BorderFactory.createEmptyBorder(0, 3, 0, 3);

    /**
     * the border to use for trees. 
     */
    protected Border treeBorder = BorderFactory.createEmptyBorder(3, 3, 3, 3);
    /**
     * the border to use for description editorpane in a titled panel:
     * lines up with caption text. Ugly!
     */
    protected Border descriptionBorder =BorderFactory.createEmptyBorder(3, 12, 3, 3);
    
    /** Creates a new instance of DemoPanel */
    public DemoPanel() {
    }
    
    /**
     * the description to use in the TOC.
     * defaults to class name - implementations should override
     * to return a handy description.
     */
    public String getName() {
        // defaults to class name
        String className = getClass().getName();
        return className.substring(className.lastIndexOf(".")+1);
    }
 
    /**
     * the title to use in the information panel.
     * defaults to getName();
     * 
     * @return
     */
    public String getInformationTitle() {
        return getName();
    }
    
    public File getSourceRootDir() {
        try {
            return new File(getClass().getResource("/sources").toURI());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public String getHowToURLString() {
        // defaults JDNC documentation website
        return "https://jdnc.dev.java.net/documentation/index.html";
    }    

    /**
     * @return File object corresponding to html-ized view of source file
     */
    public File getSourceFile() {
        try {
            String relativeSourceDir = getClass().getName().replaceAll("\\.", File.separator);
            return new File(getSourceRootDir(), relativeSourceDir + ".java.html");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Icon getIcon() {
        return icon;
    }
    
    public void setIcon(Icon i) {
        icon = i;
    }

    public void addMenuItems(JMenuBar menuBar) {
    }
    
    /** 
     * Returns an HTML-formatted string describing the demo; this can be several 
     * sentences long. */
    public String getHtmlDescription() {
        return "<html>" + getName() + "</html>";
    }
    
    /** 
     * Returns a Container with the GUI for the demo. The Container will already
     * be instantiated and configured, and not dependent on external components or
     * configuration; ready-to-use and to add to a frame for display. 
     */
    public Container getContent() {
        return this;
    }
}
