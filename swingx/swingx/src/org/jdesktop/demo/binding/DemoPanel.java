/*
 * DemoPanel.java
 *
 * Created on April 18, 2005, 8:16 AM
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package org.jdesktop.demo.binding;

import java.awt.Container;

/**
 * Defines a single GUI panel used in a multi-panel graphical demo. A DemoPanel 
 * delivers a self-contained Container which displays some GUI functionality, and
 * which has a name and description. The host for the whole demo can use the name
 * to allow selection of the demo, and the description to describe it to the user.
 * To use, just implement this interface and return your GUI Container
 * in {@link #getContents()}. 
 *
 * @author rbair
 */
public interface DemoPanel {
    /** Returns the (simple) name of the demo. */
    public String getName();
    
    /** 
     * Returns an HTML-formatted string describing the demo; this can be several 
     * sentences long. */
    public String getHtmlDescription();
    
    /** 
     * Returns a Container with the GUI for the demo. The Container will already
     * be instantiated and configured, and not dependent on external components or
     * configuration; ready-to-use and to add to a frame for display. 
     */
    public Container getContents();
}
